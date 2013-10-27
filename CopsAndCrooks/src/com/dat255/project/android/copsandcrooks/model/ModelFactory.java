package com.dat255.project.android.copsandcrooks.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * A crook pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class ModelFactory {
	
	private static ModelFactory instance = null;
	
	private static final String absolutPath = Gdx.files.getLocalStoragePath() + "saved-games/";
	
	private ModelFactory(){}
	
	public static ModelFactory getInstance(){
		if(instance == null){
			instance = new ModelFactory();
		}
		return instance;
	}
	
	/**
	 * Loads a game from scratch or with players already placed 
	 * @param interact
	 * @return - a GameModel
	 */
	public GameModel loadGameModel(GameItem gameitem, TiledMapTileLayer interact, boolean isGameHosted){
		// Creates a mediator
		Mediator mediator = new Mediator();
		
		AbstractWalkableTile policeCarStart = null;
		
		//Creates a matrix that will contain all the different tiles
		AbstractWalkableTile[][] walkable = new AbstractWalkableTile[interact.getWidth()][interact.getHeight()];
		
		List<PoliceStationTile> listOfPolicestationtile = new ArrayList<PoliceStationTile>();
		
		List<HideoutTile> listOfHideouts = new ArrayList<HideoutTile>();
		
		// List to register all the different metrostops
		List<MetroStopTile> red = new ArrayList<MetroStopTile>();
		List<MetroStopTile> blue = new ArrayList<MetroStopTile>();
		List<MetroStopTile> green = new ArrayList<MetroStopTile>();
		for(int i = 0; i < interact.getWidth(); i++){
			for(int j = 0; j < interact.getHeight(); j++){
				//try catch made because of null pointer exception when we don't have a walkable tile when we read it from the .tmx file
				try{
					
					switch(interact.getCell(i, j).getTile().getId()){
					case 1: 	// According to the tileset case 1 is the road tiles
						walkable[i][j] = new RoadTile(new Point(i, j), mediator);
						break;						
					case 2: 	// According to the tileset case 2 is the get away tiles
						walkable[i][j] = new GetAwayTile(new Point(i, j), mediator);
						break;
					case 3: 	// According to the tileset case 3 is the Bank tiles containing 2000
						walkable[i][j] = new RobbableBuildingTile(new Point(i, j), mediator, Values.CASH_BANK_LOWEST);
						break;
					case 4: 	// According to the tileset case 4 is the Bank tiles containing 5000
						walkable[i][j] = new  RobbableBuildingTile(new Point(i, j), mediator, Values.CASH_BANK_LOW);
						break;
					case 5: 	// According to the tileset case 5 is the Bank tiles containing 10000
						walkable[i][j] = new  RobbableBuildingTile(new Point(i, j), mediator, Values.CASH_BANK_HIGH);
						break;
					case 6: 	// According to the tileset case 6 is the Bank tiles containing 20000
						walkable[i][j] = new  RobbableBuildingTile(new Point(i, j), mediator, Values.CASH_BANK_HIGHEST);
						break;
					case 7: 	// According to the tileset case 8 is the Travelagency tiles
						walkable[i][j] = new TravelAgencyTile(new Point(i, j), mediator);
						break;
					case 8: 	// According to the tileset case 8 is the blue metro line tile
						walkable[i][j] = new MetroStopTile(new Point(i, j), mediator);
						blue.add((MetroStopTile) walkable[i][j]);
						break;
					case 9: 	// According to the tileset case 9 is the green metro line tile
						walkable[i][j] = new MetroStopTile(new Point(i, j), mediator);
						green.add((MetroStopTile) walkable[i][j]);
						break;
					case 10: 	// According to the tileset case 10 is the red metro line tile
						walkable[i][j] = new MetroStopTile(new Point(i, j), mediator);
						red.add((MetroStopTile) walkable[i][j]);
						break;
					case 11: 	// According to the tileset case 11 is the IntelligenceAgency tiles
						walkable[i][j] = new IntelligenceAgencyTile(new Point(i, j), mediator);
						break;
					case 12: 	// According to the tileset case 12 is the Hiding tiles
						walkable[i][j] = new HideoutTile(new Point(i, j), mediator);
						listOfHideouts.add((HideoutTile) walkable[i][j]);
						break;
					case 13: 	// According to tileset case 13 will be the coordinate of the policeCar (this will be a road tile)
						walkable[i][j] = new RoadTile(new Point(i, j), mediator);
						policeCarStart = walkable[i][j];
						break;
					case 14: 	// According to the tileset case 3 is the polisoffice tiles						
						walkable[i][j] = new PoliceStationTile(new Point(i, j), mediator);
						listOfPolicestationtile.add((PoliceStationTile) walkable[i][j]);
						break;
					}
				} catch (NullPointerException e){
					walkable[i][j] = null;
				}
			}
		}
		//Creates a all Tramlines
		List<MetroLine> metroLines = new ArrayList<MetroLine>();
		metroLines.add(new MetroLine(green));
		metroLines.add(new MetroLine(blue));
		metroLines.add(new MetroLine(red));
		
		List<Player> players = new ArrayList<Player>();
		List<PlayerItem> playerItems = gameitem.getPlayers();
		int numberOfOfficers = gameitem.getCurrentPlayerCount()-1;
		int crookID = 1;
		Random rand = new Random();
		for (PlayerItem playerItem : playerItems) {
			List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
			if (playerItem.getRole() == Role.Cop) {
				for (int j = 0; j < numberOfOfficers; j++) {
					if (!isGameHosted) {
						int k = rand.nextInt(listOfPolicestationtile.size() - 1);
						pawns.add(new Officer(listOfPolicestationtile.get(k), mediator, Values.ID_OFFICER + j));
						playerItem.addPawn(listOfPolicestationtile.get(k).getPosition(), Values.ID_OFFICER + j);
						listOfPolicestationtile.remove(k);
					} else {
						Point point = playerItem.getPawnItem(Values.ID_OFFICER + j).position;
						pawns.add(new Officer(walkable[point.x][point.y], mediator, Values.ID_OFFICER + j));
					}
				}
				if (!isGameHosted) {
					pawns.add(new CopCar(policeCarStart, mediator, Values.ID_COP_CAR));
					playerItem.addPawn(policeCarStart.getPosition(), Values.ID_COP_CAR);
				} else {
					Point point = playerItem.getPawnItem(Values.ID_COP_CAR).position;
					pawns.add(new CopCar(walkable[point.x][point.y], mediator, Values.ID_COP_CAR));
				}
				players.add(new Player(playerItem.getName(), pawns, Role.Cop, mediator, new Wallet(), playerItem.getID()));

			} else if (playerItem.getRole() == Role.Crook) {
				if (!isGameHosted) {
					int k = rand.nextInt(listOfHideouts.size() - 1);
					pawns.add(new Crook(listOfHideouts.get(k), mediator, crookID));
					playerItem.addPawn(listOfHideouts.get(k).getPosition(), crookID);
					listOfHideouts.remove(k);
				} else {
					Point point = playerItem.getPawnItem(crookID).position;
					pawns.add(new Crook(walkable[point.x][point.y], mediator, crookID));
				}

				players.add(new Player(playerItem.getName(), pawns, Role.Crook, mediator, new Wallet(), playerItem.getID()));
				++crookID;
			}
		}
		Player playerClient = null;
		for(Player player : players){
			if(player.getID().equals(GameClient.getInstance().getClientID())){
				playerClient = player;
				break;
			}
		}
		if(!isGameHosted){
			gameitem.setGameStarted(true);
			GameClient.getInstance().updateCurrentGameItem(gameitem);
		}
		new PathFinder(walkable, mediator, metroLines);
		return new GameModel(mediator, playerClient, players, walkable, metroLines, new Dice(mediator), gameitem.getName(), gameitem.getID());
	}
	
	/**
	 * Saves the current game model to a file
	 * @param game the model to be saved to a file
	 */
	public void saveModelToFile(GameModel game){
		File dir = new File(absolutPath, game.getName());
		if(!dir.exists()){
			dir.mkdirs();
		}
		File savefile = new File(dir, "model.ser");
		try {
			savefile.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(savefile));
			out.writeObject(game);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads a game model from a file
	 * @param name the name of the file
	 * @return a game model loaded from a file
	 */
	public GameModel loadModelFromFile(String name){
		File fileToLoad = new File(absolutPath, name + "/model.ser");
		System.out.println(fileToLoad.getPath() + "\n" + fileToLoad.exists());
		if(!fileToLoad.exists()){
			throw new NullPointerException(fileToLoad.getPath() + "\nWas not able to be loaded");
		}
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileToLoad));
			GameModel loadmodel = (GameModel) in.readObject();
			in.close();
			return loadmodel;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * Checks to see if the game has loaded a certain game model
	 * @param item the game 
	 * @return true- if the game has loaded the game model
	 */
	public boolean hasLoadedThisGameModel(GameItem item){
		return new File(absolutPath, item.getName() + "/model.ser").exists();
	}

	/**
	 * Deletes the locally stored model file.
	 * @param model the model to be deleted.
	 */
	public void deleteModelFile(GameModel model) {
		File mapToDelete = new File(absolutPath, model.getName());
		File fileToDelete = new File(absolutPath, model.getName() + "/model.ser");
		
		mapToDelete.delete();
		fileToDelete.delete();
	}
}

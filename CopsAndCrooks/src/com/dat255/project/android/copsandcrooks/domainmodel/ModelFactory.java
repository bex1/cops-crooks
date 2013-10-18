package com.dat255.project.android.copsandcrooks.domainmodel;

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

// WILL be used to further encapsulate model.
// The GameFactory accesses model from outside which limits encapsulation.
// It should instead be connected to this factory to get its model instances.
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
		List<TramStopTile> red = new ArrayList<TramStopTile>();
		List<TramStopTile> blue = new ArrayList<TramStopTile>();
		List<TramStopTile> green = new ArrayList<TramStopTile>();
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
						TravelAgencyTile.createTravelAgency(new Point(i, j), mediator);
						walkable[i][j] = TravelAgencyTile.getInstance();
						break;
					case 8: 	// According to the tileset case 8 is the blue metro line tile
						walkable[i][j] = new TramStopTile(new Point(i, j), mediator);
						blue.add((TramStopTile) walkable[i][j]);
						break;
					case 9: 	// According to the tileset case 9 is the green metro line tile
						walkable[i][j] = new TramStopTile(new Point(i, j), mediator);
						green.add((TramStopTile) walkable[i][j]);
						break;
					case 10: 	// According to the tileset case 10 is the red metro line tile
						walkable[i][j] = new TramStopTile(new Point(i, j), mediator);
						red.add((TramStopTile) walkable[i][j]);
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
		List<TramLine> tramLines = new ArrayList<TramLine>();
		tramLines.add(new TramLine(green));
		tramLines.add(new TramLine(blue));
		tramLines.add(new TramLine(red));
		
		List<Player> players = new ArrayList<Player>();
		List<PlayerItem> playerItems = gameitem.getPlayers();
		int numberOfOfficers = gameitem.getCurrentPlayerCount()-1;
		int crookID = 1;
		Random rand = new Random();
		for (PlayerItem playerItem : playerItems) {
			List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
			System.out.println(playerItem.getRole());
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
				players.add(new Player(playerItem.getName(), pawns, Role.Cop, mediator, playerItem.getID()));

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

				players.add(new Player(playerItem.getName(), pawns, Role.Crook, mediator, playerItem.getID()));
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
		new PathFinder(walkable, mediator, tramLines);
		return new GameModel(mediator, playerClient, null, players, walkable, tramLines, gameitem.getName(), gameitem.getID(), 0);
	}
	
	
	/**
	 * This methods reads from a file and creates a new GameModel from a serialized model 
	 * @return - Fully working GameModel
	 * @throws Exception
	 */
	public GameModel loadLocalGameModel(GameModel model){
		String gameName = model.getName();
		// Creates a mediator
		Mediator mediator = new Mediator();
		
		// Gets the old Players and creates a ArrayList for the new ones
		Collection<? extends IPlayer> oldPlayers = model.getPlayers();
		List<Player> newPlayers = new ArrayList<Player>();
		
		// Get the old 2d array of tiles and creates an 2D array as big as the old
		IWalkableTile[][] oldWalkableTile = model.getWalkabletiles();
		AbstractWalkableTile[][] newWalkableTile = new AbstractWalkableTile[oldWalkableTile.length-1]
				[oldWalkableTile[1].length-1];
		
		// Get All the metro lines and puts it in a array of size 3
		TramLine[] oldMetroLines = new TramLine[model.getTramLines().size()-1];
		oldMetroLines = model.getTramLines().toArray(oldMetroLines);
		
		// gets the old tramstop tiles arrays och creates 3 new tramstop to put them in with the new mediator
		List<TramStopTile> oldStop1 = oldMetroLines[0].getTramStops();
		List<TramStopTile> oldStop2 = oldMetroLines[1].getTramStops();
		List<TramStopTile> oldStop3 = oldMetroLines[2].getTramStops();
		List<TramStopTile> stop1 = new ArrayList<TramStopTile>();
		List<TramStopTile> stop2 = new ArrayList<TramStopTile>();
		List<TramStopTile> stop3 = new ArrayList<TramStopTile>();
		
		//Loads all the tiles again to give them the new mediator
		for(int i = 0 ; i <oldWalkableTile.length-1; i ++){
			for(int j = 0; j < oldWalkableTile[i].length- 1; j ++){
				if(oldWalkableTile[i][j] instanceof RoadTile){
					newWalkableTile[i][j] = new RoadTile(new Point(i, j), mediator);
				}else if(oldWalkableTile[i][j] instanceof GetAwayTile){
					newWalkableTile[i][j] = new GetAwayTile(new Point(i, j), mediator);
				}else if(oldWalkableTile[i][j] instanceof RobbableBuildingTile){
					newWalkableTile[i][j] = new RobbableBuildingTile(new Point(i, j), mediator, 
							((RobbableBuildingTile)oldWalkableTile[i][j]).getValue());
				}else if(oldWalkableTile[i][j] instanceof TravelAgencyTile){
					TravelAgencyTile.createTravelAgency(new Point(i, j), mediator);
					newWalkableTile[i][j] = TravelAgencyTile.getInstance();
				}else if(oldWalkableTile[i][j] instanceof TramStopTile){
					newWalkableTile[i][j] = new TramStopTile(new Point(i, j), mediator);
					if(oldStop1.contains(oldWalkableTile[i][j])){
						stop1.add((TramStopTile) newWalkableTile[i][j]);
					}else if(oldStop2.contains(oldWalkableTile[i][j])){
						stop2.add((TramStopTile) newWalkableTile[i][j]);
					}else if(oldStop3.contains(oldWalkableTile[i][j])){
						stop3.add((TramStopTile) newWalkableTile[i][j]);
					}
				}else if(oldWalkableTile[i][j] instanceof IntelligenceAgencyTile){
					newWalkableTile[i][j] = new IntelligenceAgencyTile(new Point(i,j), mediator);
				}else if(oldWalkableTile[i][j] instanceof HideoutTile){
					newWalkableTile[i][j] = new HideoutTile(new Point(i,j), mediator);
				}else if(oldWalkableTile[i][j] instanceof PoliceStationTile){
					newWalkableTile[i][j] = new PoliceStationTile(new Point(i,j), mediator);
				}else{
					newWalkableTile[i][j] = null;
				}
			}
		}
		
		// the new Tramlines that is based on the model you get
		List<TramLine> newTramLines = new ArrayList<TramLine>();
		newTramLines.add(new TramLine(stop1));
		newTramLines.add(new TramLine(stop2));
		newTramLines.add(new TramLine(stop3));
		
		//Loads the players and give them the new mediator
		Player playerClient = null;
		for(IPlayer player: oldPlayers){
			List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
			for(IMovable pawn: player.getPawns()){
				Point tilesPos = pawn.getCurrentTile().getPosition();
				AbstractWalkableTile tile = newWalkableTile[tilesPos.x][tilesPos.y];
				if(pawn instanceof Officer)
					pawns.add(new Officer(tile, mediator, pawn.getID()));
				else if(pawn instanceof CopCar)
					pawns.add(new CopCar(tile, mediator, pawn.getID()));
				else if(pawn instanceof Crook)
					pawns.add(new Crook(tile, mediator, pawn.getID()));
			}
			Player newPlayer = new Player(player.getName(), pawns, player.getPlayerRole(), mediator, player.getID());
			if(newPlayer.getID().equals(GameClient.getInstance().getClientID()))
				playerClient = newPlayer;				
			newPlayers.add(newPlayer);
		}
		Player currentPlayer = null;
		for(Player player: newPlayers){
			if(player.getID().equals(model.getCurrentPlayer().getID())){
				currentPlayer = player;
				break;
			}
		}
		System.out.println(model.getDiceResults());
		new PathFinder((AbstractWalkableTile[][]) newWalkableTile, mediator, newTramLines);
		return new GameModel(mediator, playerClient, currentPlayer, newPlayers, newWalkableTile, newTramLines, gameName, model.getID(), model.getTurnID());
	}
	

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
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public boolean hasLoadedThisGameModel(GameItem item){
		return new File(absolutPath, item.getName() + "/model.ser").exists();
	}

	public void deleteModelFile(GameModel model) {
		File mapToDelete = new File(absolutPath, model.getName());
		File fileToDelete = new File(absolutPath, model.getName() + "/model.ser");
		
		mapToDelete.delete();
		fileToDelete.delete();
	}
}

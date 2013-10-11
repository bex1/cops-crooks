package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Timer;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.PawnItem;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;
import com.dat255.project.android.copsandcrooks.utils.Point;

// WILL be used to furter encapsulate model.
// The GameFactory accesses model from outside which limits encapsulation.
// It should instead be connected to this factory to get its model instances.
public class ModelFactory {
	
	private static ModelFactory instance = null;
	
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
	 * @param userInfo
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
		
		// List to registrat all the diffrent metrostops
		List<TramStopTile> red = new ArrayList<TramStopTile>();
		List<TramStopTile> blue = new ArrayList<TramStopTile>();
		List<TramStopTile> green = new ArrayList<TramStopTile>();
		for(int i = 0; i < interact.getWidth(); i++){
			for(int j = 0; j < interact.getHeight(); j++){
				//try catch made because of null pointer exception when we dont have a walkable tile when we read it from the .tmx file
				try{
					
					switch(interact.getCell(i, j).getTile().getId()){
					case 1: 	// According to the tileset case 1 is the road tiles
						walkable[i][j] = new RoadTile(new Point(i, j), mediator);
						break;						
					case 2: 	// According to the tileset case 2 is the get away tiles
						walkable[i][j] = new GetAwayTile(new Point(i, j), mediator);
						break;
					case 3: 	// According to the tileset case 3 is the Bank tiles containing 2000
						walkable[i][j] = new RobbableBuildingTile(new Point(i, j), mediator, 2000);
						break;
					case 4: 	// According to the tileset case 4 is the Bank tiles containing 5000
						walkable[i][j] = new  RobbableBuildingTile(new Point(i, j), mediator, 5000);
						break;
					case 5: 	// According to the tileset case 5 is the Bank tiles containing 10000
						walkable[i][j] = new  RobbableBuildingTile(new Point(i, j), mediator, 10000);
						break;
					case 6: 	// According to the tileset case 6 is the Bank tiles containing 20000
						walkable[i][j] = new  RobbableBuildingTile(new Point(i, j), mediator, 20000);
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
					case 13: 	// According to tileset case 13 will be the coordinate of the poliscar(this will be a road tile)
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
		List<PlayerItem> playeritems = gameitem.getPlayers();
		int numberOfOfficers = gameitem.getCurrentPlayerCount()-1;
		int crookID = 1;
		Random rand = new Random();
		for (int i = 0; i < playeritems.size(); i ++ ) {
			List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
			if (playeritems.get(i).getRole() == Role.Cop) {
				for (int j = 0; j < numberOfOfficers; j++) {
					if(!isGameHosted){
						int k = rand.nextInt(listOfPolicestationtile.size()-1);
						pawns.add(new Officer(listOfPolicestationtile.get(k), mediator, 10 + j));
						playeritems.get(i).addPawn(listOfPolicestationtile.get(k).getPosition(), 10 + j);
						listOfPolicestationtile.remove(k);
					}else{
						Point point = playeritems.get(i).getPawnItem(10 + j).position;
						pawns.add(new Officer(walkable[point.x][point.y], mediator, 10 + j));
					}
				}
				if(!isGameHosted){
					pawns.add(new CopCar(policeCarStart, mediator, 20));
					playeritems.get(i).addPawn(policeCarStart.getPosition(), 20);
				}else{
					Point point = playeritems.get(i).getPawnItem(crookID).position;
					pawns.add(new CopCar(walkable[point.x][point.y], mediator, crookID));
				}
				players.add( new Player(playeritems.get(i).getName(), pawns, Role.Cop, mediator));
				
			} else if (playeritems.get(i).getRole() == Role.Crook) {
				if(!isGameHosted){
					int k = rand.nextInt(listOfHideouts.size()-1);
					pawns.add(new Crook(listOfHideouts.get(k), mediator, crookID));
					playeritems.get(i).addPawn(listOfPolicestationtile.get(k).getPosition(), crookID);
					listOfHideouts.remove(k);
				}else{
					Point point = playeritems.get(i).getPawnItem(crookID).position;
					pawns.add(new Crook(walkable[point.x][point.y], mediator, crookID));
				}
				
				players.add( new Player(playeritems.get(i).getName(), pawns, Role.Crook, mediator));
				++crookID;
			}
		}
		
		new PathFinder(walkable, mediator, tramLines);
		return new GameModel(mediator, players.get(0), players, walkable, tramLines, gameitem.getName(), gameitem.getID());
	}
	
	
	/**
	 * This methods reads from a file and creates a new GameModel from a serialized model 
	 * @return - Fully working gamemodel
	 * @throws Exception
	 */
	public GameModel loadLocalGameModel(GameModel model){
		String gameName = model.getName();
		// Creates a mediator
		Mediator mediator = new Mediator();
		Collection<? extends IPlayer> oldPlayers = model.getPlayers();
		List<Player> newPlayers = new ArrayList<Player>();
		
		//Loads all the tiles again to give them the new mediator
		IWalkableTile[][] oldWalkableTile = model.getWalkabletiles();
		AbstractWalkableTile[][] newWalkableTile = new AbstractWalkableTile[oldWalkableTile.length-1]
				[oldWalkableTile[1].length-1];
		// Get All the metro lines
		TramLine[] oldMetroLines = new TramLine[model.getTramLines().size()-1];
		oldMetroLines = model.getTramLines().toArray(oldMetroLines);
		//Lists to get all the tramstops
		List<TramStopTile> oldStop1 = oldMetroLines[0].getTramStops();
		List<TramStopTile> oldStop2 = oldMetroLines[1].getTramStops();
		List<TramStopTile> oldStop3 = oldMetroLines[2].getTramStops();
		List<TramStopTile> stop1 = new ArrayList<TramStopTile>();
		List<TramStopTile> stop2 = new ArrayList<TramStopTile>();
		List<TramStopTile> stop3 = new ArrayList<TramStopTile>();
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
					//TODO Fixa så att man hämtar de olika setten av tramstops och gör en tramline och sedan flera tramlines
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
		for(IPlayer player: oldPlayers){
			List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
			for(IMovable pawn: player.getPawns()){
				Point tilesPos = pawn.getCurrentTile().getPosition();
				AbstractWalkableTile tile = (AbstractWalkableTile) newWalkableTile[tilesPos.x][tilesPos.y];
				if(pawn instanceof Officer)
					pawns.add(new Officer(tile, mediator, pawn.getID()));
				else if(pawn instanceof CopCar)
					pawns.add(new CopCar(tile, mediator, pawn.getID()));
				else if(pawn instanceof Crook)
					pawns.add(new Crook(tile, mediator, pawn.getID()));
			}
			newPlayers.add(new Player(player.getName(), pawns, player.getPlayerRole(), mediator));
		}
		
		new PathFinder((AbstractWalkableTile[][]) newWalkableTile, mediator, newTramLines);
		return new GameModel(mediator, newPlayers.get(0), newPlayers, newWalkableTile, newTramLines, gameName, model.getDiceResults());
	}
}

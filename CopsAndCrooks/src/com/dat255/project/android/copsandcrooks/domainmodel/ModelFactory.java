package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.dat255.project.android.copsandcrooks.utils.Point;

// WILL be used to furter encapsulate model.
// The GameFactory accesses model from outside which limits encapsulation.
// It should instead be connected to this factory to get its model instances.
public class ModelFactory {

	public static GameModel loadGameModel(TiledMap map, TiledMapTileLayer interact, Map<String, Role> userInfo){
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
		int numberOfOfficers = userInfo.keySet().size();
		int crookID = 1;
		Random rand = new Random();
		for (String name : userInfo.keySet()) {
			List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
			if (userInfo.get(name) == Role.Cop) {
				for (int i = 0; i < numberOfOfficers; i++) {
					int k = rand.nextInt(listOfPolicestationtile.size()-1);
					pawns.add(new Officer(listOfPolicestationtile.get(k), mediator, 10 + i));
					listOfPolicestationtile.remove(k);
				}
				pawns.add(new CopCar(policeCarStart, mediator, 20));
				
				players.add( new Player(name, pawns, Role.Cop, mediator));
				
			} else if (userInfo.get(name) == Role.Crook) {
				int k = rand.nextInt(listOfHideouts.size()-1);
				pawns.add(new Crook(listOfHideouts.get(k), mediator, crookID));
				listOfHideouts.remove(k);
				
				// Test let the crook start wanted to test catching
				((Crook) pawns.get(pawns.size()-1)).setWanted(true);
				// Test end
				
				players.add( new Player(name, pawns, Role.Crook, mediator));
				++crookID;
			}
		}
		
		new PathFinder(walkable, mediator, tramLines);
		return new GameModel(mediator, players.get(0), players, walkable, tramLines);
	}
	
	public static GameModel loadHostedGameModel(GameModel model){
		// Creates a mediator
		Mediator mediator = new Mediator();
				
		new PathFinder((AbstractWalkableTile[][]) model.getWalkabletiles(), mediator, new ArrayList<TramLine>());
		return new GameModel(mediator, (Player)model.getPlayerClient(), (List<Player>) model.getPlayers()
				, model.getWalkabletiles(), model.getTramLines());
	}
}
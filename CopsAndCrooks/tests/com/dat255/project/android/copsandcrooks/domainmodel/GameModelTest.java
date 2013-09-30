package com.dat255.project.android.copsandcrooks.domainmodel;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.utils.Point;

public class GameModelTest {

	@Before
	public void setUp() throws Exception {
		

		
	}

	@Test
	public void GameModelTest() {
		
		try{
			GameModel gameModel = new GameModel(null, null, null);
			fail();
		}catch (IllegalArgumentException e){
			//expected
			
		}
		
	}
	
	@Test
	public void NextPlayerTest(){
		
		IMediator mediator = new Mediator();
		AbstractPawn crook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), mediator);
		AbstractPawn officer = new Officer(new RoadTile(new Point(0, 0), new Mediator()), mediator);
		LinkedList <AbstractPawn> crookPlayer = new LinkedList<AbstractPawn>();
		LinkedList <AbstractPawn> officerPlayer = new LinkedList<AbstractPawn>();
		officerPlayer.add(officer);
		crookPlayer.add(crook);		
		Player player1 = new Player("tjuv", crookPlayer, Role.Crook, mediator);
		Player player2 = new Player("polis", officerPlayer, Role.Police, mediator);
		ArrayList <Player>players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		AbstractWalkableTile [][] tiles = new AbstractWalkableTile [3][3];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				tiles [i][j] = new RoadTile(new Point(i, j), mediator);
			}
		}
		GameModel gameModel = new GameModel(mediator, players, tiles);
		
		gameModel.startGame();
		
		assertTrue(gameModel.getCurrentPlayer() == player1);
		
		gameModel.nextPlayer();
		
		
		assertTrue(gameModel.getCurrentPlayer() == player2);
	}
	
	@Test
	public void moveToEmptyPoliceStationTileTest(){
		IMediator mediator = new Mediator();
		AbstractPawn crook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), mediator);
		AbstractPawn officer = new Officer(new RoadTile(new Point(0, 0), new Mediator()), mediator);
		LinkedList <AbstractPawn> crookPlayer = new LinkedList<AbstractPawn>();
		LinkedList <AbstractPawn> officerPlayer = new LinkedList<AbstractPawn>();
		officerPlayer.add(officer);
		crookPlayer.add(crook);		
		Player player1 = new Player("tjuv", crookPlayer, Role.Crook, mediator);
		Player player2 = new Player("polis", officerPlayer, Role.Police, mediator);
		LinkedList <Player>players = new LinkedList<Player>();
		players.add(player1);
		players.add(player2);
		AbstractWalkableTile [][] tiles = new AbstractWalkableTile [3][3];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				tiles [i][j] = new RoadTile(new Point(i, j), mediator);
			}
		}
		tiles [2][2] = new PoliceStationTile(new Point(2, 2), mediator);
		GameModel gameModel = new GameModel(mediator, players, tiles);
		
		gameModel.startGame();
		//Sets the position of the pawn to ensure that it doesn't start in the police station.
		gameModel.getCurrentPlayer().getCurrentPawn().setCurrentTile(tiles[0][0]);
		
		gameModel.moveToEmptyPoliceStationTile(gameModel.getCurrentPlayer().getCurrentPawn());
		
		assertTrue(gameModel.getCurrentPlayer().getCurrentPawn().getCurrentTile()== tiles [2][2]);
	}
	
	@Test
	public void notifyWhatICollidedWithTest(){
		IMediator mediator = new Mediator();
		AbstractPawn crook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), mediator);
		AbstractPawn officer = new Officer(new RoadTile(new Point(0, 0), new Mediator()), mediator);
		LinkedList <AbstractPawn> crookPlayer = new LinkedList<AbstractPawn>();
		LinkedList <AbstractPawn> officerPlayer = new LinkedList<AbstractPawn>();
		officerPlayer.add(officer);
		crookPlayer.add(crook);		
		Player player1 = new Player("tjuv", crookPlayer, Role.Crook, mediator);
		Player player2 = new Player("polis", officerPlayer, Role.Police, mediator);
		LinkedList <Player>players = new LinkedList<Player>();
		players.add(player1);
		//to specify where officer is in the list
		players.addFirst(player2);
		AbstractWalkableTile [][] tiles = new AbstractWalkableTile [3][3];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				tiles [i][j] = new RoadTile(new Point(i, j), mediator);
			}     
		}
		tiles [2][2] = new PoliceStationTile(new Point(2, 2), mediator);
		tiles [2][1] = new PoliceStationTile(new Point(2, 1), mediator);
		GameModel gameModel = new GameModel(mediator, players, tiles);
		players.get(0).getCurrentPawn().setCurrentTile(tiles [1][1]);
		((Crook)players.get(1).getCurrentPawn()).setWanted(true);
		players.get(1).getCurrentPawn().setCurrentTile(tiles [1][1]);
		
		
		
		gameModel.notifyWhatICollidedWith(players.get(0).getCurrentPawn());
		
		assertTrue(players.get(1).getCurrentPawn().getCurrentTile() == tiles [2][2] || players.get(1).getCurrentPawn().getCurrentTile() == tiles [2][1]);
		
		
	}

}

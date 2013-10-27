package com.dat255.project.android.copsandcrooks.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.model.AbstractPawn;
import com.dat255.project.android.copsandcrooks.model.Crook;
import com.dat255.project.android.copsandcrooks.model.IntelligenceAgencyTile;
import com.dat255.project.android.copsandcrooks.model.Mediator;
import com.dat255.project.android.copsandcrooks.model.Officer;
import com.dat255.project.android.copsandcrooks.model.Player;
import com.dat255.project.android.copsandcrooks.model.RoadTile;
import com.dat255.project.android.copsandcrooks.model.Role;
import com.dat255.project.android.copsandcrooks.utils.Point;

public class IntelligenceAgencyTileTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHinderGetAway(){
		IntelligenceAgencyTile intAgency = 
				new IntelligenceAgencyTile(new Point(), new Mediator());
	
		List<AbstractPawn> pawns1 = new ArrayList<AbstractPawn>();
		pawns1.add(new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 10));
		Player player1 = new Player(null, pawns1, Role.Cop, new Mediator(), null, null);

		List<AbstractPawn> pawns2 = new ArrayList<AbstractPawn>();
		Crook crook1 = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		crook1.setAttemptingGetAway(true);
		pawns2.add(crook1);
		Player player2 = new Player(null, pawns2, Role.Crook, new Mediator(), null, null);

		List<AbstractPawn> pawns3 = new ArrayList<AbstractPawn>();
		Crook crook2 = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 1);
		crook2.setAttemptingGetAway(false);
		pawns3.add(crook2);
		Player player3 = new Player(null, pawns3, Role.Crook, new Mediator(), null, null);
		
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		
		intAgency.hinderGetAway(playerList);
		
		assertTrue(!crook1.isAttemptingGetAway() && !crook2.isAttemptingGetAway());
	}
}

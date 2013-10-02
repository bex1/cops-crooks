package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.Mediator;
import com.dat255.project.android.copsandcrooks.domainmodel.Officer;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
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
		
		List<IMovable> pawns1 = new ArrayList<IMovable>();
		pawns1.add(new Officer(new Mediator()));
		Player player1 = new Player(null, pawns1, Role.Officer, new Mediator());

		List<IMovable> pawns2 = new ArrayList<IMovable>();
		Crook crook1 = new Crook(new Mediator());
		crook1.setAttemptingGetAway(true);
		pawns2.add(crook1);
		Player player2 = new Player(null, pawns2, Role.Crook, new Mediator());

		List<IMovable> pawns3 = new ArrayList<IMovable>();
		Crook crook2 = new Crook(new Mediator());
		crook2.setAttemptingGetAway(false);
		pawns3.add(crook2);
		Player player3 = new Player(null, pawns3, Role.Crook, new Mediator());
		
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		
		intAgency.hinderGetAway(playerList);
		
		assertTrue(!crook1.isAttemptingGetAway() && !crook2.isAttemptingGetAway());
	}
}

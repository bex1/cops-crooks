/**
 * 
 */
package com.dat255.project.android.copsandcrooks.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertArrayEquals;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.model.AbstractPawn;
import com.dat255.project.android.copsandcrooks.model.AbstractWalkableTile;
import com.dat255.project.android.copsandcrooks.model.Crook;
import com.dat255.project.android.copsandcrooks.model.Dice;
import com.dat255.project.android.copsandcrooks.model.Mediator;
import com.dat255.project.android.copsandcrooks.model.MetroLine;
import com.dat255.project.android.copsandcrooks.model.PathFinder;
import com.dat255.project.android.copsandcrooks.model.Player;
import com.dat255.project.android.copsandcrooks.model.RoadTile;
import com.dat255.project.android.copsandcrooks.model.Role;
import com.dat255.project.android.copsandcrooks.model.Wallet;
import com.dat255.project.android.copsandcrooks.utils.Point;


/**
 * @author Bex
 *
 */
public class PlayerTest {
	
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

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Player#Player(java.lang.String, java.util.List, com.dat255.project.android.copsandcrooks.model.Role, com.dat255.project.android.copsandcrooks.model.IMediator)}.
	 */
	@Test
	public final void testPlayer() {
		try {
			new Player("Kalle", null, Role.Crook, new Mediator(), null, null);
			fail("Should throw IllegalArgumentException since pawns not allowed to be null");
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			new Player("Kalle", new ArrayList<AbstractPawn>(), Role.Crook, new Mediator(), null, null);
			fail("Should throw IllegalArgumentException since pawns list is empty");
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			new Player("Kalle", new ArrayList<AbstractPawn>(), Role.Crook, null, null, null);
			fail("Should throw IllegalArgumentException since mediator is null");
		} catch (IllegalArgumentException e) {
			// expected
		}
		
		List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
		Crook c = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		pawns.add(c);
		
		try {
			new Player("Kalle", pawns, Role.Cop, new Mediator(), null, null);
			fail("Should throw IllegalArgumentException since the role of a pawn does" +
					" not match the role of the player");
		} catch (IllegalArgumentException e) {
			// expected
		}
		
		try {
			new Player("Kalle", pawns, Role.Crook, new Mediator(), null, null);

		} catch (IllegalArgumentException e) {
			fail("Should not throw IllegalArgumentException since the roles of the pawns does" +
					" match the role of the player");
		}
		
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Player#getPlayerRole()}.
	 */
	@Test
	public final void testGetPlayerRole() {
		List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
		Crook c = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		pawns.add(c);
		Role l = Role.Crook;
		Player player = new Player("Kalle", pawns, l, new Mediator(), null, null);
		assertSame("The player role should be Crook", player.getPlayerRole(), l);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Player#getPawns()}.
	 */
	@Test
	public final void testGetPawns() {
		List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
		Crook c = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		pawns.add(c);
		Player player = new Player("Kalle", pawns, Role.Crook, new Mediator(), null, null);
		assertArrayEquals("The player pawns should be the same as in the test", player.getPawns().toArray(), pawns.toArray());
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Player#getCurrentPawn()}.
	 */
	@Test
	public final void testGetCurrentPawn() {
		List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
		Crook c = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		Crook d = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 1);
		pawns.add(c);
		pawns.add(d);
		Player player = new Player("Kalle", pawns, Role.Crook, new Mediator(), null, null);
		assertSame("The current pawn after init should be the first pawn added", player.getCurrentPawn(), c);
		Crook b = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 2);
		player.setCurrentPawn(b); 
		assertSame("Pawn should not have changed, since b is not the players pawn", player.getCurrentPawn(), c);
		player.setCurrentPawn(d); 
		assertSame("Pawn should have changed, since d is the players pawn", player.getCurrentPawn(), d);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Player#getName()}.
	 */
	@Test
	public final void testGetName() {
		List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
		Crook c = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		pawns.add(c);
		String n = "Kalle";
		Player player = new Player(n, pawns, Role.Crook, new Mediator(), null, n);
		assertSame("The name should be Kalle", player.getName(), n);
		player = new Player(null, pawns, Role.Crook, new Mediator(), null, n);
		assertTrue("The name should be an empty string", player.getName().isEmpty());
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Player#getWallet()}.
	 */
	@Test
	public final void testGetWallet() {
		List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
		Crook c = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		pawns.add(c);
		Player player = new Player("Kalle", pawns, Role.Crook, new Mediator(), null, null);
		Wallet w = player.getWallet();
		assertNotNull("Should never be null", w);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Player#rollDice()}.
	 */
	@Test
	@SuppressWarnings("unused")
	public final void testRollDice() {
		List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
		Crook c = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		pawns.add(c);
		Mediator mediator = new Mediator();
		final Player player = new Player("Kalle", pawns, Role.Crook, mediator, null, null);
		PathFinder pathFinder = new PathFinder(new AbstractWalkableTile[1][1], mediator, new ArrayList<MetroLine>());
		Dice dice = new Dice(mediator);
		player.addObserver(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getSource() == player) {
					if (evt.getPropertyName() == Player.PROPERTY_POSSIBLE_PATHS){
						// TODO lacking method 
						
					} else if (evt.getPropertyName() == Player.PROPERTY_DICE_RESULT){
						// TODO lacking method 
					} else {
						fail();
					}
				} else {
					fail();
				}
			}
			
		});
		player.rollDice();
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Player#choosePath(com.dat255.project.android.copsandcrooks.model.TilePath)}.
	 */
	@Test
	public final void testChoosePath() {
		// TODO need getter firest
	}
}

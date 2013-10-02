/**
 * 
 */
package com.dat255.project.android.copsandcrooks.domainmodel;

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

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;

/**
 * @author Bex
 *
 */
public class PlayerTest {
	
	private Player player;

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
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Player#Player(java.lang.String, java.util.List, com.dat255.project.android.copsandcrooks.domainmodel.Role, com.dat255.project.android.copsandcrooks.domainmodel.IMediator)}.
	 */
	@Test
	public final void testPlayer() {
		try {
			Player player = new Player("Kalle", null, Role.Crook, new Mediator());
			fail("Should throw IllegalArgumentException since pawns not allowed to be null");
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			Player player = new Player("Kalle", new ArrayList<IMovable>(), Role.Crook, new Mediator());
			fail("Should throw IllegalArgumentException since pawns list is empty");
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			Player player = new Player("Kalle", new ArrayList<IMovable>(), Role.Crook, null);
			fail("Should throw IllegalArgumentException since mediator is null");
		} catch (IllegalArgumentException e) {
			// expected
		}
		
		List<IMovable> pawns = new ArrayList<IMovable>();
		Crook c = new Crook(new Mediator());
		pawns.add(c);
		
		try {
			Player player = new Player("Kalle", pawns, Role.Cop, new Mediator());
			fail("Should throw IllegalArgumentException since the role of a pawn does" +
					" not match the role of the player");
		} catch (IllegalArgumentException e) {
			// expected
		}
		
		try {
			Player player = new Player("Kalle", pawns, Role.Crook, new Mediator());

		} catch (IllegalArgumentException e) {
			fail("Should not throw IllegalArgumentException since the roles of the pawns does" +
					" match the role of the player");
		}
		
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Player#getPlayerRole()}.
	 */
	@Test
	public final void testGetPlayerRole() {
		List<IMovable> pawns = new ArrayList<IMovable>();
		Crook c = new Crook(new Mediator());
		pawns.add(c);
		Role l = Role.Crook;
		Player player = new Player("Kalle", pawns, l, new Mediator());
		assertSame("The player role should be Crook", player.getPlayerRole(), l);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Player#getPawns()}.
	 */
	@Test
	public final void testGetPawns() {
		List<IMovable> pawns = new ArrayList<IMovable>();
		Crook c = new Crook(new Mediator());
		pawns.add(c);
		Player player = new Player("Kalle", pawns, Role.Crook, new Mediator());
		assertArrayEquals("The player pawns should be the same as in the test", player.getPawns().toArray(), pawns.toArray());
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Player#getCurrentPawn()}.
	 */
	@Test
	public final void testGetCurrentPawn() {
		List<IMovable> pawns = new ArrayList<IMovable>();
		Crook c = new Crook(new Mediator());
		Crook d = new Crook(new Mediator());
		pawns.add(c);
		pawns.add(d);
		Player player = new Player("Kalle", pawns, Role.Crook, new Mediator());
		assertSame("The current pawn after init should be the first pawn added", player.getCurrentPawn(), c);
		Crook b = new Crook(new Mediator());
		player.setCurrentPawn(b); 
		assertSame("Pawn should not have changed, since b is not the players pawn", player.getCurrentPawn(), c);
		player.setCurrentPawn(d); 
		assertSame("Pawn should have changed, since d is the players pawn", player.getCurrentPawn(), d);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Player#getName()}.
	 */
	@Test
	public final void testGetName() {
		List<IMovable> pawns = new ArrayList<IMovable>();
		Crook c = new Crook(new Mediator());
		pawns.add(c);
		String n = "Kalle";
		Player player = new Player(n, pawns, Role.Crook, new Mediator());
		assertSame("The name should be Kalle", player.getName(), n);
		player = new Player(null, pawns, Role.Crook, new Mediator());
		assertTrue("The name should be an empty string", player.getName().isEmpty());
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Player#getWallet()}.
	 */
	@Test
	public final void testGetWallet() {
		List<IMovable> pawns = new ArrayList<IMovable>();
		Crook c = new Crook(new Mediator());
		pawns.add(c);
		Player player = new Player("Kalle", pawns, Role.Crook, new Mediator());
		Wallet w = player.getWallet();
		assertNotNull("Should never be null", w);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Player#rollDice()}.
	 */
	@Test
	public final void testRollDice() {
		List<IMovable> pawns = new ArrayList<IMovable>();
		Crook c = new Crook(new Mediator());
		pawns.add(c);
		Mediator mediator = new Mediator();
		final Player player = new Player("Kalle", pawns, Role.Crook, mediator);
		PathFinder pathFinder = new PathFinder(new IWalkableTile[1][1], mediator);
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
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Player#choosePath(com.dat255.project.android.copsandcrooks.domainmodel.TilePath)}.
	 */
	@Test
	public final void testChoosePath() {
		// TODO need getter firest
	}
}

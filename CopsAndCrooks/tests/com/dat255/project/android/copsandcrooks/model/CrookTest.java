/**
 * 
 */
package com.dat255.project.android.copsandcrooks.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.model.AbstractWalkableTile;
import com.dat255.project.android.copsandcrooks.model.Crook;
import com.dat255.project.android.copsandcrooks.model.IPawn;
import com.dat255.project.android.copsandcrooks.model.Officer;
import com.dat255.project.android.copsandcrooks.model.RoadTile;
import com.dat255.project.android.copsandcrooks.model.Role;
import com.dat255.project.android.copsandcrooks.model.TilePath;
import com.dat255.project.android.copsandcrooks.model.Wallet;
import com.dat255.project.android.copsandcrooks.utils.Point;

/**
 * @author Bex
 *
 */
public class CrookTest {

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
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Crook#Crook(com.dat255.project.android.copsandcrooks.model.IMediator)}.
	 */
	@Test
	public final void testCrook() {
		try {
			new Crook(new RoadTile(new Point(0, 0), new Mediator()), null, 0);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		} 
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Crook#isWanted()}.
	 */
	@Test
	public final void testIsWanted() {
		Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		boolean testWanted = test.isWanted();
		assertFalse("Crook should start unwanted", testWanted);
		test.setWanted(true);
		testWanted = test.isWanted();
		assertTrue("Crook should now be wanted", testWanted);
		test.setWanted(false);
		testWanted = test.isWanted();
		assertFalse("Crook should now be unwanted", testWanted);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Crook#getWallet()}.
	 */
	@Test
	public final void testGetWallet() {
		Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		Wallet wallet = test.getWallet();
		assertTrue("Crook should start unwanted", wallet != null);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.Crook#collisionAfterMove(com.dat255.project.android.copsandcrooks.model.IPawn)}.
	 */
	@Test
	public final void testCollisionAfterMove() {
		Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		try {
			test.collisionAfterMove(new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0));
			fail("Should throw Assertion error");
		} catch (AssertionError e) {
			// expected
		} 
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.AbstractWalkingPawn#setIsInPoliceStation(boolean)}.
	 */
	@Test
	public final void testSetIsInPoliceStation() {
		Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		try {
			test.collisionAfterMove(new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0));
			fail("Should throw Assertion error");
		} catch (AssertionError e) {
			// expected
		} 
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.AbstractWalkingPawn#tilesMovedEachStep()}.
	 */
	@Test
	public final void testTilesMovedEachStep() {
		Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		int testMoves = test.tilesMovedEachStep();
		assertTrue("Crook should start unwanted", testMoves == 1);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.AbstractPawn#setCurrentTile(com.dat255.project.android.copsandcrooks.model.AbstractWalkableTile)}.
	 */
	@Test
	public final void testSetCurrentTile() {
		final Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		test.addObserver(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg) {
				assertSame("Wrong propety", IPawn.PROPERTY_CURRENT_TILE, arg.getPropertyName());
				assertSame("The new value does not match the current tile", arg.getNewValue(), test.currentTile);
			}
		});
		test.setCurrentTile(new RoadTile(new Point(), new Mediator()));
		
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.AbstractPawn#getCurrentTile()}.
	 */
	@Test
	public final void testGetCurrentTile() {
		final Crook test = new Crook(null, new Mediator(), 0);
		assertNull("Should be null since its not set, and is allowed to be null", test.getCurrentTile());
		AbstractWalkableTile walkable = new RoadTile(new Point(), new Mediator());
		test.setCurrentTile(walkable);
		assertSame("The set tile should be returned", walkable, test.getCurrentTile());
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.AbstractPawn#move(com.dat255.project.android.copsandcrooks.model.TilePath)}.
	 * and {@link com.dat255.project.android.copsandcrooks.model.AbstractPawn#update(float)}.
	 */ 
	@Test
	public final void testMoveAndUpdate() {
		final Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		AbstractWalkableTile walkable = new RoadTile(new Point(0, 0), new Mediator());
		test.setCurrentTile(walkable);
		TilePath path = new TilePath();
		final RoadTile end = new RoadTile(new Point(0, 2), new Mediator());
		path.addTileLast(end);
		path.addTileLast(new RoadTile(new Point(0, 1), new Mediator()));
		test.move(path);
		for (int i = 0; i < 10; i++)
			test.update(0.2f);
		assertSame("The set tile should be returned", test.getCurrentTile(), end);
	}
	
	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.AbstractPawn#getPawnRole()}.
	 */
	@Test
	public final void testGetPawnRole() {
		final Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		assertSame("The role of the crook should be crook", test.getPawnRole(), Role.Crook);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.AbstractPawn#getPawnType()}.
	 */
	@Test
	public final void testGetPawnType() {
		final Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		assertSame("The type of the crook should be crook", test.getPawnType(), PawnType.Crook);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.model.AbstractPawn#addObserver(java.beans.PropertyChangeListener)}.
	 */
	@Test
	public final void testAddObserver() {
		final Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg) {
				
			}
		};
		test.addObserver(listener);
		assertTrue("The propertychange was not added", test.pcs.getPropertyChangeListeners()[0] == listener);
	}
}
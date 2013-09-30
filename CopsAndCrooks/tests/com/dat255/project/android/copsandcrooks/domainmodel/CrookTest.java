/**
 * 
 */
package com.dat255.project.android.copsandcrooks.domainmodel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.dat255.project.android.copsandcrooks.utils.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;

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
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Crook#Crook(com.dat255.project.android.copsandcrooks.domainmodel.IMediator)}.
	 */
	@Test
	public final void testCrook() {
		try {
			Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), null);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		} 
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Crook#isWanted()}.
	 */
	@Test
	public final void testIsWanted() {
		Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator());
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
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Crook#getWallet()}.
	 */
	@Test
	public final void testGetWallet() {
		Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator());
		Wallet wallet = test.getWallet();
		assertTrue("Crook should start unwanted", wallet != null);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Crook#collisionAfterMove(com.dat255.project.android.copsandcrooks.domainmodel.IMovable)}.
	 */
	@Test
	public final void testCollisionAfterMove() {
		Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator());
		try {
			test.collisionAfterMove(new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator()));
			fail("Should throw Assertion error");
		} catch (AssertionError e) {
			// expected
		} 
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractWalkingPawn#setIsInPoliceStation(boolean)}.
	 */
	@Test
	public final void testSetIsInPoliceStation() {
		Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator());
		try {
			test.collisionAfterMove(new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator()));
			fail("Should throw Assertion error");
		} catch (AssertionError e) {
			// expected
		} 
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractWalkingPawn#tilesMovedEachStep()}.
	 */
	@Test
	public final void testTilesMovedEachStep() {
		Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator());
		int testMoves = test.tilesMovedEachStep();
		assertTrue("Crook should start unwanted", testMoves == 1);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#setCurrentTile(com.dat255.project.android.copsandcrooks.domainmodel.AbstractWalkableTile)}.
	 */
	@Test
	public final void testSetCurrentTile() {
		final Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator());
		test.addObserver(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg) {
				assertSame("Wrong propety", test.PROPERTY_CURRENT_TILE, arg.getPropertyName());
				assertSame("The new value does not match the current tile", arg.getNewValue(), test.currentTile);
			}
		});
		test.setCurrentTile(new RoadTile(new Point(), new Mediator()));
		
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#getCurrentTile()}.
	 */
	@Test
	public final void testGetCurrentTile() {
		final Crook test = new Crook(null, new Mediator());
		assertNull("Should be null since its not set, and is allowed to be null", test.getCurrentTile());
		AbstractWalkableTile walkable = new RoadTile(new Point(), new Mediator());
		test.setCurrentTile(walkable);
		assertSame("The set tile should be returned", walkable, test.getCurrentTile());
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#move(com.dat255.project.android.copsandcrooks.domainmodel.TilePath)}.
	 * and {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#update(float)}.
	 */ 
	@Test
	public final void testMoveAndUpdate() {
		final Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator());
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
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#getPawnRole()}.
	 */
	@Test
	public final void testGetPawnRole() {
		final Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator());
		assertSame("The role of the crook should be crook", test.getPawnRole(), Role.Crook);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#getPawnType()}.
	 */
	@Test
	public final void testGetPawnType() {
		final Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator());
		assertSame("The type of the crook should be crook", test.getPawnType(), PawnType.Crook);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#addObserver(java.beans.PropertyChangeListener)}.
	 */
	@Test
	public final void testAddObserver() {
		final Crook test = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator());
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg) {
				
			}
		};
		test.addObserver(listener);
		assertTrue("The propertychange was not added", test.pcs.getPropertyChangeListeners()[0] == listener);
	}
}

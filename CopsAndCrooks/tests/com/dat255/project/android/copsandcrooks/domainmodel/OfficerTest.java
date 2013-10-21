package com.dat255.project.android.copsandcrooks.domainmodel;

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

import com.dat255.project.android.copsandcrooks.utils.Point;

public class OfficerTest {

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
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.Officer#Officer(com.dat255.project.android.copsandOfficers.domainmodel.IMediator)}.
	 */
	@Test
	public final void testOfficer() {
		try {
			new Officer(new RoadTile(new Point(0, 0), new Mediator()), null, 10);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		} 
	}


	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.Officer#collisionAfterMove(com.dat255.project.android.IPawn.domainmodel.IMovable)}.
	 */
	@Test
	public final void testCollisionAfterMove() {
		Officer test = new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 10);
		try {
			test.collisionAfterMove(new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 10));
			fail("Should throw Assertion error");
		} catch (AssertionError e) {
			// expected
		} 
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractWalkingPawn#setIsInPoliceStation(boolean)}.
	 */
	@Test
	public final void testSetIsInPoliceStation() {
		Officer test = new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 10);
		try {
			test.collisionAfterMove(new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 10));
			fail("Should throw Assertion error");
		} catch (AssertionError e) {
			// expected
		} 
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractWalkingPawn#tilesMovedEachStep()}.
	 */
	@Test
	public final void testTilesMovedEachStep() {
		Officer test = new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 10);
		int testMoves = test.tilesMovedEachStep();
		assertTrue("Officer should move 1 steps", testMoves == 1);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#setCurrentTile(com.dat255.project.android.copsandcrooks.domainmodel.copsandOfficers.domainmodel.tiles.AbstractWalkableTile)}.
	 */
	@Test
	public final void testSetCurrentTile() {
		final Officer test = new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 10);
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
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#getCurrentTile()}.
	 */
	@Test
	public final void testGetCurrentTile() {
		final Officer test = new Officer(null, new Mediator(), 10);
		assertNull("Should be null since its not set, and is allowed to be null", test.getCurrentTile());
		AbstractWalkableTile walkable = new RoadTile(new Point(), new Mediator());
		test.setCurrentTile(walkable);
		assertSame("The set tile should be returned", walkable, test.getCurrentTile());
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#move(com.dat255.project.android.copsandOfficers.domainmodel.TilePath)}.
	 * and {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#update(float)}.
	 */ 
	@Test
	public final void testMoveAndUpdate() {
		final Officer test = new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 10);
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
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#getPawnRole()}.
	 */
	@Test
	public final void testGetPawnRole() {
		final Officer test = new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 10);
		assertSame("The role of the Officer should be Officer", test.getPawnRole(), Role.Cop);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#getPawnType()}.
	 */
	@Test
	public final void testGetPawnType() {
		final Officer test = new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 10);
		assertSame("The type of the Officer should be Officer", test.getPawnType(), PawnType.Officer);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#addObserver(java.beans.PropertyChangeListener)}.
	 */
	@Test
	public final void testAddObserver() {
		final Officer test = new Officer(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 10);
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg) {
				
			}
		};
		test.addObserver(listener);
		assertTrue("The propertychange was not added", test.pcs.getPropertyChangeListeners()[0] == listener);
	}

}

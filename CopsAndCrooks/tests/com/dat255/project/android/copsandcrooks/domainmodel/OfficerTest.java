package com.dat255.project.android.copsandcrooks.domainmodel;

import static org.junit.Assert.*;

import com.dat255.project.android.copsandcrooks.utils.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.RoadTile;

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
			Officer test = new Officer(null);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		} 
	}


	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.Officer#collisionAfterMove(com.dat255.project.android.copsandOfficers.domainmodel.IMovable)}.
	 */
	@Test
	public final void testCollisionAfterMove() {
		Officer test = new Officer(new Mediator());
		try {
			test.collisionAfterMove(new Officer(new Mediator()));
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
		Officer test = new Officer(new Mediator());
		try {
			test.collisionAfterMove(new Officer(new Mediator()));
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
		Officer test = new Officer(new Mediator());
		int testMoves = test.tilesMovedEachStep();
		assertTrue("Officer should move 1 steps", testMoves == 1);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#setCurrentTile(com.dat255.project.android.copsandOfficers.domainmodel.tiles.IWalkableTile)}.
	 */
	@Test
	public final void testSetCurrentTile() {
		final Officer test = new Officer(new Mediator());
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
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#getCurrentTile()}.
	 */
	@Test
	public final void testGetCurrentTile() {
		final Officer test = new Officer(new Mediator());
		assertNull("Should be null since its not set, and is allowed to be null", test.getCurrentTile());
		IWalkableTile walkable = new RoadTile(new Point(), new Mediator());
		test.setCurrentTile(walkable);
		assertSame("The set tile should be returned", walkable, test.getCurrentTile());
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#move(com.dat255.project.android.copsandOfficers.domainmodel.TilePath)}.
	 * and {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#update(float)}.
	 */ 
	@Test
	public final void testMoveAndUpdate() {
		final Officer test = new Officer(new Mediator());
		IWalkableTile walkable = new RoadTile(new Point(0, 0), new Mediator());
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
		final Officer test = new Officer(new Mediator());
		assertSame("The role of the Officer should be Officer", test.getPawnRole(), Role.Officer);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#getPawnType()}.
	 */
	@Test
	public final void testGetPawnType() {
		final Officer test = new Officer(new Mediator());
		assertSame("The type of the Officer should be Officer", test.getPawnType(), PawnType.Officer);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandOfficers.domainmodel.AbstractPawn#addObserver(java.beans.PropertyChangeListener)}.
	 */
	@Test
	public final void testAddObserver() {
		final Officer test = new Officer(new Mediator());
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg) {
				
			}
		};
		test.addObserver(listener);
		assertTrue("The propertychange was not added", test.pcs.getPropertyChangeListeners()[0] == listener);
	}

}

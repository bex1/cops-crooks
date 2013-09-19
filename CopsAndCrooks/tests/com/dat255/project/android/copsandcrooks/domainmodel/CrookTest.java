/**
 * 
 */
package com.dat255.project.android.copsandcrooks.domainmodel;

import static org.junit.Assert.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
			Crook test = new Crook(null);
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
		Crook test = new Crook(new Mediator());
		boolean testWanted = test.isWanted();
		assertFalse("Crook should start unwanted", testWanted);
		test.getWallet().incrementCash(1);
		testWanted = test.isWanted();
		assertTrue("Crook should now be wanted since it has cash", testWanted);
		test.getWallet().setCash(0);
		testWanted = test.isWanted();
		assertFalse("Crook should now be unwanted since cash is 0", testWanted);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Crook#getWallet()}.
	 */
	@Test
	public final void testGetWallet() {
		Crook test = new Crook(new Mediator());
		Wallet wallet = test.getWallet();
		assertTrue("Crook should start unwanted", wallet != null);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.Crook#collisionAfterMove(com.dat255.project.android.copsandcrooks.domainmodel.IMovable)}.
	 */
	@Test
	public final void testCollisionAfterMove() {
		Crook test = new Crook(new Mediator());
		try {
			test.collisionAfterMove(new Officer(new Mediator()));
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
		Crook test = new Crook(new Mediator());
		try {
			test.collisionAfterMove(new Officer(new Mediator()));
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
		Crook test = new Crook(new Mediator());
		int testMoves = test.tilesMovedEachStep();
		assertTrue("Crook should start unwanted", testMoves == 1);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#setCurrentTile(com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile)}.
	 */
	@Test
	public final void testSetCurrentTile() {
		final Crook test = new Crook(new Mediator());
		test.addObserver(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg) {
				assertSame("Wrong propety", test.PROPERTY_CURRENT_TILE, arg.getPropertyName());
			}
		});
		int testMoves = test.tilesMovedEachStep();
		assertTrue("Crook should start unwanted", testMoves == 1);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#getCurrentTile()}.
	 */
	@Test
	public final void testGetCurrentTile() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#move(com.dat255.project.android.copsandcrooks.domainmodel.TilePath)}.
	 */
	@Test
	public final void testMove() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#update(float)}.
	 */
	@Test
	public final void testUpdate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#getPawnRole()}.
	 */
	@Test
	public final void testGetPawnRole() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#getPawnType()}.
	 */
	@Test
	public final void testGetPawnType() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#addObserver(java.beans.PropertyChangeListener)}.
	 */
	@Test
	public final void testAddObserver() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn#removeObserver(java.beans.PropertyChangeListener)}.
	 */
	@Test
	public final void testRemoveObserver() {
		fail("Not yet implemented"); // TODO
	}

}

package com.dat255.project.android.copsandcrooks.domainmodel;



import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;



public class CopCarTest {

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
	 * Test method for {@link com.dat255.project.android.copsandCopCars.domainmodel.CopCar#CopCar(com.dat255.project.android.copsandCopCars.domainmodel.IMediator)}.
	 */
	@Test
	public final void testCopCar() {
		try {
			new CopCar(new RoadTile(new Point(0, 0), new Mediator()), null, 20);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		} 
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandCopCars.domainmodel.CopCar#collisionAfterMove(com.dat255.project.android.IPawn.domainmodel.IMovable)}.
	 */
	@Test
	public final void testCollisionAfterMove() {
		Mediator mediator = new Mediator();
		
		CopCar test = new CopCar(new RoadTile(new Point(0, 0), mediator), mediator, 20);
		Crook crook = new Crook(new RoadTile(new Point(0, 0), mediator), mediator, 10);
		List<AbstractPawn> pawnList = new ArrayList<AbstractPawn>();
		pawnList.add(test);
		Player player = new Player(null, pawnList, Role.Cop, mediator, null, null);
		crook.setWanted(true);
		crook.getWallet().setCash(20000);
		
		test.collisionAfterMove(crook);
		
		assertTrue(crook.getTimesArrested() == 1 && crook.getCurrentTile() instanceof PoliceStationTile 
				&& crook.getWallet().getCash() == 0 && player.getWallet().getCash() == 5000);
		
		try {
			test.collisionAfterMove(new Officer(new RoadTile(new Point(0, 0), mediator), mediator, 10));
			fail("Should throw Assertion error");
		} catch (AssertionError e) {
			// expected
		} 
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandCopCars.domainmodel.AbstractWalkingPawn#setIsInPoliceStation(boolean)}.
	 */
	@Test
	public final void testSetIsInPoliceStation() {
		CopCar test = new CopCar(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 20);
		try {
			test.collisionAfterMove(new Officer(new RoadTile(new Point(0, 0), new Mediator()),new Mediator(), 10));
			fail("Should throw Assertion error");
		} catch (AssertionError e) {
			// expected
		} 
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandCopCars.domainmodel.AbstractWalkingPawn#tilesMovedEachStep()}.
	 */
	@Test
	public final void testTilesMovedEachStep() {
		CopCar test = new CopCar(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 20);
		int testMoves = test.tilesMovedEachStep();
		assertTrue("CopCar should move 2 steps", testMoves == 2);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandCopCars.domainmodel.AbstractPawn#setCurrentTile(com.dat255.project.android.copsandcrooks.domainmodel.copsandCopCars.domainmodel.tiles.AbstractWalkableTile)}.
	 */
	@Test
	public final void testSetCurrentTile() {
		final CopCar test = new CopCar(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 20);
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
	 * Test method for {@link com.dat255.project.android.copsandCopCars.domainmodel.AbstractPawn#getCurrentTile()}.
	 */
	@Test
	public final void testGetCurrentTile() {
		final CopCar test = new CopCar(null, new Mediator(), 20);
		assertNull("Should be null since its not set, and is allowed to be null", test.getCurrentTile());
		AbstractWalkableTile walkable = new RoadTile(new Point(), new Mediator());
		test.setCurrentTile(walkable);
		assertSame("The set tile should be returned", walkable, test.getCurrentTile());
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandCopCars.domainmodel.AbstractPawn#move(com.dat255.project.android.copsandCopCars.domainmodel.TilePath)}.
	 * and {@link com.dat255.project.android.copsandCopCars.domainmodel.AbstractPawn#update(float)}.
	 */ 
	@Test
	public final void testMoveAndUpdate() {
		final CopCar test = new CopCar(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 20);
		AbstractWalkableTile walkable = new RoadTile(new Point(0, 0), new Mediator());
		test.setCurrentTile(walkable);
		TilePath path = new TilePath();
		final RoadTile end = new RoadTile(new Point(0, 2), new Mediator());
		path.addTileLast(end);
		path.addTileLast(new RoadTile(new Point(0, 1), new Mediator()));
		test.move(path);
		for (int i = 0; i < 10; i++)
			test.update(Values.PAWN_MOVE_DELAY);
		assertSame("The set tile should be returned", test.getCurrentTile(), end);
	}
	
	/**
	 * Test method for {@link com.dat255.project.android.copsandCopCars.domainmodel.AbstractPawn#getPawnRole()}.
	 */
	@Test
	public final void testGetPawnRole() {
		final CopCar test = new CopCar(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 20);
		assertSame("The role of the CopCar should be CopCar", test.getPawnRole(), Role.Cop);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandCopCars.domainmodel.AbstractPawn#getPawnType()}.
	 */
	@Test
	public final void testGetPawnType() {
		final CopCar test = new CopCar(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 20);
		assertSame("The type of the CopCar should be CopCar", test.getPawnType(), PawnType.Car);
	}

	/**
	 * Test method for {@link com.dat255.project.android.copsandCopCars.domainmodel.AbstractPawn#addObserver(java.beans.PropertyChangeListener)}.
	 */
	@Test
	public final void testAddObserver() {
		final CopCar test = new CopCar(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 20);
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg) {
				
			}
		};
		test.addObserver(listener);
		assertTrue("The propertychange was not added", test.pcs.getPropertyChangeListeners()[0] == listener);
	}
}

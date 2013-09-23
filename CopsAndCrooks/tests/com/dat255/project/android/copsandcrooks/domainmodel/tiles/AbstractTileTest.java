package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.domainmodel.Mediator;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.utils.Point;

public class AbstractTileTest {
	
	//Mockup class
	public class AbstractTileMock extends AbstractTile{

		public AbstractTileMock(Point position) {
			super(position, new Mediator());
		}
		
	}

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
	public void testIsOccupied(){
		AbstractTile abstractTile = new AbstractTileMock(new Point(0,0));
		if(abstractTile.isOccupied()){
			fail();
		}
		abstractTile.setOccupiedBy(PawnType.Car);
		assertTrue(abstractTile.isOccupied());
	}

	@Test
	public void testSetOccupiedBy() {
		AbstractTile abstractTile = new AbstractTileMock(new Point(0,0));
		abstractTile.setOccupiedBy(PawnType.Car);
		assertFalse(abstractTile.getOccupiedBy().equals(PawnType.Crook));
		assertTrue(abstractTile.getOccupiedBy().equals(PawnType.Car));
	}

	@Test
	public void testSetNotOccupied(){
		AbstractTile abstractTile = new AbstractTileMock(new Point(0,0));
		abstractTile.setOccupiedBy(PawnType.Car);
		abstractTile.setNotOccupied();
		assertTrue(!abstractTile.isOccupied());
	}

	@Test
	public void testGetOccupiedBy(){
		AbstractTile abstractTile = new AbstractTileMock(new Point(0,0));
		abstractTile.setOccupiedBy(PawnType.Crook);
		assertTrue(abstractTile.getOccupiedBy().equals(PawnType.Crook));
	}
	
	@Test
	public void testGetPosition(){
		AbstractTile abstractTile = new AbstractTileMock(new Point(5,3));
		Point p = new Point(abstractTile.getPosition().x, abstractTile.getPosition().y);
		assertTrue(p.x == 5 && p.y == 3);
	}
	
	@Test
	public void testSetPosition(){
		AbstractTile abstractTile = new AbstractTileMock(new Point(0,0));
		Point p = new Point(3, 5);
		abstractTile.setPosition(p);
		assertFalse(abstractTile.getPosition() == null);
		assertTrue(p.x == 3 && p.y == 5);
	}

	@Test
	public void testGetAllowedPawnTypes() {
		AbstractTile abstractTile = new AbstractTileMock(new Point(0,0));
		
		abstractTile.pawnTypes.add(PawnType.Car);
		abstractTile.pawnTypes.add(PawnType.Crook);
		
		assertTrue(abstractTile.getAllowedPawnTypes().get(0).
				equals(PawnType.Car) && abstractTile.getAllowedPawnTypes().
				get(1).equals(PawnType.Crook));
	}

}

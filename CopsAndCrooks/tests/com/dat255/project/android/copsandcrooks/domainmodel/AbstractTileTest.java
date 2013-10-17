package com.dat255.project.android.copsandcrooks.domainmodel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.utils.Point;

public class AbstractTileTest {
	
	//Mockup class
	public class AbstractTileMock extends AbstractWalkableTile{

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
		AbstractWalkableTile abstractTile = new AbstractTileMock(new Point(0,0));
		if(abstractTile.isOccupied()){
			fail();
		}
		abstractTile.setOccupiedBy(PawnType.Car);
		assertTrue(abstractTile.isOccupied());
	}

	@Test
	public void testSetOccupiedBy() {
		AbstractWalkableTile abstractTile = new AbstractTileMock(new Point(0,0));
		abstractTile.setOccupiedBy(PawnType.Car);
		assertFalse(abstractTile.getOccupiedBy().equals(PawnType.Crook));
		assertTrue(abstractTile.getOccupiedBy().equals(PawnType.Car));
	}

	@Test
	public void testSetNotOccupied(){
		AbstractWalkableTile abstractTile = new AbstractTileMock(new Point(0,0));
		abstractTile.setOccupiedBy(PawnType.Car);
		abstractTile.setNotOccupied();
		assertTrue(!abstractTile.isOccupied());
	}

	@Test
	public void testGetOccupiedBy(){
		AbstractWalkableTile abstractTile = new AbstractTileMock(new Point(0,0));
		abstractTile.setOccupiedBy(PawnType.Crook);
		assertTrue(abstractTile.getOccupiedBy().equals(PawnType.Crook));
	}
	
	@Test
	public void testGetPosition(){
		AbstractWalkableTile abstractTile = new AbstractTileMock(new Point(5,3));
		Point p = new Point(abstractTile.getPosition().x, abstractTile.getPosition().y);
		assertTrue(p.x == 5 && p.y == 3);
	}

	@Test
	public void testGetAllowedPawnTypes() {
		AbstractWalkableTile abstractTile = new AbstractTileMock(new Point(0,0));
		
		abstractTile.pawnTypes.add(PawnType.Car);
		abstractTile.pawnTypes.add(PawnType.Crook);
		
		List<PawnType> pawnTypes = new ArrayList<PawnType>(abstractTile.getAllowedPawnTypes());
		
		assertTrue(pawnTypes.get(0).equals(PawnType.Car) && 
				pawnTypes.get(1).equals(PawnType.Crook));
	}

}

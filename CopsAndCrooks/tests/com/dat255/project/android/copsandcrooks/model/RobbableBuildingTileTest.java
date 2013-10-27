package com.dat255.project.android.copsandcrooks.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.model.Crook;
import com.dat255.project.android.copsandcrooks.model.Mediator;
import com.dat255.project.android.copsandcrooks.model.RoadTile;
import com.dat255.project.android.copsandcrooks.model.RobbableBuildingTile;
import com.dat255.project.android.copsandcrooks.utils.Point;

public class RobbableBuildingTileTest {

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
	public void testRobBuilding(){
		Crook crook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		int cash = crook.getWallet().getCash();
		RobbableBuildingTile robbableBuilding = 
				new RobbableBuildingTile(new Point(), new Mediator(), 750);
		
		robbableBuilding.getRobbedBy(crook);
		
		//The crooks cash should now be 750
		assertTrue(crook.getWallet().getCash() == cash+750);
	}
	
	@Test
	public void testGetValue(){
		RobbableBuildingTile robbableBuilding = 
				new RobbableBuildingTile(new Point(), new Mediator(), 350);
		assertTrue(robbableBuilding.getValue() == 350);
	}
	
	@Test
	public void testSetValue(){
		RobbableBuildingTile robbableBuilding = 
				new RobbableBuildingTile(new Point(), new Mediator(), 350);
		robbableBuilding.setValue(650);
		assertTrue(robbableBuilding.getValue() == 650);
	}
}

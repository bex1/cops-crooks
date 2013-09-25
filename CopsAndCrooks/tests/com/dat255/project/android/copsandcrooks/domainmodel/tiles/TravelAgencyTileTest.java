package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.Mediator;

public class TravelAgencyTileTest {

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
		TravelAgencyTile travelAgency = new TravelAgencyTile(null, new Mediator());
		Crook crook = new Crook(new Mediator());
		
		travelAgency.robBuilding(crook);
		
		if(crook.getWallet().getCash() != 0){
			fail();
		}
		
		travelAgency.setValue(2000);
		travelAgency.robBuilding(crook);
		assertTrue(crook.getWallet().getCash() == 2000);
	}
	
	@Test
	public void testAddCash(){
		TravelAgencyTile travelAgency = new TravelAgencyTile(null, new Mediator());
		//Default value should be zero
		travelAgency.addCash(5000);
		
		assertTrue(travelAgency.getValue() == 5000);
	}

}

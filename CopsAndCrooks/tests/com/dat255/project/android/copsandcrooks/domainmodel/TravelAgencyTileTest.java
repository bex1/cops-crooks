package com.dat255.project.android.copsandcrooks.domainmodel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.Mediator;
import com.dat255.project.android.copsandcrooks.domainmodel.TravelAgencyTile;
import com.dat255.project.android.copsandcrooks.utils.Point;

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
		TravelAgencyTile travelAgency = new TravelAgencyTile(new Point(), new Mediator());
		// Reset to avoid failure if other tests have been run before,
		// since TravelAgencyTile is singleton
		travelAgency.setValue(0);
		Crook crook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		
		travelAgency.getRobbedBy(crook);
		
		if(crook.getWallet().getCash() != 0){
			fail();
		}
		
		travelAgency.setValue(2000);
		travelAgency.getRobbedBy(crook);
		assertTrue(crook.getWallet().getCash() == 2000);
	}
	
	@Test
	public void testAddCash(){
		TravelAgencyTile travelAgency = new TravelAgencyTile(new Point(), new Mediator());
		//Default value should be zero
		travelAgency.addCash(5000);
		
		assertTrue(travelAgency.getValue() == 5000);
	}

}

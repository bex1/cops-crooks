package com.dat255.project.android.copsandcrooks.model;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.utils.Point;

public class GetAwayTileTest {

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
	public void testInteract(){
		Crook crook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		GetAwayTile getAway = new GetAwayTile(new Point(), new Mediator());
		
		crook.getWallet().setCash(1000);
		//Ticket cost is 5000
		getAway.interact(crook);
		if(crook.isAttemptingGetAway()){
			fail();
		}
		
		crook.getWallet().setCash(6000);
		getAway.interact(crook);
		if(!crook.isAttemptingGetAway()){
			fail();
		}
	}

}

package com.dat255.project.android.copsandcrooks.domainmodel;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.HideoutTile;
import com.dat255.project.android.copsandcrooks.domainmodel.Mediator;
import com.dat255.project.android.copsandcrooks.utils.Point;

public class HideoutTest {

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
	public void testHasStoredCash(){
		Crook testCrook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		List<Crook> crookList = new LinkedList<Crook>();
		crookList.add(testCrook);
		
		HideoutTile testHideout = new HideoutTile(new Point(0,0), crookList, new Mediator());
		testCrook.getWallet().setCash(6);
		testHideout.depositCash(testCrook);
		assertTrue(testHideout.hasStoredCash(testCrook));
	}

	@Test
	public void testGetStoredCashAmount(){
		Crook testCrook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		List<Crook> crookList = new LinkedList<Crook>();
		crookList.add(testCrook);
		
		HideoutTile testHideout = new HideoutTile(new Point(0,0), crookList, new Mediator());
		testCrook.getWallet().setCash(7);
		testHideout.depositCash(testCrook);
		assertTrue(testHideout.getStoredCashAmount(testCrook) == 7);
	}
	
	@Test
	public void testDepositCash(){
		Crook testCrook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		List<Crook> crookList = new LinkedList<Crook>();
		crookList.add(testCrook);
		
		HideoutTile testHideout = new HideoutTile(new Point(0,0), crookList, new Mediator());
		testCrook.getWallet().setCash(9);
		testHideout.depositCash(testCrook);
		assertTrue(testHideout.getStoredCashAmount(testCrook) == 9);
	}
	
	@Test
	public void testWithdrawCash(){
		Crook testCrook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), new Mediator(), 0);
		List<Crook> crookList = new LinkedList<Crook>();
		crookList.add(testCrook);
		
		HideoutTile testHideout = new HideoutTile(new Point(0,0), crookList, new Mediator());
		testCrook.getWallet().setCash(7);
		testHideout.depositCash(testCrook);
		testHideout.withdrawCash(testCrook);
		
		assertTrue(testCrook.getWallet().getCash() == 7);
	}
}

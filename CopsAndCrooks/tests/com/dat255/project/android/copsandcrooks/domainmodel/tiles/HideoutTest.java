package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dat255.project.android.copsandcrooks.domainmodel.*;

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
		Crook testCrook = new Crook(new Mediator());
		
		Hideout testHideout = new Hideout(new Point(0,0));
		testCrook.getWallet().setCash(10);
		int depositAmount = 7;
		testHideout.depositCash(testCrook, depositAmount);
		assertTrue(testHideout.hasStoredCash(testCrook));
	}

	@Test
	public void testGetStoredCashAmount(){
		Crook testCrook = new Crook(new Mediator());
		
		Hideout testHideout = new Hideout(new Point(0,0));
		testCrook.getWallet().setCash(10);
		int depositAmount = 7;
		testHideout.depositCash(testCrook, depositAmount);
		assertTrue(testHideout.getStoredCashAmount(testCrook) == 7);
	}
	
	@Test
	public void testDepositCash(){
		Crook testCrook = new Crook(new Mediator());
		
		Hideout testHideout = new Hideout(new Point(0,0));
		testCrook.getWallet().setCash(10);
		int depositAmount = 13;
		testHideout.depositCash(testCrook, depositAmount);
		assertTrue(testHideout.getStoredCashAmount(testCrook) == 10);
	}
	
	@Test
	public void testWithdrawCash(){
		Crook testCrook = new Crook(new Mediator());
		
		Hideout testHideout = new Hideout(new Point(0,0));
		testCrook.getWallet().setCash(10);
		int depositAmount = 7;
		testHideout.depositCash(testCrook, depositAmount);
		int withdrawAmount = 3;
		testHideout.withdrawCash(testCrook, withdrawAmount);
		
		assertTrue(testHideout.getStoredCashAmount(testCrook) == 
				(depositAmount - withdrawAmount));
	}
}

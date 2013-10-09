package com.dat255.project.android.copsandcrooks.domainmodel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testDice() {
		try{
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public final void testRoll() {
		/*Dice dice = new Dice(new Mediator());
		for (int i = 0; i < 1000; i++) {
			int roll = dice.roll();
			if (roll < 1 || roll > 6) {
				fail();
			}
		}*/
	}
}

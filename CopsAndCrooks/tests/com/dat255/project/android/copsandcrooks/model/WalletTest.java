package com.dat255.project.android.copsandcrooks.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dat255.project.android.copsandcrooks.model.Wallet;

public class WalletTest {

	@Test
	public void SetCashTest() {
		Wallet wallet = new Wallet();
		if(wallet.getCash() < 0){
			fail("Cash should never be less than 0");
		}
		
		assertTrue(wallet.getCash() == 0);
		
		wallet.setCash(4000);
		
		assertTrue(wallet.getCash() == 4000);
		
	}
	@Test
	public void IncrementCashTest(){
		Wallet wallet = new Wallet();
		if(wallet.getCash() < 0){
			fail("Cash should never be less than 0");
		}
		
		wallet.setCash(4000);
		
		wallet.incrementCash(2000);
		
		assertTrue(wallet.getCash() == 6000);
		
	}
	@Test
	public void DecrementCashTest(){
		Wallet wallet = new Wallet();
		if(wallet.getCash() < 0){
			fail("Cash should never be less than 0");
		}
		
		wallet.setCash(4000);
		
		wallet.decrementCash(2000);
		
		assertTrue(wallet.getCash() == 2000);
	}

}

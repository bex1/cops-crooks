package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import com.dat255.project.android.copsandcrooks.utils.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dat255.project.android.copsandcrooks.domainmodel.*;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;

/**
 * A class representing a hideout.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class HideoutTile extends AbstractTile implements IInteractiveTile {
	
	private Map<Crook, Integer> storedCash;

	/**
	 * Create a new hideout.
	 * @param position the hideout's position
	 * @param players a list of all players
	 */
	public HideoutTile(Point position, List<Crook> crook) {
		super(position);
		
		storedCash = new HashMap<Crook, Integer>();

		pawnTypes.add(PawnType.Crook);
	}

	@Override
	public void interact(IMovable target) {
		//TODO would you like to deposit/withdraw cash?
	}
	
	/**
	 * Deposit an amount of a crook's cash in the hideout.
	 * @param crook the crook
	 * @param amount the amount to store
	 */
	public void depositCash(Crook crook, int amount){
		//Adds the crook to the list of crooks that have stored cash
		if(storedCash.containsKey(crook)){
			storedCash.put(crook, 0);
		}
		
		if(amount <= crook.getWallet().getCash()){
			storedCash.put(crook, amount + getStoredCashAmount(crook));
			crook.getWallet().decrementCash(amount);
		}else{
			storedCash.put(crook, crook.getWallet().getCash() + getStoredCashAmount(crook));
			crook.getWallet().setCash(0);
		}
	}
	
	/**
	 * Withdraw an amount of the crook's stored cash.
	 * @param crook the crook
	 * @param amount amount to retrieve
	 */
	public void withdrawCash(Crook crook, int amount){
		int cash;
		
		//Checks if the crook has any cash in the hideout.
		if(hasStoredCash(crook)){
			cash = getStoredCashAmount(crook);
		}else{
			throw new NullPointerException("The crook can't be null!");
		}
		
		//Returns the stored cash if amount exceeds or equals the stored cash.
		if(cash <= amount){
			storedCash.put(crook, 0);
			crook.getWallet().incrementCash(cash);
		}else{
			storedCash.put(crook, cash-amount);
			crook.getWallet().incrementCash(amount);
		}
	}
	
	/**
	 * Get the stored cash in this hideout for a specific crook.
	 * @param crook the crook
	 * @return the amount of cash stored by a crook
	 */
	public int getStoredCashAmount(Crook crook){
		if(crook==null){
			return 0;
		}
		
		int cash = 0;
		if(hasStoredCash(crook)){
			cash = storedCash.get(crook);
		}
		return cash;
	}
	
	/**
	 * Check if a crook has any cash in this hideout.
	 * @param crook the crook
	 */
	public boolean hasStoredCash(Crook crook){
		if(!(crook==null) && storedCash.containsKey(crook) && storedCash.get(crook) > 0){
			return true;
		}
		return false;
	}
	
}

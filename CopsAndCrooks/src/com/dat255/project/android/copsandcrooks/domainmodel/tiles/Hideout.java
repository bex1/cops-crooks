package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import java.awt.Point;
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
public class Hideout extends AbstractTile implements IInteractiveTile {
	
	private Map<Crook, Integer> storedCash;

	/**
	 * Create a new hideout.
	 * @param position the hideout's position
	 * @param players a list of all players
	 */
	public Hideout(Point position, List<Player> players) {
		super(position);
		
		storedCash = new HashMap<Crook, Integer>();
		
		//TODO GAMEMODEL.getPlayers().getCrooks() store them here
		
		//Adds all crooks to the hideouts storedCash list.
		/*for(Player p: players){
			if(p.getPlayerRole() == Role.Crook){
				IMovable[] pawns = (IMovable[]) p.getPawns().toArray();
				if(pawns[0] instanceof Crook){
					storedCash.put((Crook) pawns[0], 0);
				}
			}
		}*/

		pawnTypes.add(PawnType.Crook);
	}

	@Override
	public void interact(IMovable target) {
		setOccupied(true);
		//setInHideout(true);
	}
	
	/**
	 * Deposit an amount of a crook's cash in the hideout.
	 * @param crook the crook
	 * @param amount the amount to store
	 */
	public void depositCash(Crook crook, int amount){
		if(amount <= crook.getWallet().getCash()){
			if(storedCash.containsKey(crook)){
				storedCash.put(crook, amount + storedCash.get(crook));
				crook.getWallet().decrementCash(amount);
			}else{
				storedCash.put(crook, amount);
				crook.getWallet().decrementCash(amount);
			}
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
		if(storedCash.containsKey(crook) || !(crook==null)){
			cash = storedCash.get(crook);
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
	
}

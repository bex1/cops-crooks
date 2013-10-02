package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.utils.Point;

/**
 * A class representing a hideout.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class HideoutTile extends AbstractWalkableTile implements IInteractiveTile {
	
	private Map<Crook, Integer> storedCash;
	
	public static final String PROPERTY_HIDEOUT_INTERACT = "Hideout_Interact";
	public static final String PROPERTY_HIDEOUT_MONEY = "Hideout_Money";

	/**
	 * Create a new hideout.
	 * @param position the hideout's position
	 * @param players a list of all players
	 */
	public HideoutTile(Point position, IMediator mediator) {
		super(position, mediator);
		
		storedCash = new HashMap<Crook, Integer>();

		pawnTypes.add(PawnType.Crook);
	}
	
	public void addCrooks(Collection<Crook> crooks) {
		for (Crook crook : crooks) {
			storedCash.put(crook, crook.getWallet().getCash());
		}
	}

	@Override
	public void interact(IMovable target) {
		//this needs to be listened to by something
		if (target instanceof Crook) {
			pcs.firePropertyChange(PROPERTY_HIDEOUT_INTERACT, null, target);
		}
	}
	
	/**
	 * Deposit an amount of a crook's cash in the hideout.
	 * @param crook the crook
	 * @param amount the amount to store
	 */
	public void depositCash(Crook crook){
		//Adds the crook to the list of crooks that have stored cash
		if(storedCash.containsKey(crook)){
			storedCash.put(crook, 0);
		}
		storedCash.put(crook, crook.getWallet().getCash() + getStoredCashAmount(crook));
		crook.getWallet().setCash(0);
		pcs.firePropertyChange(PROPERTY_HIDEOUT_MONEY, null, crook);
		
		if(crook.getWallet().getCash() == 0){
			crook.setWanted(false);
		}
		mediator.playerTurnDone(2f);
	}
	
	/**
	 * Withdraw an amount of the crook's stored cash.
	 * @param crook the crook
	 * @param amount amount to retrieve
	 */
	public void withdrawCash(Crook crook){
		int cash;
		
		//Checks if the crook has any cash in the hideout.
		if(hasStoredCash(crook)){
			cash = getStoredCashAmount(crook);
		}else{
			throw new NullPointerException("The crook can't be null!");
		}
		
		storedCash.put(crook, 0);
		crook.getWallet().incrementCash(cash);
		pcs.firePropertyChange(PROPERTY_HIDEOUT_MONEY, null, crook);
		
		crook.setWanted(crook.getWallet().getCash() > 0);
		
		mediator.playerTurnDone(2f);
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
		return crook!=null && storedCash.containsKey(crook) && storedCash.get(crook) > 0;
	}
	
	/**
	 * Cancels the crooks interaction with the hideout.
	 * @param crook the crook
	 */
	public void cancelInteraction(Crook crook){
		mediator.playerTurnDone(2f);
	}
	
	@Override
	public boolean isOccupied(){
		return false;
	}
	
	@Override
	public void setOccupiedBy(PawnType pawnType){
	}
}

package com.dat255.project.android.copsandcrooks.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.dat255.project.android.copsandcrooks.model.GameModel.GameState;
import com.dat255.project.android.copsandcrooks.model.Turn.HideoutChoice;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * A class representing a hideout.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class HideoutTile extends AbstractWalkableTile implements IInteractiveTile {
	
	private static final long serialVersionUID = 8711266762352368L;
	
	private Map<Crook, Integer> storedCash;
	
	public static final String PROPERTY_HIDEOUT_INTERACT = "Hideout_Interact";
	public static final String PROPERTY_HIDEOUT_MONEY = "Hideout_Money";

	/**
	 * Create a new hideout.
	 * @param position the hideout's position
	 */
	HideoutTile(Point position, IMediator mediator) {
		super(position, mediator);

		this.storedCash = new HashMap<Crook, Integer>();

		pawnTypes.add(PawnType.Crook);
	}
	
	void addCrooks(Collection<Crook> crooks) {
		for (Crook crook : crooks) {
			storedCash.put(crook, crook.getWallet().getCash());
		}
	}

	@Override
	public void interact(IPawn target) {
		//this needs to be listened to by something
		pcs.firePropertyChange(PROPERTY_HIDEOUT_INTERACT, null, target);
		if(target instanceof Crook){
			Crook crook = (Crook)target;
			if(crook.getWallet().getCash() == 0){
				crook.setWanted(false);
			}
		}
	}

	/**
	 * DEPOSIT an amount of a crook's cash in the hideout.
	 * @param crook the crook
	 */
	public void depositCash(Crook crook){
		//Adds the crook to the list of crooks that have stored cash
		if (mediator.checkState() == GameState.PLAYING) {
			mediator.getCurrentTurn().setHideoutChoice(HideoutChoice.DEPOSIT);
		}
		storedCash.put(crook, crook.getWallet().getCash() + getStoredCashAmount(crook));
		crook.getWallet().setCash(0);
		crook.setWanted(false);
		pcs.firePropertyChange(PROPERTY_HIDEOUT_MONEY, null, crook);
		
		mediator.playerTurnDone(Values.DELAY_CHANGE_PLAYER_STANDARD);
	}

	/**
	 * WITHDRAW an amount of the crook's stored cash.
	 * @param crook the crook
	 */
	public void withdrawCash(Crook crook){
		int cash;
		if (mediator.checkState() == GameState.PLAYING) {
			mediator.getCurrentTurn().setHideoutChoice(HideoutChoice.WITHDRAW);
		}
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
		
		mediator.playerTurnDone(Values.DELAY_CHANGE_PLAYER_STANDARD);
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
	 */
	public void cancelInteraction(){
		if (mediator.checkState() == GameState.PLAYING) {
			mediator.getCurrentTurn().setHideoutChoice(HideoutChoice.CANCEL);
		}
		mediator.playerTurnDone(Values.DELAY_CHANGE_PLAYER_STANDARD);
	}
	
	@Override
	public boolean isOccupied(){
		return false;
	}
	
	@Override
	public void setOccupiedBy(PawnType pawnType){
	}
	

	public Map<Crook, Integer> getStoredCash(){
		return storedCash;
	}
}

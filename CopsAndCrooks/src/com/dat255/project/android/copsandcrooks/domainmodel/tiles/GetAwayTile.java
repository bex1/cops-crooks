package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import com.dat255.project.android.copsandcrooks.utils.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMediator;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;

/**
 * This class represents a get away tile.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class GetAwayTile extends AbstractTile implements IInteractiveTile {

	public static final int TICKET_COST = 5000;
	
	/**
	 * Create a new GetAway with a position.
	 * @param position the position
	 */
	public GetAwayTile(Point position, IMediator mediator) {
		super(position, mediator);

		pawnTypes.add(PawnType.Crook);
	}

	@Override
	public void interact(IMovable target) {
		if(target instanceof Crook){
			Crook crook = (Crook)target;
			attemptGetAway(crook);
		}
	}
	
	/**
	 * A crook attempts a get away.
	 * @param crook the escaping crook
	 */
	private void attemptGetAway(Crook crook){
		purchaseTicket(crook);
		crook.setAttemptingGetAway(true);
	}
	
	/**
	 * The crook purchases a ticket if it has enough cash.
	 * Adds that cash to the travel agency.
	 * @param crook the crook that is purchasing the ticket
	 */
	private void purchaseTicket(Crook crook){
		if(crook.getWallet().getCash() >= TICKET_COST){
			crook.getWallet().decrementCash(TICKET_COST);
			//TODO Add cash to TravelAgency!
			crook.setAttemptingGetAway(true);
		}
	}
}

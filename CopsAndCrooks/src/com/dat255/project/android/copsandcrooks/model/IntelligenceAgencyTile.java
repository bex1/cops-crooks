package com.dat255.project.android.copsandcrooks.model;

import java.util.Collection;

import com.dat255.project.android.copsandcrooks.utils.Point;

/**
 * A class representing an intelligence agency.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class IntelligenceAgencyTile extends AbstractWalkableTile implements IInteractiveTile {
	
	private static final long serialVersionUID = 11257766225618843L;
	
	/**
	 * Create a new IntelligenceAgencyTile.
	 * @param position the position of this tile
	 * @param mediator the mediator
	 */
	IntelligenceAgencyTile(Point position, IMediator mediator) {
		super(position, mediator);

        pawnTypes.add(PawnType.Officer);
	}

	@Override
	public void interact(IPawn target) {
		mediator.hinderGetAway(this);
		// Call the mediator so the gameModel can provide us with the players.
	}
        
	/**
	 * Hinders the get away of all crooks that are trying to escape. The crooks
	 * are also moved to the policestation.
	 * @param playerList a list of all players
	 */
	void hinderGetAway(Collection<Player> playerList){
		//Access the players via the mediator
		for(Player player:playerList){
			//Check the role of the player, 
			//if it's a crook you stop the get away attempt.
			if(player.getPlayerRole().equals(Role.Crook)){
				IPawn pawn = player.getPawns().iterator().next();
				if(pawn instanceof Crook){
					Crook crook = (Crook)pawn;
					if(crook.isAttemptingGetAway()){
						crook.setAttemptingGetAway(false);
						
						//TODO should a cop accompany the crook the policestation?
						mediator.moveToPoliceStation(crook);
						crook.setIsInPoliceStation(true);
					}
				}
			}
		}
	}
}
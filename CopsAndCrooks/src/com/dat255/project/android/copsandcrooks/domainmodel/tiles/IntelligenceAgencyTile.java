package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import java.util.Collection;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMediator;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.utils.Point;

/**
 * A class representing an intelligence agency.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class IntelligenceAgencyTile extends AbstractTile implements IInteractiveTile {
	/**
	 * Create a new IntelligenceAgencyTile.
	 * @param position the position of this tile
	 * @param mediator the mediator
	 */
	public IntelligenceAgencyTile(Point position, IMediator mediator) {
		super(position, mediator);

        pawnTypes.add(PawnType.Officer);
	}

        @Override
        public void interact(IMovable target) {
        	mediator.hinderGetAway(this);
        	// Call the mediator so the gameModel can provide us with the players.
        }
        
	/**
	 * Hinders the get away of all crooks that are trying to escape. The crooks
	 * are also moved to the policestation.
	 * @param playerList a list of all players
	 */
	public void hinderGetAway(Collection<Player> playerList){
		//Access the players via the mediator
		for(Player player:playerList){
			//Check the role of the player, 
			//if it's a crook you stop the get away attempt.
			if(player.getPlayerRole().equals(Role.Crook)){
				IMovable movable = player.getPawns().iterator().next();
				if(movable instanceof Crook){
					Crook crook = (Crook)movable;
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
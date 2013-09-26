package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import java.util.Collection;
import java.util.List;

import com.dat255.project.android.copsandcrooks.utils.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMediator;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;

public class IntelligenceAgencyTile extends AbstractTile implements IInteractiveTile {

        public IntelligenceAgencyTile(Point position, IMediator mediator) {
                super(position, mediator);

                pawnTypes.add(PawnType.Officer);
        }

        @Override
        public void interact(IMovable target) {
                hinderGetAway(mediator.getGameModel().getPlayers());
        }
        
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
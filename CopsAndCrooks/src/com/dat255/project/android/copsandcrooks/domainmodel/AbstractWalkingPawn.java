package com.dat255.project.android.copsandcrooks.domainmodel;

/**
 * This class represents an abstract walking pawn in the game Cops&Crooks.
 * A walking pawn can enter some buildings.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
abstract class AbstractWalkingPawn extends AbstractPawn {
	private boolean isInPoliceHouse;
	
	private boolean isWaitingOnTram;

	protected AbstractWalkingPawn(AbstractWalkableTile startTile, Role pawnRole, PawnType type, IMediator mediator, int tilesMovedEachStep) {
		super(startTile, pawnRole, type, mediator, tilesMovedEachStep);
	}

	void setIsInPoliceStation(boolean inHouse) {
		boolean oldValue = isInPoliceHouse;
		isInPoliceHouse = inHouse;
		pcs.firePropertyChange(PROPERTY_IS_IN_POLICE_HOUSE, oldValue, isInPoliceHouse);
	}
	
	boolean isWaitingOnTram(){
		return isWaitingOnTram;
	}
	
	void standingOnTramstop(boolean standing){
		isWaitingOnTram = standing;
	}
}

package com.dat255.project.android.copsandcrooks.domainmodel;

/**
 * This class represents an abstract walking pawn in the game Cops&Crooks.
 * A walking pawn can enter some buildings.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public abstract class AbstractWalkingPawn extends AbstractPawn {
	private boolean isInPoliceHouse;
	
	private boolean isWaitingOnTram;
	
	public static final String PROPERTY_IS_IN_POLICE_HOUSE = "IsInPoliceHouse";

	protected AbstractWalkingPawn(Role pawnRole, PawnType type, IMediator mediator, int tilesMovedEachStep, int id) {
		super(pawnRole, type, mediator, tilesMovedEachStep, id);
	}

	public void setIsInPoliceStation(boolean inHouse) {
		boolean oldValue = isInPoliceHouse;
		isInPoliceHouse = inHouse;
		pcs.firePropertyChange(PROPERTY_IS_IN_POLICE_HOUSE, oldValue, isInPoliceHouse);
	}
	
	public boolean isWaitingOnTram(){
		return isWaitingOnTram;
	}
	
	public void standingOnTramstop(boolean standing){
		isWaitingOnTram = standing;
	}
}

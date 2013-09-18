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
	
	public static final String PROPERTY_IS_IN_POLICE_HOUSE = "IsInPoliceHouse";

	protected AbstractWalkingPawn(Role pawnRole, IMediator mediator) {
		super(pawnRole, mediator);
	}

	public void setIsInPoliceStation(boolean inHouse) {
		boolean oldValue = isInPoliceHouse;
		isInPoliceHouse = inHouse;
		pcs.firePropertyChange(PROPERTY_IS_IN_POLICE_HOUSE, oldValue, isInPoliceHouse);
	}
	
	@Override
	public int tilesMovedEachStep() {
		return 1;
	}
}

package com.dat255.project.android.copsandcrooks.model;

/**
 * This class represents an abstract walking pawn in the game Cops&Crooks.
 * A walking pawn can enter some buildings.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
abstract class AbstractWalkingPawn extends AbstractPawn {
	private boolean isInPoliceHouse;

	protected AbstractWalkingPawn(AbstractWalkableTile startTile, Role pawnRole, PawnType type, IMediator mediator, int tilesMovedEachStep, int id) {
		super(startTile, pawnRole, type, mediator, tilesMovedEachStep, id);
	}
	
	/**
	 * Sets whether or not the pawn is in the police station.
	 * @param inHouse True to set that pawn is in the police station, false otherwise
	 */

	void setIsInPoliceStation(boolean inHouse) {
		boolean oldValue = isInPoliceHouse;
		isInPoliceHouse = inHouse;
		pcs.firePropertyChange(PROPERTY_IS_IN_POLICE_HOUSE, oldValue, isInPoliceHouse);
	}

	/**
	 * return true if the pawn is standing on a TramStop, false otherwise
	 * @return true if the pawn is standing on a TramStop, false otherwise
	 */

	boolean isWaitingOnTram(){
		return currentTile instanceof MetroStopTile;
	}
}

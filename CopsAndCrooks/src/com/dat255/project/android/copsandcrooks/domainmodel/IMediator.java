package com.dat255.project.android.copsandcrooks.domainmodel;

/**
 * A mediator implementation is responsible for communicating within the module to avoid
 * cross-reference which decouples dependencies.
 * 
 * Refers to the mediator pattern http://en.wikipedia.org/wiki/Mediator_pattern
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public interface IMediator {
	/**
	 * Register the game model for communication.
	 * 
	 * @param gameModel to register for communication.
	 */
	void registerGameModel(GameModel gameModel);
	
	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to move the movable to the police station.
	 * 
	 * @param movable the movable to be moved.
	 */
	void moveToPoliceStation(IMovable movable);
	
	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to alert the movable of what it collided with.
	 * 
	 * @param the movable that requests information about what it collided with.
	 */
	void didCollideAfterMove(IMovable movable);

	void addCashToOurPlayer(int cash, IMovable movable);
}

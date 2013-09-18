package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;

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
	 * Register the dice for communication.
	 * 
	 * @param dice to register for communication.
	 */
	void registerDice(Dice dice);
	
	/**
	 * Register the pathfinder for communication.
	 * 
	 * @param pathfinder to register for communication.
	 */
	void registerPathFinder(PathFinder pathFinder);
	
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

	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to add cash to the player who controls the movable.
	 * 
	 * @param cash the cash to add.
	 * @param movable the movable which the player should control.
	 */
	void addCashToMyPlayer(int cash, IMovable movable);

	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to roll the dice.
	 * 
	 * @return the result.
	 */
	int rollDice();

	List<LinkedList<IWalkableTile>> getPossiblePaths(Role playerRole, 
			IMovable currentPawn, int stepsToMove);
}

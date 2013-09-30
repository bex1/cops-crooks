package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IntelligenceAgencyTile;

/**
 * A mediator implementation is responsible for communicating within the module to avoid
 * cross-reference which decouples dependencies.
 * 
 * Refers to the mediator pattern http://en.wikipedia.org/wiki/Mediator_pattern
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
//TODO: REMOVE, MOVE DOC TO MEDIATOR
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
	 * @param pathFinder to register for communication.
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
	 * @param movable the movable that requests information about what it collided with.
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

	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to calculate the possible paths a pawn can move.
	 * 
	 * @param pawnType The type of the pawn.
	 * @param pawn The pawn itself.
	 * @param stepsToMove The number of steps to be moved.
	 * @return A collection with tilepaths representing paths that can be walked
	 */
	Collection<TilePath> getPossiblePaths(PawnType pawnType, 
			IMovable pawn, int stepsToMove);
	
	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to react on that the turn is done.
	 */
	void playerTurnDone();

	/**
	 * Ask the mediator to communicate with the necessary objects
	 * hinder a getaway.
	 * 
	 * Move whole method to gamemodel instead of callback?
	 */
	void hinderGetAway(IntelligenceAgencyTile intelligenceAgencyTile);
	
	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to select the pawn.
	 * 
	 * @param pawn the pawn that wants to be selected
	 */
	void changePawn(IMovable pawn);

	boolean isWantedCrookOn(IWalkableTile tile);
}

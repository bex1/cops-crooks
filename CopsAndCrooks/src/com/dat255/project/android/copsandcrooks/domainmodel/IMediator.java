package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Collection;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;

/**
 * A mediator implementation is responsible for communicating within the module to avoid
 * cross-reference which decouples dependencies.
 * 
 * Refers to the mediator pattern http://en.wikipedia.org/wiki/Mediator_pattern
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
interface IMediator {
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
	void moveToPoliceStation(AbstractPawn movable);
	
	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to alert the movable of what it collided with.
	 * 
	 * @param movable the movable that requests information about what it collided with.
	 */
	void didCollideAfterMove(AbstractPawn movable);

	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to add cash to the player who controls the movable.
	 * 
	 * @param cash the cash to add.
	 * @param movable the movable which the player should control.
	 */
	void addCashToMyPlayer(int cash, AbstractPawn movable);

	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to roll the dice.
	 * 
	 * @return the result.
	 */
	void rollDice(Player player);

	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to calculate the possible paths a pawn can move.
	 * 
	 *
	 * @param pawn The pawn itself.
	 * @param stepsToMove The number of steps to be moved.
	 * @return A collection of TilePaths representing paths that can be walked
	 */
	Collection<TilePath> getPossiblePaths(AbstractPawn pawn, int stepsToMove);
	
	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to react on that the turn is done.
	 */
	void playerTurnDone(float delay);

	/**
	 * Ask the mediator to communicate with the necessary objects
	 * hinder a getaway.
	 * 
	 * Move whole method to GameModel instead of callback?
	 */
	void hinderGetAway(IntelligenceAgencyTile intelligenceAgencyTile);
	
	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to select the pawn.
	 * 
	 * @param pawn the pawn that wants to be selected
	 */
	void changePawn(AbstractPawn pawn);

	boolean isWantedCrookOn(IWalkableTile tile);

	Collection<TilePath> getPossibleMetroPaths(AbstractPawn currentPawn);

	boolean isItMyPlayerTurn(AbstractPawn movable);
}

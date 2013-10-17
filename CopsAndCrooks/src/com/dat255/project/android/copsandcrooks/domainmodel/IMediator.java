package com.dat255.project.android.copsandcrooks.domainmodel;

import java.io.Serializable;
import java.util.Collection;

import com.dat255.project.android.copsandcrooks.domainmodel.GameModel.GameState;

/**
 * A mediator implementation is responsible for communicating within the module to avoid
 * cross-reference which decouples dependencies.
 * 
 * Refers to the mediator pattern http://en.wikipedia.org/wiki/Mediator_pattern
 * 
 * Note that the interface is package private, which indicates that it solely handels communication
 * in the domainmodel.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
interface IMediator extends Serializable{
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

	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to check if there is a wanted crook on the specified tile.
	 * 
	 * @param tile the tile to check for crooks
	 * 
	 * @return true if there is a wanted crook on the tile, false otherwise.
	 */
	boolean isWantedCrookOn(IWalkableTile tile);

	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to get possible metro paths that a pawn can travel.
	 * 
	 * @param pawn the pawn to get possible metro paths for.
	 * 
	 * @return a collection of possible metro paths for the specified pawn.
	 */
	Collection<TilePath> getPossibleMetroPaths(AbstractPawn pawn);

	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to check if the player of a pawn has the turn.
	 * 
	 * @param pawn who requests a check if it is it's players turn.
	 * 
	 * @return a boolean indicating if the player of the specified pawn has the turn.
	 */
	boolean isItMyPlayerTurn(AbstractPawn pawn);
	
	/**
	 * Ask the mediator to communicate with the necessary objects
	 * to check the current game state.
	 * 
	 * @return the state of the game.
	 */
	GameState checkState();
	
	/**
	 * Returns the current turn being played.
	 * 
	 * @return a the current turn being played.
	 */
	Turn getCurrentTurn();
}

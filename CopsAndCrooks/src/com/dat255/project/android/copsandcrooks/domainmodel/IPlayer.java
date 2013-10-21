package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Collection;

import com.dat255.project.android.copsandcrooks.utils.IObservable;

/**
 * Public functionality for a player in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public interface IPlayer extends IObservable {
	/**
	 * Returns the type of player this is.
	 * @return the type of player this is.
	 */
	Role getPlayerRole();

	/**
	 * Returns an unmodifiable collection of the pawns which the player controls.
	 * 
	 * @return an unmodifiable collection of the pawns which the player controls.
	 */
	Collection<? extends IPawn> getPawns();

	/**
	 * Returns the current pawn of the player.
	 * 
	 * @return the current pawn of the player.
	 */
	IPawn getCurrentPawn();

	/**
	 * Returns the name of the player.
	 * 
	 * @return the name of the player.
	 */
	String getName();

	/**
	 * Returns the wallet of the player.
	 * 
	 * @return the wallet of the player.
	 */
	Wallet getWallet();


	/**
	 * Returns true if one of the players walking pawns is on a tramstop,
	 * false otherwise.
	 * 
	 * @return true if one of the players walking pawns is on a tramstop,
	 * false otherwise.
	 */
	boolean isAnyWalkingPawnOnMetro();

	/**
	 * Tells the player to go by metro.
	 */
	void goByMetro();

	/**
	 * Roll the dice.
	 */
	void rollDice();

	/**
	 * Returns an unmodifiable Collection<TilePath> of the possible paths the
	 * current selected pawn get walk.
	 * 
	 * @return unmodifiable Collection<TilePath> of the possible paths.
	 */
	Collection<TilePath> getPossiblePaths();

	/**
	 * The player choose a path and the current pawn then moves along it.
	 * 
	 * The choosen path has to actually be one of the player's paths.
	 * 
	 * @param path The selected path.
	 */
	void choosePath(TilePath path);

	/**
	 * Returns true if the player is moving by dice walk, false otherwise.
	 * 
	 * @return true if the player is moving by dice walk, false otherwise.
	 */
	boolean isGoingByDice();

	/**
	 * Returns true if the player is moving by metro, false otherwise.
	 * 
	 * @return true if the player is moving by metro, false otherwise.
	 */
	boolean isGoingByMetro();

	/**
	 * Notifies the player which metrostop the selected pawn shall move to.
	 * 
	 * @param metroStop the metroStop that the selected pawn shall move to.
	 */
	void chooseMetroStop(MetroStopTile metroStop);

	/**
	 * Sets whether the player is active or not,
	 * i.e. if it is possible that the player can play a turn.
	 * @param active if the player is active
	 */
	void setActive(boolean active);

	/**
	 * Returns whether the player is active or not,
	 * i.e. if it is possible that the player can play a turn.
	 * @return true if the player is active
	 */
	boolean isActive();

	/**
	 * Returns the id of the player.
	 * @return the ID of the player
	 */
	String getID();
	
	public static final String PROPERTY_POSSIBLE_PATHS = "PossiblePaths";
	public static final String PROPERTY_DICE_RESULT = "DiceResult";
	public static final String PROPERTY_SELECTED_PAWN = "TheSelectedPawn";
}

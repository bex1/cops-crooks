package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Collection;

import com.dat255.project.android.copsandcrooks.utils.IObservable;

/**
 *  functionality for a player in the game Cops&Crooks.
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
     Collection<? extends IMovable> getPawns();
    
    /**
     * Returns the current pawn of the player.
     * 
     * @return the current pawn of the player.
     */
     IMovable getCurrentPawn();
        
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
     
     void goByMetro();
    
    /**
     * Roll the dice.
     */
     void rollDice();
    
    /**
     * Updates the possible paths that the pawn can move in.
     */
     void updatePossiblePaths();
    
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

	boolean isGoingByDice();

	boolean isGoingByMetro();

	void chooseMetroStop(TramStopTile metroStop);
}

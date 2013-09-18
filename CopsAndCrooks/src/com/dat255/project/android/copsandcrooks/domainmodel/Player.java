package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A player in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class Player {
	/**
	 * The type a player can be.
	 *
	 * @author Group 25, course DAT255 at Chalmers Uni.
	 */
	public enum PlayerType{
		Crook,
		Police,
	}
	
	private List<IMovable> pawns;
	
	private PlayerType playerType;
	
	private String name;
	
	private Wallet wallet;

	/**
	 * Initializes a new player.
	 * 
	 * @param name the name of the player.
	 * @param pawns the pawns controlled by the player, not allowed to be null or empty.
	 * @param type the type of the player.
	 */
	public Player(String name, List<IMovable> pawns, PlayerType type) {
		if (pawns == null || pawns.isEmpty()) {
			throw new IllegalArgumentException("pawns not allowed to be null or empty");
		}
		this.name = name;
		this.pawns = pawns;
		this.playerType = type;
		wallet = new Wallet();
	}
	
	/**
	 * Returns the type of player this is.
	 * @return the type of player this is.
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}
	
	/**
	 * Returns an unmodifiable collection of the pawns which the player controls.
	 * 
	 * @return an unmodifiable collection of the pawns which the player controls.
	 */
    public Collection<IMovable> getPawns() {
        return Collections.unmodifiableList(pawns);
    }
    
    /**
     * Returns the name of the player.
     * 
     * @return the name of the player.
     */
    public String getName() {
    	return name;
    }
    
    /**
     * Returns the wallet of the player.
     * 
     * @return the wallet of the player.
     */
    public Wallet getWallet() {
    	return wallet;
    }
}

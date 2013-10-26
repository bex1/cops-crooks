package com.dat255.project.android.copsandcrooks.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**	
 * Class used to describe a game
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class GameItem implements Serializable{

	private final String id;
	private String name;
	private int playerCap;
	private boolean gameStarted;
	private String hostId;
	
	private List<PlayerItem> playerList;
	
	/**
	 * Create a new GameItem with the given name and playercap
	 * @param name the name of the game
	 * @param playerCap the max number of players allowed
	 */
	public GameItem(String name, int playerCap) {
		id = UUID.randomUUID().toString();
		this.name = name;
		this.playerCap = playerCap;
		gameStarted = false;
		playerList = new ArrayList<PlayerItem>();
	}
	
	/**
	 * Create a new GameItem
	 */
	public GameItem(){
		playerList = new ArrayList<PlayerItem>();
		id = "";
	}
	
	/**
	 * Get the ID of the game
	 * @return id
	 */
	public String getID(){
		return id;
	}
	
	/**
	 * Get the name of the game
	 * @return name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Get the names of the players in the game
	 * @return list of player names
	 */
	public List<String> getPlayerNames(){
		ArrayList<String> playerNames = new ArrayList<String>();
		for(PlayerItem pi : playerList){
			playerNames.add(pi.getName());
		}
		
		return playerNames;
	}
	
	/**
	 * Get the max number of players allowed
	 * @return playerCap
	 */
	public int getPlayerCap() {
		return playerCap;
	}

	/**
	 * Get the number of players currently in the game
	 * @return numer of players
	 */
	public int getCurrentPlayerCount() {
		return this.playerList.size();
	}
	
	/**
	 * Set the name of the game
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String toString(){
		return getName();
	}
	
	/**
	 * Check if the game has started
	 * @return true if the game as started or false otherwise
	 */
	public boolean hasGameStarted(){
		return gameStarted;
	}
	
	/**
	 * Set if the game has started
	 * @param bool
	 */
	public void setGameStarted(boolean bool){
		gameStarted = bool;
	}
	
	/**
	 * Add a player the the game
	 * @param player
	 */
	public void addPlayer(PlayerItem player){
		if(this.getCurrentPlayerCount() < this.playerCap)
			playerList.add(0, player);
	}

	/**
	 * Get the players in the game
	 * @return list of players
	 */
	public List<PlayerItem> getPlayers(){
		return playerList;
	}
	
	/** 
	 * Set the id that the host of the game has
	 * @param id
	 */
	public void setHostId(String id){
		hostId = id;
	}
	
	/**
	 * Return the id that the host of the game has
	 * @return id
	 */
	public String getHostId(){
		return hostId;
	}
}

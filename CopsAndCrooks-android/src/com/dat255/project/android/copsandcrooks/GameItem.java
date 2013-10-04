package com.dat255.project.android.copsandcrooks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameItem implements Serializable{

	private String name;
	private int playerCap;
	private int currentPlayerCount;
	private boolean gameStarted;
	private PlayerItem host;
	
	private List<PlayerItem> playerList;
	
	public GameItem(String name, int playerCap) {
		this.name = name;
		this.playerCap = playerCap;
		this.currentPlayerCount = 0;
		gameStarted = false;
		playerList = new ArrayList<PlayerItem>();
	}
	
	public String getName(){
		return name;
	}
	
	public int getPlayerCap() {
		return playerCap;
	}

	public void setPlayerCap(int playerCap) {
		this.playerCap = playerCap;
	}

	public int getCurrentPlayerCount() {
		return currentPlayerCount;
	}

	public void setCurrentPlayerCount(int currentPlayerCount) {
		this.currentPlayerCount = currentPlayerCount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString(){
		return getName();
	}
	
	public boolean hasGameStarted(){
		return gameStarted;
	}
	
	public void setGameStarted(boolean bool){
		gameStarted = bool;
	}
	
	public void addPlayer(PlayerItem player){
		playerList.add(player);
	}

	public List<PlayerItem> getPlayers(){
		return playerList;
	}
	
	public void setHost(PlayerItem host){
		this.host = host;
	}
}

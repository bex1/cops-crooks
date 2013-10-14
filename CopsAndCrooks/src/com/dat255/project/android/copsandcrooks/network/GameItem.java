package com.dat255.project.android.copsandcrooks.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameItem implements Serializable{

	private int id;
	private String name;
	private int playerCap;
	private boolean gameStarted;
	private String hostId;
	
	private List<PlayerItem> playerList;
	
	public GameItem(String name, int playerCap) {
		id = (int) (Math.random()*1000);
		this.name = name;
		this.playerCap = playerCap;
		gameStarted = false;
		playerList = new ArrayList<PlayerItem>();
	}
	
	public GameItem(){
		playerList = new ArrayList<PlayerItem>();
	}
	
	public int getID(){
		return id;
	}
	
	public void setID(int gameID){
		id = gameID;
	}
	
	public String getName(){
		return name;
	}
	
	public List<String> getPlayerNames(){
		ArrayList<String> playerNames = new ArrayList<String>();
		for(PlayerItem pi : playerList){
			playerNames.add(pi.getName());
		}
		
		return playerNames;
	}
	
	public int getPlayerCap() {
		return playerCap;
	}

	public void setPlayerCap(int playerCap) {
		this.playerCap = playerCap;
	}

	public int getCurrentPlayerCount() {
		return this.playerList.size();
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
		if(this.getCurrentPlayerCount() < this.playerCap)
			playerList.add(player);
	}

	public List<PlayerItem> getPlayers(){
		return playerList;
	}
	
	public void setHostId(String id){
		hostId = id;
	}
	
	public String getHostId(){
		return hostId;
	}
}

package com.dat255.project.android.copsandcrooks.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.Turn;
import com.dat255.project.android.copsandcrooks.network.Network.*;
import com.esotericsoftware.kryonet.*;

/**	Client used to communicate between application and server **/
public class GameClient{
	
	private static GameClient instance;
	private Client client;
	private ArrayList<GameItem> gameItems;
	private GameItem chosenGameItem;
	private GameModel currentGameModel;

	private String clientID;
	private String serverIP;
	private String playerName;

	public static GameClient getInstance(){
		if(instance == null)
			instance = new GameClient();
	
		return instance;
	}

	private GameClient(){
		// initialize client
		client = new Client();
		gameItems = new ArrayList<GameItem>();
		
		// register network classes (in the same way as the server)
		Network.register(client);
		
		this.client.addListener(new Listener(){
			// connected to the server
			
			// called when the client connects to the server
			@Override
			public void connected(Connection connection) {
				// send a request to join the server
				Pck0_ClientHandshake pck = new Pck0_ClientHandshake();
				pck.playerName = getPlayerName();
				System.out.println("Network: Connecting..");
				
				sendPacket(pck);
			}

			// received a packet
			@Override
			public void received(Connection con, Object pck) {
				super.received(con, pck);
				if (pck instanceof Packet) {
					System.out.println("Network: Received packet!");

					// server sent a handshake
					if(pck instanceof Pck1_ServerHandshake){
						System.out.println("Network: Server says: " + ((Pck1_ServerHandshake) pck).message);
					}
					
					// server sent a list of games
					if(pck instanceof Pck3_GameItems){
						System.out.println("Network: Received a list of games.");
						gameItems.clear();
						for(GameItem gi : ((Pck3_GameItems)pck).gameItems){
							gameItems.add(gi);
							System.out.println("Network: Added a game.");
						}
					}

					// server sent a list of turns
					if(pck instanceof Pck5_Turns){
						System.out.println("Network: Received a list of turns.");
						getCurrentGameModel().addReplayTurns(((Pck5_Turns) pck).turns);
					}
				}
			}

			// called when connection to the server is lost 
			@Override
			public void disconnected(Connection connection) {
				System.out.println( "Network: Disconnected!");
			}
		});
		
		client.start();
	}
	

	/** Check if a game with a given name exist */
	public boolean hasGame(String name){
		for(GameItem gameItem: gameItems){
			if(gameItem.getName().equals(name))
				return true;
		}
		return false;
	}
	
	/**
	 * Connect to the server if connection is not already established
	 */
	public void connectToServer(){
		if(!client.isConnected()){
			try {
				client.start();
				System.out.println("Network: Trying to connect to " + serverIP + "..");
				
				// connect to the given IP and port with a given timeout limit (in milliseconds)
				client.connect(10000, serverIP, Network.PORT);
				if(client.isConnected())
					System.out.println("Network: Connected!");
				else
					System.out.println("Network: Not connected!");
			} catch (IOException e) {
				System.out.println("Network: Failed to connect!");
				e.printStackTrace();
				client.stop();
			}
		}
	}
	
	/**
	 * Stops the client, i.e. when changing the IP in options
	 */
	public void stopClient(){
		client.stop();
	}
	
	/**
	 * Sends a packet to the server requesting a list of games
	 */
	public void requestGameItemsFromServer() {
		if(client.isConnected()){
			System.out.println("Network: Requesting list of games from server..");
			Pck2_ClientRequestGames pck = new Pck2_ClientRequestGames();
			
			sendPacket(pck);
		}
    }
	
	/**
	 * Returns a list of games
	 * @return gameItems
	 */
	public ArrayList<GameItem> getGameItems(){
		return gameItems;
	}
	
	/**
	 * Return the connection status
	 * @return isConnected
	 */
	public boolean isConnected(){
		return client.isConnected();
	}
	
	/**
	 * Update the chosen game item in the local game item list
	 */
	public void updateChosenGameItem(){
		for(GameItem gameItem: gameItems){
			if(gameItem.getID() == chosenGameItem.getID()){
				chosenGameItem = gameItem;
				break;
			}
		}
	}
	
	/**
	 * Set the chosen game item
	 * @param gameItem
	 */
	public void setChosenGameItem(GameItem gameItem){
		chosenGameItem = gameItem;
	}
	
	/**
	 * Return of the chosen game item
	 * @return gameItem
	 */
	public GameItem getChosenGameItem(){
		return chosenGameItem;
	}

	/**
	 * Send the created game (game item) to the server
	 * @param gameItem
	 */
	public void sendCreatedGame(GameItem gameItem) {
		if(client.isConnected()){
			System.out.println("Network: Sending created game to server");
			Pck3_GameItems pck = new Pck3_GameItems();
			pck.gameItems = new ArrayList<GameItem>();
			pck.gameItems.add(gameItem);
			
			sendPacket(pck);
		}  
    }

	/**
	 * Send a request to the server to join a game (with a player)
	 * @param gameID
	 * @param player
	 */
	public void joinGame(int gameID, PlayerItem player) {
		if(client.isConnected()){
			System.out.println("Network: Joining remote game");
			Pck4_PlayerItem pck = new Pck4_PlayerItem();
			pck.gameID = gameID;
			pck.playerItem = player;

			sendPacket(pck);
		}
    }
	
	/**
	 * Set the players name
	 * @param name
	 */
	public void setPlayerName(String name){
		playerName = name;
	}
	
	/**
	 * Return the players name
	 * @return
	 */
	public String getPlayerName(){
		return playerName;
	}
	
	/**
	 * Set the IP of the server
	 * @param ip
	 */
	public void setServerIP(String ip){
		serverIP = ip;
	}
	
	/**
	 * Return the IP of the server
	 * @return ip
	 */
	public String getServerIP() {
	    return serverIP;
    }
	
	/**
	 * Set the ID of the client
	 * @param id
	 */
	public void setClientID(String id){
		clientID = id;
	}
	
	/**
	 * Return the ID of the client
	 * @return
	 */
	public String getClientID(){
		return clientID + "";
	}

	public void sendTurn(Turn currentTurn) {
		System.out.println("Network: Sending turn");
	    Pck5_Turns turnPck = new Pck5_Turns();
	    turnPck.gameID = chosenGameItem.getID();
	    turnPck.turns = new LinkedList<Turn>();
	    turnPck.turns.add(currentTurn);
	    
	    sendPacket(turnPck);
    }

	/**
	 * Send a packet to the sever requesting turns
	 */
	public void requestTurns(){
		System.out.println("Network: Requesting turns");
		Pck6_ClientRequestTurns requestPck = new Pck6_ClientRequestTurns();
		requestPck.gameID = getCurrentGameModel().getID();
		requestPck.clientTurnID = getCurrentGameModel().getTurnID();
		
		sendPacket(requestPck);
	}

	/**
	 * Return the current game model
	 * @return currentGameModel
	 */
	public GameModel getCurrentGameModel() {
		return currentGameModel;
	}

	/**
	 * Set the current game model
	 * @param currentGameModel
	 */
	public void setCurrentGameModel(GameModel currentGameModel) {
		this.currentGameModel = currentGameModel;
	}

	/**
	 * Send a packet to server to start a game
	 */
	public void startGame() {
	    System.out.println("Network: Starting game");
		Pck8_ClientStartGame pck = new Pck8_ClientStartGame();
		pck.gameID = chosenGameItem.getID();
		
		sendPacket(pck);
    }

	/**
	 * Send an edited game to server
	 * @param gameItem
	 */
	public void updateCurrentGameItem(GameItem gameItem) {
		System.out.println("Network: Sending edited game item to server");
		Pck9_ClientEditedGame gamePck = new Pck9_ClientEditedGame();
		gamePck.gameItem = gameItem;
		
		sendPacket(gamePck);
	}

	/**
	 * Send a packet telling the server a game has ended
	 */
	public void sendGameEnd() {
		Pck10_ClientEndGame pck = new Pck10_ClientEndGame();
		pck.gameID = chosenGameItem.getID();
		sendPacket(pck);
	}
	
	/**
	 * Send the given packet to the server
	 * @param packet
	 */
	private void sendPacket(Packet packet){
		client.sendTCP(packet);
	}
}

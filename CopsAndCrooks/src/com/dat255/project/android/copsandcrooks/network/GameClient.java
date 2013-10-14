package com.dat255.project.android.copsandcrooks.network;

import java.io.IOException;
import java.util.ArrayList;

import com.dat255.project.android.copsandcrooks.domainmodel.Turn;
import com.dat255.project.android.copsandcrooks.network.Network.*;
import com.esotericsoftware.kryonet.*;


public class GameClient{
	
	private static GameClient instance;
	private Client client;
	private ArrayList<GameItem> gameItems;
	private String playerName;
	private GameItem chosenGameItem;
	private String clientID;
	
	public enum Server{
		
	}
	
	public static GameClient getInstance(){
		if(instance == null)
			instance = new GameClient();
	
		return instance;
	}

	private GameClient(){
		// initialize client
		client = new Client();
		Network.register(client);
		gameItems = new ArrayList<GameItem>();
		
		this.client.addListener(new Listener(){
			// connected to the server
			@Override
			public void connected(Connection connection) {
				// send a request to join the server
				Pck0_ClientHandshake pck = new Pck0_ClientHandshake();
				pck.message = "Client says hi!";
				System.out.println("Network: Connecting..");
				client.sendTCP(pck);
			}

			// received a packet
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
				}
			}

			@Override
			public void disconnected(Connection connection) {
				System.out.println( "Network: Disconnected!");
			}
		});
		
		client.start();
	}
	
	public void connectToServer(){
		if(!client.isConnected()){
			try {
				System.out.println("Network: Trying to connect..");
				String ip = "192.168.0.14";
				System.out.println(ip);
				client.connect(120000, ip, Network.PORT);
				if(client.isConnected())
					System.out.println("Network: Connected!");
				else
					System.out.println("Network: Not connected!");
			} catch (IOException e) {
				System.out.println("Network: Failed to connect!");
				e.printStackTrace();
				client.stop();
				return;
			}
		}
	}
	
	// send a packet to the server requesting a list of games
	public void requestGameItemsFromServer() {
		if(client.isConnected()){
			System.out.println("Network: Requesting list of games from server..");
			Pck2_ClientRequestGames pck = new Pck2_ClientRequestGames();
			client.sendTCP(pck);
		}
    }
	
	public ArrayList<GameItem> getGameItems(){
		return gameItems;
	}
	
	public Client getClient(){
		return client;
	}
	
	
	public void setChosenGameItem(GameItem gameItem){
		chosenGameItem = gameItem;
	}
	
	public GameItem getChosenGameItem(){
		return chosenGameItem;
	}

	public void sendCreatedGame(GameItem gameItem) {
		if(client.isConnected()){
			System.out.println("Network: Sending created game to server");
			Pck3_GameItems pck = new Pck3_GameItems();
			pck.gameItems = new ArrayList<GameItem>();
			pck.gameItems.add(gameItem);
			client.sendTCP(pck);
		}  
    }

	public void joinGame(int gameID, PlayerItem player) {
		if(client.isConnected()){
			System.out.println("Network: Joining remote game");
			Pck4_PlayerItem pck = new Pck4_PlayerItem();
			pck.gameID = gameID;
			pck.playerItem = player;
			client.sendTCP(pck);
		}
    }
	
	public void setPlayerName(String name){
		playerName = name;
	}
	
	public String getPlayerName(){
		return playerName;
	}
	
	public void setClientID(String id){
		this.clientID = id;
	}
	
	public String getClientID(){
		return clientID + "";
	}

	public void sendTurn(Turn currentTurn) {
		System.out.println("Network: Sending turn");
	    Pck5_Turns turnPck = new Pck5_Turns();
	    turnPck.gameID = chosenGameItem.getID();
	    turnPck.turns = new ArrayList<Turn>();
	    turnPck.turns.add(currentTurn);
	    
	    client.sendTCP(turnPck);
    }

	public void startGame(int id) {
	    System.out.println("Network: Starting game");
		Pck8_StartGame pck = new Pck8_StartGame();
		pck.gameID = chosenGameItem.getID();
		
		client.sendTCP(pck);
    }
}

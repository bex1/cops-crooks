package com.dat255.project.android.copsandcrooks.network;

import java.io.IOException;
import java.util.ArrayList;
import com.dat255.project.android.copsandcrooks.network.Network.*;
import com.esotericsoftware.kryonet.*;


public class GameClient {
	
	private static GameClient instance;
	private Client client;
	private ArrayList<GameItem> gameItems;
	
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
				client.connect(60000, "192.168.1.4", Network.PORT);
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
		connectToServer();
		if(client.isConnected()){
			System.out.println("Network: Requesting list of games from server..");
			Pck2_ClientRequestGames pck = new Pck2_ClientRequestGames();
			client.sendTCP(pck);
		}
    }
	
	public ArrayList<GameItem> getGameItems(){
		return gameItems;
	}
}

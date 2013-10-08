package com.dat255.project.android.copsandcrooks.network;

import com.dat255.project.android.copsandcrooks.network.Network.*;
import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.*;

public class GameServer {

	private Server server;
	private ArrayList<GameItem> gameItems;
	
	public GameServer(){
		
		gameItems = new ArrayList<GameItem>();
		
		// test games
		gameItems.add(new GameItem("Server created #1", 5));
		gameItems.add(new GameItem("Server created #2", 5));
		gameItems.add(new GameItem("Server created #3", 5));
		
		// initialize server
		server = new Server();
		Network.register(server);
		
		server.addListener(new Listener(){
			public void received(Connection con, Object msg){
				super.received(con, msg);
					
				// client sent a packet
				if (msg instanceof Packet) {
					System.out.println("Received packet.");
					Packet packet = (Packet)msg;
					packet.setConnection(con);
					
					// client sent a handshake
					if(packet instanceof Pck0_ClientHandshake){	
						Pck1_ServerHandshake responsePacket = new Pck1_ServerHandshake();
						System.out.println(((Pck0_ClientHandshake) packet).message);
						responsePacket.message = "Welcome!";
					   	packet.getConnection().sendTCP(responsePacket);
				    }
					
					// client requested a list of game items
					if(packet instanceof Pck2_ClientRequestGames){
						
						// send the games to the client
						Pck3_GameItems pck = new Pck3_GameItems();
						pck.gameItems = new ArrayList<GameItem>(gameItems);
						packet.getConnection().sendTCP(pck);
				    }
				}
			}
			
			// a client connected
			@Override
			public void connected(Connection connection) {
				System.out.println("New connection!");
			}
			
			// a client disconnected
			@Override
			public void disconnected(Connection connection) {
				System.out.println("Lost a connection!");
			}
		});
	}
	
	public void startServer() {
		try {
			server.start();
			server.bind(Network.PORT);
			System.out.println("Server started!");
        } catch (IOException e) {
	        System.out.println("Error: failed to bind port");
	        e.printStackTrace();
        }
	}
}

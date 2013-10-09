package com.dat255.project.android.copsandcrooks.network;

import com.dat255.project.android.copsandcrooks.network.Network.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import com.esotericsoftware.kryonet.*;

public class GameServer {

	private Server server;
	private ArrayList<GameItem> gameItems;
	
	public GameServer(){
		
		gameItems = new ArrayList<GameItem>();
		
		// test games
		gameItems.add(new GameItem("Olle's game", 5));
		gameItems.add(new GameItem("Victor's game", 5));
		gameItems.add(new GameItem("Nisse's game", 5));
		
		// initialize server
		server = new Server();
		Network.register(server);
		
		server.addListener(new Listener(){
			public void received(Connection con, Object msg){
				super.received(con, msg);
					
				// client sent a packet
				if (msg instanceof Packet) {
					Packet packet = (Packet)msg;
					packet.setConnection(con);
					int clientID = con.getID();
					
					// client sent a handshake
					if(packet instanceof Pck0_ClientHandshake){
						System.out.println(new Timestamp(System.currentTimeMillis()).toString().substring(0, 19) + " " + "Client #" + clientID + ": received a handshake");
					
						Pck1_ServerHandshake responsePacket = new Pck1_ServerHandshake();
						System.out.println(new Timestamp(System.currentTimeMillis()).toString().substring(0, 19) + " " + "Client #" + clientID + ": received message: \"" + ((Pck0_ClientHandshake) packet).message + "\"");
						responsePacket.message = "Welcome!";
					   	packet.getConnection().sendTCP(responsePacket);
				    }

					// client requested a list of game items
					
					if(packet instanceof Pck2_ClientRequestGames){
						System.out.println(new Timestamp(System.currentTimeMillis()).toString().substring(0, 19) + " " + "Client #" + clientID + ": requesting list of games");
						
						// send the games to the client
						Pck3_GameItems pck = new Pck3_GameItems();
						pck.gameItems = new ArrayList<GameItem>(gameItems);
						packet.getConnection().sendTCP(pck);

						System.out.println(new Timestamp(System.currentTimeMillis()).toString().substring(0, 19) + " " + "Client #" + clientID + ": sent list of games");
				    }
				}
			}
			
			// a client connected
			@Override
			public void connected(Connection connection) {
				System.out.println(new Timestamp(System.currentTimeMillis()).toString().substring(0, 19) + " " + "New connection! Client #" + connection.getID());			}
			
			// a client disconnected
			@Override
			public void disconnected(Connection connection) {
				System.out.println(new Timestamp(System.currentTimeMillis()).toString().substring(0, 19) + " " + "Lost a connection! Client #" + connection.getID());
			}
		});
	}
	
	public void startServer() {
		try {
			server.start();
			server.bind(Network.PORT);
			System.out.println(new Timestamp(System.currentTimeMillis()).toString().substring(0, 19) + " " + "Server started!");
        } catch (IOException e) {
	        System.out.println(new Timestamp(System.currentTimeMillis()).toString().substring(0, 19) + " " + "Error: failed to bind port");
	        e.printStackTrace();
        }
	}
}

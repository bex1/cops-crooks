package com.dat255.project.android.copsandcrooks.network;

import com.dat255.project.android.copsandcrooks.domainmodel.Turn;
import com.dat255.project.android.copsandcrooks.network.Network.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import com.esotericsoftware.kryonet.*;

public class GameServer {

	private Server server;
	private ArrayList<GameItem> gameItems;
	private Map<Integer, LinkedList<Turn>> turns;
	
	public GameServer(){
		
		gameItems = new ArrayList<GameItem>();
		turns = new TreeMap<Integer, LinkedList<Turn>>();

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
						printMsg("Client #" + clientID + ": received a handshake");
					
						Pck1_ServerHandshake responsePacket = new Pck1_ServerHandshake();
						printMsg("Client #" + clientID + ": received message: \"" + ((Pck0_ClientHandshake) packet).message + "\"");
						responsePacket.message = "Welcome!";
					   	packet.getConnection().sendTCP(responsePacket);
				    }

					// client requested a list of game items
					if(packet instanceof Pck2_ClientRequestGames){
						printMsg("Client #" + clientID + ": requesting list of games");
						
						// send the games to the client
						Pck3_GameItems pck = new Pck3_GameItems();
						pck.gameItems = new ArrayList<GameItem>(gameItems);
						packet.getConnection().sendTCP(pck);

						printMsg("Client #" + clientID + ": sent list of games");
				    }
					
					// client sent a created game
					if(packet instanceof Pck3_GameItems){
						printMsg("Client #" + clientID + ": sent a created game");
						Pck3_GameItems gamePck = ((Pck3_GameItems)packet);
						gameItems.add(gamePck.gameItems.get(0));
					}
					
					// client joins a game
					if(packet instanceof Pck4_PlayerItem){
						printMsg("Client #" + clientID + ": join a game");
						Pck4_PlayerItem gamePck = ((Pck4_PlayerItem)packet);
						for(GameItem game : gameItems){
							if(game.getID() == gamePck.gameID){
								game.addPlayer(gamePck.playerItem);
							}
						}
					}
					
					// client sends a turn
					if(packet instanceof Pck5_Turns){
						printMsg("Client #" + clientID + ": sent a turn");
						Pck5_Turns gamePck = ((Pck5_Turns)packet);
						LinkedList<Turn> oldTurns = turns.get(gamePck.gameID);
						if(oldTurns == null){
							oldTurns = new LinkedList<Turn>();
						}
						oldTurns.add(gamePck.turns.get(0));
					}
					
					// client requests a list of turns
					if(packet instanceof Pck6_RequestTurns){
						Pck6_RequestTurns gamePck = ((Pck6_RequestTurns)packet);
						printMsg("Client #" + clientID + ": requested a list of turns of game: " + gamePck.gameID);
						
						Pck5_Turns responsePck = new Pck5_Turns();
						responsePck.turns = turns.get(gamePck.gameID);
						responsePck.gameID = gamePck.gameID;
						
						gamePck.getConnection().sendTCP(responsePck);
					}
					
					// client starts a game
					if(packet instanceof Pck8_StartGame){
						Pck8_StartGame gamePck = ((Pck8_StartGame)packet);
						printMsg("Client #" + clientID + ": started game: " + gamePck.gameID);
						
						for(GameItem gi : gameItems){
							if(gi.getID() == gamePck.gameID)
								gi.setGameStarted(true);
						}
					}
					
					// client sends an edited game
					if(packet instanceof Pck9_ClientEditedGame){
						Pck9_ClientEditedGame gamePck = ((Pck9_ClientEditedGame)packet);
						printMsg("Client #" + clientID + ": sent an edited game: " + gamePck.gameItem.getID());
						
						for(GameItem gi : gameItems){
							if(gi.getID() == gamePck.gameItem.getID())
								gi = gamePck.gameItem;
						}
					}
				}
			}
			
			// a client connected
			@Override
			public void connected(Connection connection) {
				printMsg("New connection! Client #" + connection.getID());
			}
			
			// a client disconnected
			@Override
			public void disconnected(Connection connection) {
				printMsg("Lost a connection! Client #" + connection.getID());
			}
		});
	}
	
	public void startServer() {
		try {
			server.start();
			server.bind(Network.PORT);
			printMsg("Server started!");
        } catch (IOException e) {
        	printMsg("Error: failed to bind port");
	        e.printStackTrace();
        }
	}
	
	private void printMsg(String message){
		System.out.println(new Timestamp(System.currentTimeMillis()).toString().substring(0, 19) + " " +  message);
	}
}

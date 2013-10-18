package com.dat255.project.android.copsandcrooks.network;

import com.dat255.project.android.copsandcrooks.domainmodel.Turn;
import com.dat255.project.android.copsandcrooks.network.Network.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import com.esotericsoftware.kryonet.*;

/** Server application */
public class GameServer {

	private Server server;
	private ArrayList<GameItem> gameItems;
	private Map<String, LinkedList<Turn>> turns;
	
	public GameServer(){
		// initialize server
		server = new Server();
		gameItems = new ArrayList<GameItem>();
		turns = new TreeMap<String, LinkedList<Turn>>();
		
		// register network classes (in the same way as the client)
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
						con.setName(((Pck0_ClientHandshake) packet).playerName);
						printMsg(con, "Received a handshake");
						
						Pck1_ServerHandshake responsePacket = new Pck1_ServerHandshake();
						responsePacket.message = "Welcome!";
					   	packet.getConnection().sendTCP(responsePacket);
				    }

					// client requested a list of game items
					else if(packet instanceof Pck2_ClientRequestGames){
						// send the games to the client
						Pck3_GameItems pck = new Pck3_GameItems();
						pck.gameItems = new ArrayList<GameItem>(gameItems);
						packet.getConnection().sendTCP(pck);
				    }
					
					// client sent a created game
					else if(packet instanceof Pck3_GameItems){
						Pck3_GameItems gamePck = ((Pck3_GameItems)packet);
						GameItem createdGame = gamePck.gameItems.get(0);
						printMsg(con, "Sent a created game \"" + createdGame.getName() + "\" (" + createdGame.getID() + ")");
						gameItems.add(gamePck.gameItems.get(0));
					}
					
					// client wants to join a game
					else if(packet instanceof Pck4_PlayerItem){
						Pck4_PlayerItem gamePck = ((Pck4_PlayerItem)packet);
						for(GameItem game : gameItems){
							if(game.getID().equals(gamePck.gameID)){
								printMsg(con, "Joined game \"" + game.getName() + "\"");
								game.addPlayer(gamePck.playerItem);
								break;
							}
						}
					}
					
					// client sends a turn
					else if(packet instanceof Pck5_Turns){
						printMsg(con, "Sent a turn");
						Pck5_Turns gamePck = ((Pck5_Turns)packet);
						LinkedList<Turn> oldTurns = turns.get(gamePck.gameID);
						if(oldTurns == null){
							oldTurns = new LinkedList<Turn>();
						}
						oldTurns.add(gamePck.turns.get(0));
					}
					
					// client requests a list of turns
					else if(packet instanceof Pck6_ClientRequestTurns){
						Pck6_ClientRequestTurns gamePck = ((Pck6_ClientRequestTurns)packet);
						
						if(turns.get(gamePck.gameID) == null){
							printMsg(con, "Requested a list of turns of INVALID game #" + gamePck.gameID);
							return;
						}

						printMsg(con, "Requested a list of turns of game #" + gamePck.gameID + ", has turn ID "+gamePck.clientTurnID + " turn size: " + turns.get(gamePck.gameID).size());

						LinkedList<Turn> replayTurns = new LinkedList<Turn>();
						for(int i = gamePck.clientTurnID; i < turns.get(gamePck.gameID).size(); i++)
							replayTurns.add(turns.get(gamePck.gameID).get(i));

						Pck5_Turns responsePck = new Pck5_Turns();
						responsePck.turns = replayTurns;
						responsePck.gameID = gamePck.gameID;
						responsePck.gameTurns = turns.get(gamePck.gameID).size();

						printMsg("\tResponded with a list of "+replayTurns.size()+" turns");
						gamePck.getConnection().sendTCP(responsePck);
					}
					
					// client starts a game
					else if(packet instanceof Pck8_ClientStartGame){
						Pck8_ClientStartGame gamePck = ((Pck8_ClientStartGame)packet);
						printMsg(con, "Started game: " + gamePck.gameID);
						
						for(GameItem gi : gameItems){
							if(gi.getID().equals(gamePck.gameID)){
								turns.put(gamePck.gameID, new LinkedList<Turn>());
							}
						}
					}
					
					// client sends an edited game
					else if(packet instanceof Pck9_ClientEditedGame){
						Pck9_ClientEditedGame gamePck = ((Pck9_ClientEditedGame)packet);
						printMsg(con, "Sent an edited game: " + gamePck.gameItem.getID());
						
						for(int i = 0; i < gameItems.size(); i++){
							if(gameItems.get(i).getID().equals(gamePck.gameItem.getID()))
								gameItems.set(i,gamePck.gameItem);
						}
					}

					// client ends the game
					else if(packet instanceof Pck10_ClientEndGame){
						for(int i = 0; i < gameItems.size(); i++){
							if(gameItems.get(i).getID().equals(((Pck10_ClientEndGame) packet).gameID)){
								gameItems.remove(i);
								// TODO Remove associated turns from this game when all clients have received last turn
								printMsg(con, "Removed game: "+((Pck10_ClientEndGame) packet).gameID);
							}
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
				printMsg("Lost a connection! \""+connection.toString()+"\" #" + connection.getID());
			}
		});
	}
	
	/**
	 * Start the server. It will fail if an existing server is running on the same port
	 */
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
	
	/**
	 * Print a message with a timestamp
	 * @param message
	 */
	private void printMsg(String message){
		System.out.println(new Timestamp(System.currentTimeMillis()).toString().substring(0, 19) + " " +  message);
	}

	private void printMsg(Connection connection, String message){
		printMsg(String.format("%-15s", connection.toString()) + " #" + connection.getID() + ": "+message);
	}
}

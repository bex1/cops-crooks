package com.dat255.project.android.copsandcrooks.network;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.LinkedList;

import com.dat255.project.android.copsandcrooks.model.Role;
import com.dat255.project.android.copsandcrooks.model.TilePath;
import com.dat255.project.android.copsandcrooks.model.Turn;
import com.dat255.project.android.copsandcrooks.model.Turn.HideoutChoice;
import com.dat255.project.android.copsandcrooks.model.Turn.MoveType;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.esotericsoftware.kryo.*;
import com.esotericsoftware.kryonet.*;

/**	
 * Network utility class
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class Network {
	
	/** The port that server and client uses */
	public static final int PORT = 54555;
	
	/** The default IP that client attempts to connect to */
	public static final String DEFAULT_IP = "81.228.55.131";
	
	/**
	 * Register the classes so they can be sent over a network.
	 * @param endPoint the end point (server/client) to register the classes on 
	 */
	static public void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(ArrayList.class);
		kryo.register(Pck0_ClientHandshake.class);
		kryo.register(Pck1_ServerHandshake.class);
		kryo.register(Pck2_ClientRequestGames.class);
		kryo.register(Pck3_GameItems.class);
		kryo.register(GameItem.class);
		kryo.register(PlayerItem.class);
		kryo.register(Pck4_PlayerItem.class);
		kryo.register(Role.class);
		kryo.register(Pck5_Turns.class);
		kryo.register(Turn.class);
		kryo.register(Pck6_ClientRequestTurns.class);
		kryo.register(TilePath.class);
		kryo.register(Point.class);
		kryo.register(LinkedList.class);
		kryo.register(PropertyChangeSupport.class);
		kryo.register(HideoutChoice.class);
		kryo.register(MoveType.class);
		kryo.register(Pck8_ClientStartGame.class);
		kryo.register(Pck9_ClientEditedGame.class);
		kryo.register(PawnItem.class);
		kryo.register(Pck10_ClientEndGame.class);
	}

	/** Super-class packet */
	public static class Packet{
		private Connection connection;
		
		public void setConnection(Connection con){
			this.connection = con;
		}
		
		public Connection getConnection(){
			return this.connection;
		}
	}
	
	/** Sent by the client upon connection */		
	public static class Pck0_ClientHandshake extends Packet{
		public String playerName;
	}

	/** Handshake sent by the server */
	public static class Pck1_ServerHandshake extends Packet{
		public String message;
	}

	/** Sent by the client upon requesting games */
	public static class Pck2_ClientRequestGames extends Packet{
	}

	/** Game item */
	public static class Pck3_GameItems extends Packet{
		public ArrayList<GameItem> gameItems;
	}
	
	/** Super-class for game item packets */
	public static class Pck_ExistingGame extends Packet {
		public String gameID;
	}
	
	/** Player item */
	public static class Pck4_PlayerItem extends Pck_ExistingGame{
		public PlayerItem playerItem;
	}
	
	/** Turn item */
	public static class Pck5_Turns extends Pck_ExistingGame{
		public LinkedList<Turn> turns;
		public int gameTurns;
	}

	/** Client request a list of turns */
	public static class Pck6_ClientRequestTurns extends Pck_ExistingGame{
		public int clientTurnID;
	}

	/** Client starts a game */
	public static class Pck8_ClientStartGame extends Pck_ExistingGame{}
	
	/** Client sends an edited game */
	public static class Pck9_ClientEditedGame extends Packet {
		public GameItem gameItem;
	}

	/** Game has ended */
	public static class Pck10_ClientEndGame extends Pck_ExistingGame{}
}
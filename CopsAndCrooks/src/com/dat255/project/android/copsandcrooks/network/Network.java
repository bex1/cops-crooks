package com.dat255.project.android.copsandcrooks.network;

import java.util.ArrayList;

import com.dat255.project.android.copsandcrooks.*;
import com.esotericsoftware.kryo.*;
import com.esotericsoftware.kryonet.*;

/** Network utility class */
public class Network {
	static public final int PORT = 54555;
	
	/**
	 * Register the network classes.
	 * @param endPoint
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
	}

	/** Packet */
	public static class Packet{
		private Connection connection;
		
		public void setConnection(Connection con){
			this.connection = con;
		}
		
		public Connection getConnection(){
			return this.connection;
		}
	}
	
	/** Entity packet */
	public static class EntityPacket extends Packet{
		public int entityID;
	}	
	
	/** Sent by the client upon connection */		
	public static class Pck0_ClientHandshake extends Packet{
		public String playerName;
		public String message;
	}

	/** Answer sent by the server */
	public static class Pck1_ServerHandshake extends Packet{
		public String message;
	}

	/** Sent by the client upon requesting games */
	public static class Pck2_ClientRequestGames extends Packet{
	}

	/** Message */
	public static class Pck3_GameItems extends Packet{
		public ArrayList<GameItem> gameItems;
	}
}
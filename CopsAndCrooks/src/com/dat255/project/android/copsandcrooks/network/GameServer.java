package com.dat255.project.android.copsandcrooks.network;

import com.dat255.project.android.copsandcrooks.network.Network.*;
import java.io.IOException;
import com.esotericsoftware.kryonet.*;

public class GameServer {

	private Server server;
	
	public GameServer(){
		server = new Server();
		Network.register(server);
		
		server.addListener(new Listener(){
			public void received(Connection con, Object msg){
				super.received(con, msg);
				System.out.println("Received packet.");
					
				if (msg instanceof Packet) {
					// put new packets in the queue	
					Packet packet = (Packet)msg;
					packet.setConnection(con);
					
					if(packet instanceof Pck0_ClientHandshake){	
						Pck1_ServerHandshake responsePacket = new Pck1_ServerHandshake();
						System.out.println(((Pck0_ClientHandshake) packet).message);
						responsePacket.message = "Welcome!";
					   	packet.getConnection().sendTCP(responsePacket);
				    }
				}
			}
			
			@Override
			public void connected(Connection connection) {
				System.out.println("New connection!");
			}
			
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

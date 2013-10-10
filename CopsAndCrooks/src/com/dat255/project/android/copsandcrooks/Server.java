package com.dat255.project.android.copsandcrooks;
import com.dat255.project.android.copsandcrooks.network.GameServer;

public class Server {
	
	public static void main(String[] args) {
		GameServer gameServer = new GameServer();

		gameServer.startServer();
	}

}

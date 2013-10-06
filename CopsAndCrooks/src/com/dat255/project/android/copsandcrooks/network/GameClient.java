package com.dat255.project.android.copsandcrooks.network;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.network.Network.*;
import com.esotericsoftware.kryonet.*;

public class GameClient {
	
	private static GameClient instance;
	private Client client;
	
	public static GameClient getInstance(){
		if(instance == null)
			instance = new GameClient();
	
		return instance;
	}

	private GameClient(){
		client = new Client();
		Network.register(client);
		
		this.client.addListener(new Listener(){
			@Override
			public void connected(Connection connection) {
				// send a request to join the server
				Pck0_ClientHandshake pck = new Pck0_ClientHandshake();
				pck.message = "Client says hi!";
				Gdx.app.log(CopsAndCrooks.LOG, "Network: Connecting..");
				client.sendTCP(pck);
			}

			public void received(Connection con, Object pck) {
				super.received(con, pck);
				if (pck instanceof Packet) {
					Gdx.app.log(CopsAndCrooks.LOG, "Network: Received!");

					if(pck instanceof Pck1_ServerHandshake){
						System.out.println(((Pck1_ServerHandshake) pck).message);
					}
				}
			}

			@Override
			public void disconnected(Connection connection) {
				Gdx.app.log(CopsAndCrooks.LOG, "Network: Disconnected!");
			}
		});
		
		client.start();
	}
	
	public void connectToServer(){
		try {
			Gdx.app.log(CopsAndCrooks.LOG, "Network: Trying to connect..");
			client.connect(2000, "127.0.0.1", Network.PORT);
		if(client.isConnected())
				Gdx.app.log(CopsAndCrooks.LOG, "Network: Connected!");
			else
				Gdx.app.log(CopsAndCrooks.LOG, "Network: Not connected!");
		} catch (IOException e) {
			Gdx.app.log(CopsAndCrooks.LOG, "Network: Failed to connect!");
			e.printStackTrace();
			client.stop();
			return;
		}
	}
}

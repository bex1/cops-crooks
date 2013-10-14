package com.dat255.project.android.copsandcrooks;

import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;

import android.app.Activity;
import android.os.AsyncTask;

public class CommunicateTask extends AsyncTask<GameItem, Void, Void> {

	private GameClient gameClient;
	private Activity activity;

	public CommunicateTask(Activity activity){
		this.activity = activity;
		this.gameClient = GameClient.getInstance();
	}

	@Override
	protected void onPostExecute(Void result) {
		if(activity instanceof LobbyActivity){
			//TODO tell GameClient load the GAmeItem and update the player list
			((LobbyActivity)activity).updatePlayerList();
		}
	}

	@Override
	protected Void doInBackground(GameItem... params) {
		while(true){
			System.out.println(this.getStatus() + "*************************************************************************");
			if(activity instanceof MainActivity){
				gameClient.connectToServer();
			}else if(activity instanceof GameBrowseActivity){
				gameClient.requestGameItemsFromServer();
			}else if(activity instanceof HostActivity){
				gameClient.sendCreatedGame(params[0]);
				return null;
			}else if(activity instanceof LobbyActivity){
				if(params == null || params.length == 0){ 
					gameClient.getGameItems();
				}else{
					gameClient.setChosenGameItem(params[0]);
					gameClient.startGame(params[0].getID());
					return null;
				}
				
			}//*/
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e){
				e.printStackTrace();
				return null;
			}
		}
		
	}
}

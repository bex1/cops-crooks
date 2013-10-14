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
	protected Void doInBackground(GameItem... params) {
		while(true){
			System.out.println(this.getStatus() + "*************************************************************************");
			if(activity instanceof MainActivity){
				gameClient.connectToServer();
			}else if(activity instanceof GameBrowseActivity){
				gameClient.requestGameItemsFromServer();
				gameClient.getChosenGameItem();
				this.publishProgress();
			}else if(activity instanceof HostActivity){
				gameClient.sendCreatedGame(params[0]);
				return null;
			}else if(activity instanceof LobbyActivity){
				if(params == null || params.length == 0){ 
					gameClient.getGameItems();
					this.publishProgress();
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

	@Override
	protected void onProgressUpdate(Void... values) {
		if(activity instanceof LobbyActivity)
			((LobbyActivity)activity).updatePlayerList();
		else if(activity instanceof GameBrowseActivity)
			((GameBrowseActivity)activity).refreshGameList();
	}
}

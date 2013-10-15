package com.dat255.project.android.copsandcrooks;

import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;

import android.app.Activity;
import android.content.Intent;
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
				this.publishProgress();
			}else if(activity instanceof HostActivity){
				// When a game is created this will send it to the server and set the chosen game to the created game
				gameClient.sendCreatedGame(params[0]);
				return null;
			}else if(activity instanceof LobbyActivity){
				if(params == null || params.length == 0){
					gameClient.getChosenGameItem();
					gameClient.updateChosenGameItem();
					this.publishProgress();
				}else{
					gameClient.updateChosenGameItem();
					// If your the host you will  strt the game but if you are 
					if(((LobbyActivity)activity).getCurrentTask() == LobbyActivity.Task.start){
						gameClient.startGame(params[0].getID());
					}else if(((LobbyActivity)activity).getCurrentTask() == LobbyActivity.Task.join){
						gameClient.joinGame(params[0].getID(), params[0].getPlayers().get(0));
					}else if(((LobbyActivity)activity).getCurrentTask() == LobbyActivity.Task.update){
						gameClient.updateCurrentGameItem(params[0]);
					}
					return null;
				}
				
			}else if(activity instanceof GameActivity){
				if(gameClient.getCurrentGameModel()!=null){
					if(gameClient.getCurrentGameModel().getGameState() != GameModel.GameState.Playing)
						gameClient.requestTurns();
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

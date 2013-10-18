package com.dat255.project.android.copsandcrooks;

import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

public class CommunicateTask extends AsyncTask<GameItem, Boolean, Void> {

	private GameClient gameClient;
	private Activity activity;

	public CommunicateTask(Activity activity){
		this.activity = activity;
		this.gameClient = GameClient.getInstance();
	}

	@Override
	protected void onPostExecute(Void result) {
		if(!gameClient.isConnected() && !(activity instanceof MenuActivity)){
			Intent intent = new Intent(activity.getApplicationContext(), MenuActivity.class);
			activity.startActivity(intent);
		}
	}

	@Override
	protected Void doInBackground(GameItem... params) {
		while(true){
			//SleepTimee is added so some activitys can make the thread sleep longer
			int sleepTime = 2000;
			//This checks which activity tha task is created in and what is should do
			if(activity instanceof MenuActivity){
				gameClient.connectToServer();
			}else if(activity instanceof GameBrowseActivity){
				gameClient.requestGameItemsFromServer();
				this.publishProgress();
			}else if(activity instanceof HostActivity){
				if(((HostActivity) activity).getThisTask() == HostActivity.ThisTask.hostGame){
					// When a game is created this will send it to the server and set the chosen game to the created game
					gameClient.sendCreatedGame(params[0]);
				}else if(((HostActivity) activity).getThisTask() == HostActivity.ThisTask.checkName){
					gameClient.requestGameItemsFromServer();
					publishProgress(gameClient.hasGame(params[0].getName()));
				}
				return null;
			}else if(activity instanceof LobbyActivity){
				if(params == null || params.length == 0){
					gameClient.requestGameItemsFromServer();
					gameClient.updateChosenGameItem();
					publishProgress();
				}else{
					// If your the host you will start the game but if you are
					if(((LobbyActivity)activity).getCurrentTask() == LobbyActivity.Task.start){
						gameClient.startGame();
					}else{
						if(((LobbyActivity)activity).getCurrentTask() == LobbyActivity.Task.join){
							gameClient.joinGame(params[0].getID(), params[0].getPlayers().get(0));
						}else if(((LobbyActivity)activity).getCurrentTask() == LobbyActivity.Task.update){
							gameClient.updateCurrentGameItem(params[0]);
						}
						gameClient.requestGameItemsFromServer();
						gameClient.updateChosenGameItem();
						publishProgress();
					}
					return null;
				}
			}else if(activity instanceof GameActivity){
				sleepTime = 4000;
				if(!gameClient.isConnected())
					gameClient.connectToServer();
				if(gameClient.getCurrentGameModel()!=null){
					if(gameClient.getCurrentGameModel().getGameState() == GameModel.GameState.Waiting){
						gameClient.requestTurns();
					}
				}
			}	

			if(!gameClient.isConnected())
				this.publishProgress();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e){
				e.printStackTrace();
				return null;
			}
		}
		
	}

	@Override
	protected void onProgressUpdate(Boolean... values) {
		if(activity instanceof MenuActivity){
			((MenuActivity)activity).showMessage("Failed to connect");
		}else if(activity instanceof LobbyActivity){
			((LobbyActivity)activity).updatePlayerList();
		}else if(activity instanceof GameBrowseActivity){
			((GameBrowseActivity)activity).refreshGameList();
		}else if(activity instanceof HostActivity){
			if(((HostActivity) activity).getThisTask() == HostActivity.ThisTask.checkName){
				((HostActivity)activity).hostButtonEnabled(!values[0]);
			}
		}
	}
}

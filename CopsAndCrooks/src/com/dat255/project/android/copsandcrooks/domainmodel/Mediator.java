package com.dat255.project.android.copsandcrooks.domainmodel;


/**
 * The mediator is responsible for communicating within the module to avoid
 * cross-reference which decouples dependencies.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class Mediator implements IMediator {
	private GameModel gameModel;

	@Override
	public void registerGameModel(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	@Override
	public void didCollideAfterMove(IMovable movable) {
		if (gameModel != null)
			gameModel.notifyWhatICollidedWith(movable);
	}
	
	@Override
	public void moveToPoliceStation(IMovable movable) {
		if (gameModel != null)
			gameModel.moveToEmptyPoliceStationTile(movable);
	}

	@Override
	public void addCashToOurPlayer(int cash, IMovable movable) {
		if (gameModel != null)
			gameModel.findPlayerAndAddCash(cash, movable);
	}
}

package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Collection;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IntelligenceAgencyTile;


/**
 * The mediator is responsible for communicating within the module to avoid
 * cross-reference which decouples dependencies.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public final class Mediator implements IMediator {
	private GameModel gameModel;
	private Dice dice;
	private PathFinder pathFinder;

	@Override
	public void registerGameModel(GameModel gameModel) {
		this.gameModel = gameModel;
	}
	
	@Override
	public void registerDice(Dice dice) {
		this.dice = dice;
	}

	@Override
	public void registerPathFinder(PathFinder pathFinder) {
		this.pathFinder = pathFinder;
	}
	@Override
	public void didCollideAfterMove(IMovable movable) {
		if (gameModel != null)
			gameModel.notifyWhatICollidedWith(movable);
	}
	
	@Override
	public void changePawn(Officer pawn){
		if (gameModel != null)
			gameModel.officerSelected(pawn);
	}
	
	@Override
	public void moveToPoliceStation(IMovable movable) {
		if (gameModel != null)
			gameModel.moveToEmptyPoliceStationTile(movable);
	}

	@Override
	public void addCashToMyPlayer(int cash, IMovable movable) {
		if (gameModel != null)
			gameModel.findPlayerAndAddCash(cash, movable);
	}

	@Override
	public int rollDice() {
		if (dice != null) {
			return dice.roll();
		} else {
			throw new NullPointerException("No dice is registered");
		}
	}

	@Override
	public Collection<TilePath> getPossiblePaths(PawnType pawnType,
			IMovable pawn, int stepsToMove) {
		if (pathFinder != null) {
			return pathFinder.calculatePossiblePaths(pawn, stepsToMove);
		} else {
				throw new NullPointerException("No pathfinder is registered");
		}
	}
	
	@Override
	public void playerTurnDone(){
		if (gameModel != null) 
			gameModel.nextPlayer();
	}

	@Override
	public void hinderGetAway(IntelligenceAgencyTile intelligenceAgencyTile) {
		if (gameModel != null) 
			gameModel.hinderGetAway(intelligenceAgencyTile);
	}

	@Override
	public boolean isWantedCrookOn(IWalkableTile tile) {
		if (gameModel != null) 
			return gameModel.checkIfWantedCrookAt(tile);
		return false;
	}
}

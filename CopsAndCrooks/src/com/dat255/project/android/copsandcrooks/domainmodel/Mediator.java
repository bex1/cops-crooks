package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Collection;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;


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
	public void didCollideAfterMove(AbstractPawn movable) {
		if (gameModel != null)
			gameModel.notifyWhatICollidedWith(movable);
	}
	
	@Override
	public void changePawn(AbstractPawn pawn){
		if (gameModel != null)
			gameModel.pawnSelected(pawn);
	}
	
	@Override
	public void moveToPoliceStation(AbstractPawn movable) {
		if (gameModel != null)
			gameModel.moveToEmptyPoliceStationTile(movable);
	}

	@Override
	public void addCashToMyPlayer(int cash, AbstractPawn movable) {
		if (gameModel != null)
			gameModel.findPlayerAndAddCash(cash, movable);
	}

	@Override
	public void rollDice(Player player) {
		if (dice != null) 
			dice.roll(player);
	}

	@Override
	public Collection<TilePath> getPossiblePaths(PawnType pawnType,
			AbstractPawn pawn, int stepsToMove) {
		if (pathFinder != null) {
			return pathFinder.calculatePossiblePaths(pawn, stepsToMove);
		} else {
			throw new NullPointerException("No pathfinder is registered");
		}
	}
	
	@Override
	public void playerTurnDone(float delay){
		if (gameModel != null) 
			gameModel.nextPlayer(delay);
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

	@Override
	public Collection<TilePath> getPossibleMetroPaths(AbstractPawn pawn) {
		if (pathFinder != null) {
			return pathFinder.calculatePossibleMetroPaths(pawn);
		} else {
			throw new NullPointerException("No pathfinder is registered");
		}
	}

	@Override
	public boolean isItMyPlayerTurn(AbstractPawn movable) {
		if (gameModel != null) 
			return gameModel.isCurrentPlayerOwnerOfPawn(movable);
		return false;
	}
}

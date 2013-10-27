package com.dat255.project.android.copsandcrooks.model;

import java.util.Collection;

import com.dat255.project.android.copsandcrooks.model.GameModel.GameState;

/**
 * The mediator is responsible for communicating within the module to avoid
 * cross-reference which decouples dependencies.
 * 
 * Refers to the mediator pattern http://en.wikipedia.org/wiki/Mediator_pattern
 * 
 * Note that the interface is package private, which indicates that it solely handels communication
 * in the domainmodel.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
final class Mediator implements IMediator {
	
	private static final long serialVersionUID = 112548962226547L;
	
	
	private GameModel gameModel;
	private Dice dice;
	private PathFinder pathFinder;
	
	Mediator() {}

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
	public Collection<TilePath> getPossiblePaths(AbstractPawn pawn, int stepsToMove) {
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

	@Override
	public GameState checkState() {
		if (gameModel != null) 
			return gameModel.getGameState();
		return null;
	}

	@Override
	public Turn getCurrentTurn() {
		if (gameModel != null) 
			return gameModel.getCurrentTurn();
		return null;
	}

	@Override
	public void addCashToTravelAgency(int cash) {
		if (gameModel != null) 
			gameModel.addCashToTravelAgency(cash);
	}
}

package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;


/**
 * The mediator is responsible for communicating within the module to avoid
 * cross-reference which decouples dependencies.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class Mediator implements IMediator {
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
	public List<TilePath> getPossiblePaths(PawnType pawnType,
			IMovable pawn, int stepsToMove) {
		if (pathFinder != null) {
			return pathFinder.calculatePossiblePaths(pawn, stepsToMove);
		} else {
				throw new NullPointerException("No pathfinder is registered");
		}
	}
}

package com.dat255.project.android.copsandcrooks.domainmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Path finder in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
final class PathFinder implements Serializable {
	private final AbstractWalkableTile[][] tiles;
	private final List<TramLine> metroLines;
	private final IMediator mediator;

	PathFinder(final AbstractWalkableTile[][] tiles, final IMediator mediator, final List<TramLine> metroLines) {
		if (tiles == null)
			throw new IllegalArgumentException("Tiles not allowed to be null");
		if (mediator == null)
			throw new IllegalArgumentException("Mediator not allowed to be null");
		if (metroLines == null)
			throw new IllegalArgumentException("Metro not allowed to be null");

		this.tiles = tiles;
		this.mediator = mediator;
		this.metroLines = metroLines;
		mediator.registerPathFinder(this);
	}
	
	Collection<TilePath> calculatePossibleMetroPaths(AbstractPawn pawn) {
		Collection<TilePath> paths = new ArrayList<TilePath>();
		AbstractWalkableTile currentTile = pawn.getCurrentTile();
		if (currentTile instanceof TramStopTile) {
			for (TramLine metroLine : metroLines) {
				TilePath path = metroLine.getPossibleStops();
				if (path != null && metroLine.contains(currentTile)) {
					paths.add(path);
				}
			}
		}
		return paths;
	}

	Collection<TilePath> calculatePossiblePaths(AbstractPawn pawn, int stepsToMove) {
		// Note that the current tile might be null
		AbstractWalkableTile currentTile = pawn.getCurrentTile();
		if (currentTile != null && stepsToMove > 0) {
			return Collections.unmodifiableCollection(calculateActualPossiblePaths(pawn, pawn.getPawnType(), stepsToMove, stepsToMove, pawn.getCurrentTile(), pawn.getCurrentTile(), null, pawn.getDirection()));
		} else {
			return null;
		}
	}

	private List<TilePath> calculateActualPossiblePaths(AbstractPawn pawn, PawnType pawnType,
			int stepsRemaining, int stepsToMove, AbstractWalkableTile currentTile, AbstractWalkableTile startTile, AbstractWalkableTile previousTile, Direction direction) {

		if(stepsRemaining==0 || (previousTile != null && (currentTile instanceof HideoutTile || currentTile instanceof RobbableBuildingTile 
				|| currentTile instanceof GetAwayTile ||(pawn.getPawnRole() == Role.Cop && mediator.isWantedCrookOn(currentTile))))){
			TilePath path = new TilePath();
			path.addTileLast(currentTile);
			List<TilePath> subPaths = new ArrayList<TilePath>();
			subPaths.add(path);
			return subPaths;
		}

		int x = currentTile.getPosition().x;
		int y = currentTile.getPosition().y;

		// temporary store direction to restore state after calculation,
		// since the direction is changed when calculating
		Direction pawnDirection= direction;

		List<TilePath> subPathsAllDirections = new ArrayList<TilePath>();
		AbstractWalkableTile nextTile = null;

		for(int i=0; i<4; i++){
			switch(i){
			case 0:
				if(x+1<tiles.length && isAllowedDirection(Direction.EAST, pawn, direction)){
					nextTile = tiles[x+1][y];
					pawnDirection = Direction.EAST;
				}else
					nextTile = null;
				break;
			case 1:
				if(y+1<tiles[x].length && isAllowedDirection(Direction.NORTH, pawn, direction)){
					nextTile = tiles[x][y+1];
					pawnDirection = Direction.NORTH;
				}else
					nextTile = null;
				break;
			case 2:
				if(x-1>=0 && isAllowedDirection(Direction.WEST, pawn, direction)){
					nextTile = tiles[x-1][y];
					pawnDirection = Direction.WEST;
				}else
					nextTile = null;
				break;
			case 3:
				if(y-1>=0 && isAllowedDirection(Direction.SOUTH, pawn, direction)){
					nextTile = tiles[x][y-1];
					pawnDirection = Direction.SOUTH;
				}else
					nextTile = null;
				break;
			}
			if(canMoveTo(nextTile, previousTile, pawn, pawnType, startTile, stepsRemaining)){
				List<TilePath> subPaths = calculateActualPossiblePaths(pawn, pawnType, stepsRemaining-1, stepsToMove, nextTile, startTile, currentTile, pawnDirection);
				subPathsAllDirections.addAll(subPaths);
				pawnDirection = direction;
				if(stepsToMove != stepsRemaining){
					for(TilePath subPath : subPaths){
						if(subPath.contains(currentTile))
							// Concurrent modification fix
							subPathsAllDirections.remove(subPath);
						else
							subPath.addTileLast(currentTile);
					}
				}
			}
		}

		return subPathsAllDirections;
	}

	private boolean isAllowedDirection(Direction direction, AbstractPawn pawn, Direction pawnsCurrentDirection){
		if(pawn instanceof CopCar){
			// CopCar can only make 90 degrees turns,
			// i.e. not make 180 degrees turns
			switch (direction){
				case EAST:
					return pawnsCurrentDirection!=Direction.WEST;
				case NORTH:
					return pawnsCurrentDirection!=Direction.SOUTH;
				case WEST:
					return pawnsCurrentDirection!=Direction.EAST;
				case SOUTH:
					return pawnsCurrentDirection!=Direction.NORTH;
			}
		}
		return true;
	}

	private boolean canMoveTo(AbstractWalkableTile target, AbstractWalkableTile previous, AbstractPawn pawn, PawnType pawnType, AbstractWalkableTile startTile, int stepsRemaining){
		// Check that the target is valid
		if (target != null && target != previous && target != startTile 
				&& target.getAllowedPawnTypes().contains(pawnType)) {
			
			// Is it occupied?
			if (target.isOccupied()) {
				// Just one step left? We will end on target
				if (stepsRemaining == 1) {
					return (pawnType == PawnType.Car || pawnType == PawnType.Officer) && mediator.isWantedCrookOn(target);
				// More steps left, check so we don't pass through a police when a wanted crook
				} else if (pawn instanceof Crook) {
					PawnType targetType = target.getOccupiedBy();
					Crook crook = (Crook)pawn;
					return !(crook.isWanted() && (targetType == PawnType.Car || targetType == PawnType.Officer));
				// More steps left. We can pass.
				} else {
					return true;
				}
			// Was not occupied and we are accepted. We can move.
			} else {
				return true;
			}
		}
		return false;
	}

}

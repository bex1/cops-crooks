package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.HideoutTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;

/**
 * Path finder in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public final class PathFinder {
	private IWalkableTile[][] tiles;

	public PathFinder(IWalkableTile[][] tiles, IMediator mediator) {
		if (tiles == null)
			throw new IllegalArgumentException("Tiles not allowed to be null");
		if (mediator == null)
			throw new IllegalArgumentException("Mediator not allowed to be null");

		this.tiles = tiles;
		mediator.registerPathFinder(this);
	}

	public Collection<TilePath> calculatePossiblePaths(IMovable pawn, int stepsToMove) {
		// Note that the current tile might be null
		IWalkableTile currentTile = pawn.getCurrentTile();
		if (currentTile != null) {
			return Collections.unmodifiableCollection(calculateActualPossiblePaths(pawn.getPawnType(), stepsToMove, stepsToMove, pawn.getCurrentTile(), pawn.getCurrentTile(), null));
		} else {
			return null;
		}
	}

	private List<TilePath> calculateActualPossiblePaths(PawnType pawnType,
			int stepsRemaining, int stepsToMove, IWalkableTile currentTile, IWalkableTile startTile, IWalkableTile previousTile) {
		
		if(stepsRemaining==0 || (previousTile != null && currentTile instanceof HideoutTile )){
			TilePath path = new TilePath();
			path.addTileLast(currentTile);
			List<TilePath> subPaths = new LinkedList<TilePath>();
			subPaths.add(path);
			return subPaths;
		}
		
		int x = currentTile.getPosition().x;
		int y = currentTile.getPosition().y;
		
		List<TilePath> subPathsAllDirections = new LinkedList<TilePath>();
		IWalkableTile nextTile = null;
		
		for(int i=0; i<4; i++){
			switch(i){
			case 0:
				if(x+1<tiles.length)
					nextTile = tiles[x+1][y];
				else
					nextTile = null;
				break;
			case 1:
				if(y+1<tiles[x].length)
					nextTile = tiles[x][y+1];
				else
					nextTile = null;
				break;
			case 2:
				if(x-1>=0)
					nextTile = tiles[x-1][y];
				else
					nextTile = null;
				break;
			case 3:
				if(y-1>=0)
					nextTile = tiles[x][y-1];
				else
					nextTile = null;
				break;
			}
			if(nextTile != null && nextTile != previousTile && canMoveTo(nextTile, pawnType, startTile, stepsRemaining) && 
					!(stepsRemaining == 1 && (nextTile.isOccupied() /* TODO cross reference problem? && (nextTile.getOccupiedBy() == PawnType.Crook && (pawnType == PawnType.Car || pawnType == PawnType.Officer))*/))){
				List<TilePath> subPaths = calculateActualPossiblePaths(pawnType, stepsRemaining-1, stepsToMove, nextTile, startTile, currentTile);
				if(stepsToMove != stepsRemaining){
					for(TilePath subPath : subPaths){
						if(subPath.contains(currentTile))
							subPaths.remove(subPath);
						else
							subPath.addTileLast(currentTile);
					}
				}
				subPathsAllDirections.addAll(subPaths);
			}
		}		
		
		return subPathsAllDirections;
	}
	
	private boolean canMoveTo(IWalkableTile tile, PawnType pawnType, IWalkableTile startTile, int stepsRemaining){
		return (tile.getAllowedPawnTypes().contains(pawnType) && tile != startTile)
				|| (tile.getOccupiedBy() == PawnType.Crook && pawnType==PawnType.Officer
				&& tile.getAllowedPawnTypes().contains(pawnType) && tile != startTile);
	}

}

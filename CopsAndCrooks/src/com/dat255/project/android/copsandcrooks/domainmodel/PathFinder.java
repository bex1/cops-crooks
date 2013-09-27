package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.ArrayList;
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
	private IMediator mediator;

	public PathFinder(IWalkableTile[][] tiles, IMediator mediator) {
		if (tiles == null)
			throw new IllegalArgumentException("Tiles not allowed to be null");
		if (mediator == null)
			throw new IllegalArgumentException("Mediator not allowed to be null");

		this.tiles = tiles;
		this.mediator = mediator;
		mediator.registerPathFinder(this);
	}

	public Collection<TilePath> calculatePossiblePaths(IMovable pawn, int stepsToMove) {
		// Note that the current tile might be null
		IWalkableTile currentTile = pawn.getCurrentTile();
		if (currentTile != null && stepsToMove > 0) {
			return Collections.unmodifiableCollection(calculateActualPossiblePaths(pawn, pawn.getPawnType(), stepsToMove, stepsToMove, pawn.getCurrentTile(), pawn.getCurrentTile(), null));
		} else {
			return null;
		}
	}

	private List<TilePath> calculateActualPossiblePaths(IMovable pawn, PawnType pawnType,
			int stepsRemaining, int stepsToMove, IWalkableTile currentTile, IWalkableTile startTile, IWalkableTile previousTile) {
		
		if(stepsRemaining==0 || (previousTile != null && currentTile instanceof HideoutTile )){
			TilePath path = new TilePath();
			path.addTileLast(currentTile);
			List<TilePath> subPaths = new ArrayList<TilePath>();
			subPaths.add(path);
			return subPaths;
		}
		
		int x = currentTile.getPosition().x;
		int y = currentTile.getPosition().y;
		
		List<TilePath> subPathsAllDirections = new ArrayList<TilePath>();
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
			if(canMoveTo(nextTile, previousTile, pawn, pawnType, startTile, stepsRemaining)){
				List<TilePath> subPaths = calculateActualPossiblePaths(pawn, pawnType, stepsRemaining-1, stepsToMove, nextTile, startTile, currentTile);
				subPathsAllDirections.addAll(subPaths);
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

	private boolean canMoveTo(IWalkableTile target, IWalkableTile previous, IMovable pawn, PawnType pawnType, IWalkableTile startTile, int stepsRemaining){
		// Check that the target is valid
		if (target != null && target != previous && target != startTile 
				&& target.getAllowedPawnTypes().contains(pawnType)) {
			
			// Is it occupied?
			if (target.isOccupied()) {
				// Just one step left? We will end on target
				if (stepsRemaining == 1) {
					if (pawnType == PawnType.Car || pawnType == PawnType.Officer) {
						return mediator.isWantedCrookOn(target);
					} else {
						return false;
					}
				// More steps left, check so we dont pass through a police when a wanted crook
				} else if (pawn instanceof Crook) {
					PawnType targetType = target.getOccupiedBy();
					Crook crook = (Crook)pawn;
					if (crook.isWanted() && (targetType == PawnType.Car || targetType == PawnType.Officer)) {
						return false;
					} else {
						return true;
					}
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

package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;

public final class PathFinder {
	private IWalkableTile[][] tiles;
	private List<TilePath> paths;
	


	public PathFinder(IWalkableTile[][] tiles, IMediator mediator) {
		if (tiles == null)
			throw new IllegalArgumentException("Tiles not allowed to be null");

		this.tiles = tiles;
		mediator.registerPathFinder(this);
	}

	public List<TilePath> calculatePossiblePaths(IMovable pawn, int stepsToMove) {
		// TODO implement this... note to get current tile just do pawn.getCurrentTile()
		// maybe we need to make the paths of a Path class. since we later need to choose one path.



		return null;
	}
	private List<TilePath> calculatePossiblePaths_(PawnType pawnType,
			int stepsToMove, IWalkableTile currentTile, IWalkableTile previousTile) {
		
		if(stepsToMove==0){
			TilePath path = new TilePath();
			path.addTile(currentTile);
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
				break;
			case 1:
				if(y+1<tiles[x].length)
					nextTile = tiles[x][y+1];
				break;
			case 2:
				if(x-1>=0)
					nextTile = tiles[x-1][y];
				break;
			case 3:
				if(y-1>=0)
					nextTile = tiles[x][y-1];
				break;
			}
			if(nextTile!=previousTile && canMoveTo(nextTile, pawnType)){
				List<TilePath> subPaths = calculatePossiblePaths_(pawnType, stepsToMove-1, nextTile, currentTile);
				for(TilePath subPath : subPaths){
					subPath.addTile(currentTile);
				}
				subPathsAllDirections.addAll(subPaths);
			}
		}		
		
		return subPathsAllDirections;
	}
	private boolean canMoveTo(IWalkableTile tile, PawnType pawnType){
		return (!tile.isOccupied() && tile.getAllowedPawnTypes().contains(pawnType))
				|| (tile.getOccupiedBy() == PawnType.Crook && pawnType==PawnType.Officer);
	}

}

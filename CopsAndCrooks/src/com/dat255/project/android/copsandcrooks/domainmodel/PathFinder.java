package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;

public final class PathFinder {
	private IWalkableTile[][] tiles;
	
	public PathFinder(IWalkableTile[][] tiles, IMediator mediator) {
		if (tiles == null)
			throw new IllegalArgumentException("Tiles not allowed to be null");
		
		this.tiles = tiles;
		mediator.registerPathFinder(this);
	}

	public List<LinkedList<IWalkableTile>> calculatePossiblePaths(Role role,
			IMovable pawn, int stepsToMove) {
		// TODO implement this... note to get current tile just do pawn.getCurrentTile()
		// maybe we need to make the paths of a Path class. since we later need to choose one path.
		return null;
	}

}

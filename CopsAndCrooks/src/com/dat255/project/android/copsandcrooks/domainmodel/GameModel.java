package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;

public class GameModel {
	
	private List<Player> players;
	private IWalkableTile[][] tiles;
	private PathFinder pathFinder;

	public GameModel(List<Player> players, IWalkableTile[][] tiles) {
		this.players = players;
		this.tiles = tiles;
	}
	
	
}

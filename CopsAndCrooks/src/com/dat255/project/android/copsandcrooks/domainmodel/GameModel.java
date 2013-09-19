package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.ArrayList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.PoliceStation;

public class GameModel  {
	
	private List<Player> players;
	private List<PoliceStation> policeStationTiles;

	public GameModel(IMediator mediator, List<Player> players, IWalkableTile[][] tiles) {
		if (mediator == null)
			throw new IllegalArgumentException("Mediator not allowed to be null");
		if (players == null || players.isEmpty())
			throw new IllegalArgumentException("Players not allowed to be null or empty");
		if (tiles == null)
			throw new IllegalArgumentException("Tiles not allowed to be null");
		
		this.players = players;
		
		mediator.registerGameModel(this);
		
		policeStationTiles = new ArrayList<PoliceStation>();
		
		// Extract police station tiles
		for (IWalkableTile[] tileArray : tiles) {
			for (IWalkableTile tile : tileArray) {
				if (tile instanceof PoliceStation) {
					policeStationTiles.add((PoliceStation)tile);
				}
			}
		}
	}

	void moveToEmptyPoliceStationTile(IMovable movable) {
		PoliceStation policeStationTile = findEmptyPoliceStationTile();
		movable.setCurrentTile(policeStationTile);
	}

	private PoliceStation findEmptyPoliceStationTile() {
		for (PoliceStation tile : policeStationTiles) {
			if (!tile.isOccupied()) {
				return tile;
			}
		}
		// Should always be room in policehouse.
		assert false;
		return null;
	}

	void notifyWhatICollidedWith(IMovable movable) {
		if (movable == null) {
			throw new IllegalArgumentException("Movable not allowed to be null");
		}
		for (Player player : players) {
			for (IMovable pawn : player.getPawns()) {
				if (pawn.getCurrentTile() == movable.getCurrentTile() && pawn != movable) {
					movable.collisionAfterMove(pawn);
				}
			}
		}
	}

	void findPlayerAndAddCash(int cash, IMovable movable) {
		for (Player player : players) {
			if (player.getPawns().contains(movable)) {
				player.getWallet().incrementCash(cash);
			}
		}
		
	}
}

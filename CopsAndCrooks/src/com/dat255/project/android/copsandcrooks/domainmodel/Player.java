package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Player {
	
	public enum PlayerType{
		Crook,
		Player,
	}
	
	private List<IMovable> pawns;
	
	private PlayerType playerType;

	public Player(List<IMovable> pawns, PlayerType type) {
		this.pawns = pawns;
		this.playerType = type;
	}
	
	public PlayerType getPlayerType() {
		return playerType;
	}
	
    public Collection<IMovable> getPawns() {
        return Collections.unmodifiableList(pawns);
    }
}

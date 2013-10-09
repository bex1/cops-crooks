package com.dat255.project.android.copsandcrooks.domainmodel;

import java.io.Serializable;

public class Turn implements Serializable{
	public enum HideoutChoice {
		Withdraw,
		Deposit,
		Cancel,
	}
	
	public enum MoveType {
		Metro,
		Walk,
	}

	private TilePath pathWalked;
	private IWalkableTile endTile;
	private int playerID, pawnID;
	private HideoutChoice hideoutChoice;
	private MoveType moveType;
	
	public MoveType getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}

	public HideoutChoice getHideoutChoice() {
		return hideoutChoice;
	}

	public void setHideoutChoice(HideoutChoice hideoutChoice) {
		this.hideoutChoice = hideoutChoice;
	}

	public TilePath getPathWalked() {
		return pathWalked;
	}
	
	public void setPathWalked(TilePath pathWalked) {
		this.pathWalked = pathWalked;
	}
	
	public IWalkableTile getEndTile() {
		return endTile;
	}
	
	public void setEndTile(IWalkableTile endTile) {
		this.endTile = endTile;
	}
	
	public int getPlayerID() {
		return playerID;
	}
	
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	public int getPawnID() {
		return pawnID;
	}
	
	public void setPawnID(int pawnID) {
		this.pawnID = pawnID;
	}
}

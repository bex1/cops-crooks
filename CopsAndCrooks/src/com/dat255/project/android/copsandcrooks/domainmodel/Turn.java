package com.dat255.project.android.copsandcrooks.domainmodel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.utils.Point;

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

	private List<Point> pathWalked;
	private Point endTilePos;
	private int pawnID;
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

	public List<Point> getPathWalked() {
		return pathWalked;
	}
	
	public void setPathWalked(TilePath tilePathWalked) {
		this.pathWalked = new LinkedList<Point>();
		for(int i=0; i<tilePathWalked.getPathLength(); i++){
			this.pathWalked.add(tilePathWalked.getTile(i).getPosition());
		}
	}
	
	public Point getEndTilePos() {
		return endTilePos;
	}
	
	public void setEndTile(IWalkableTile endTile) {
		endTilePos = endTile.getPosition();
	}
	
	public int getPawnID() {
		return pawnID;
	}
	
	public void setPawnID(int pawnID) {
		this.pawnID = pawnID;
	}
}

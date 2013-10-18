package com.dat255.project.android.copsandcrooks.domainmodel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.utils.Point;
/**
 * A crook pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
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
	/**
	 * Returns the type of how the pawn is moving
	 * @return the type of how the pawn is moving
	 */
	public MoveType getMoveType() {
		return moveType;
	}
	/**
	 * Sets the type of how the pawn is moving
	 * @param moveType the type that the pawn will be moving
	 */
	public void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}
	/**
	 * Returns what the players does inside a hideout
	 * @return what the player does inside a hideout
	 */
	public HideoutChoice getHideoutChoice() {
		return hideoutChoice;
	}
	/**
	 * Sets what the player does inside a hideout
	 * @param hideoutChoice what the player does inside a hideout
	 */
	public void setHideoutChoice(HideoutChoice hideoutChoice) {
		this.hideoutChoice = hideoutChoice;
	}
	/**
	 * Returns the path that the pawn has walked
	 * @return the path that the pawn has walked
	 */
	public List<Point> getPathWalked() {
		return pathWalked;
	}
	/**
	 * Sets the path that the pawn has walked
	 * @param tilePathWalked - the path that the pawns has walked
	 */
	public void setPathWalked(TilePath tilePathWalked) {
		this.pathWalked = new LinkedList<Point>();
		for(int i=0; i<tilePathWalked.getPathLength(); i++){
			this.pathWalked.add(tilePathWalked.getTile(i).getPosition());
		}
	}
	/**
	 * Returns the last tile on the path
	 * @return the last tile on the path
	 */
	public Point getEndTilePos() {
		return endTilePos;
	}
	/**
	 * Sets the end tile on the path
	 * @param endTile the end tile of the path
	 */
	public void setEndTile(IWalkableTile endTile) {
		endTilePos = endTile.getPosition();
	}
	/**
	 * Returns the ID of the pawn
	 * @return the ID of the pawn
	 */
	public int getPawnID() {
		return pawnID;
	}
	/**
	 * Sets the ID of the pawn
	 * @param pawnID the ID of the pawn
	 */
	public void setPawnID(int pawnID) {
		this.pawnID = pawnID;
	}
}

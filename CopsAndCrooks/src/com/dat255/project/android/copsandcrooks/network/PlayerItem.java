package com.dat255.project.android.copsandcrooks.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.model.Role;
import com.dat255.project.android.copsandcrooks.utils.Point;

/**	
 * Class used to desribe a player
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class PlayerItem implements Serializable{
	
	private static final long serialVersionUID = 8824775206655227L;

	private final String name;
	private Role role;
	private final String playerID;
	private final List<PawnItem> pawns = new ArrayList<PawnItem>();
	
	/**
	 * Create a new PlayerItem with the given name and ID
	 * @param name
	 * @param playerID
	 */
	public PlayerItem(String name, String playerID) {
		this.name = name;
		this.playerID = playerID;
		
		//default role
		role = Role.Crook;
	}
	
	/**
	 * Create a new player item
	 */
	public PlayerItem() {
		name = "";
		playerID ="";
	}
	
	/**
	 * Get the name of the player
	 * @return name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Get the role of the player
	 * @return role
	 */
	public Role getRole(){
		return role;
	}
	
	/**
	 * Get the ID of the player
	 * @return ID
	 */
	public String getID(){
		return playerID;
	}
	
	/**
	 * Add a pawn to the player
	 * @param position of the pawn
	 * @param id of the pawn
	 */
	public void addPawn(Point p, int i){
		pawns.add(new PawnItem(p, i));
	}

	/**
	 * Get a pawn item of the player
	 * @param pawnID
	 * @return pawn
	 */
	public PawnItem getPawnItem(int pawnID) {
		for(PawnItem pawn: pawns){
			if(pawn.id == pawnID)
				return pawn;
		}
		return null;
	}
	
	/**
	 * Set the role of the player
	 * @param role
	 */
	public void setRole(Role role){
		this.role = role;
	}
}

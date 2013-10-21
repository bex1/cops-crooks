package com.dat255.project.android.copsandcrooks.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.utils.Point;

public class PlayerItem implements Serializable{

	private final String name;
	private Role role;
	private final String playerID;
	private final List<PawnItem> pawns = new ArrayList<PawnItem>();
	
	public PlayerItem(String name, String playerID) {
		this.name = name;
		this.playerID = playerID;
		
		//default role
		role = Role.Crook;
	}
	
	public PlayerItem() {
		name = "";
		playerID ="";
	}
	
	public String getName(){
		return name;
	}
	
	public Role getRole(){
		return role;
	}
	
	public String getID(){
		return playerID;
	}
	
	public void addPawn(Point p, int i){
		pawns.add(new PawnItem(p, i));
	}

	public PawnItem getPawnItem(int pawnID) {
		for(PawnItem pawn: pawns){
			if(pawn.id == pawnID)
				return pawn;
		}
		return null;
	}
	
	public void setRole(Role role){
		this.role = role;
	}
}

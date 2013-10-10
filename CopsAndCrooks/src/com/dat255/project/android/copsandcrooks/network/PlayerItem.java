package com.dat255.project.android.copsandcrooks.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.utils.Point;

public class PlayerItem implements Serializable{

	private final String name;
	private final Role role;
	private List<PawnItem> pawns = new ArrayList<PawnItem>();
	
	public PlayerItem(String name, Role role) {
		this.name = name;
		this.role = role;
	}
	
	public PlayerItem() {
		name = "";
		role = null;
	}
	
	public String getName(){
		return name;
	}
	
	public Role getRole(){
		return role;
	}
	
	public void addPawn(Point p, int i){
		pawns.add(new PawnItem(p, i));
	}
}

package com.dat255.project.android.copsandcrooks.network;

import com.dat255.project.android.copsandcrooks.utils.Point;

public class PawnItem {
	
	public final Point position;
	public final int id;

	public PawnItem(Point p, int id) {
		this.position = p;
		this.id = id;
	}
	
	public PawnItem(){
		position = null;
		id = -1;
	}

}

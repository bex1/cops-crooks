package com.dat255.project.android.copsandcrooks.domainmodel.tiles;


import java.awt.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;

public class TravelAgency extends RobbableBuilding {
	
	public TravelAgency(Point position, int value) {
		super(position,value);
	}

	@Override
	public void robBuilding(Crook robber){
		super.robBuilding(robber);
		if(getValue() > 0){
			this.setValue(0);
		}
	}
}

package com.dat255.project.android.copsandcrooks.domainmodel.tiles;


import com.dat255.project.android.copsandcrooks.utils.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;

public class TravelAgency extends RobbableBuilding {

	public TravelAgency(Point position) {
		super(position, 0);
	}

	@Override
	public void interact(IMovable target){
		
	}
	
	@Override
	public void robBuilding(Crook robber){
		if(getValue() > 0){
			this.setValue(0);
			super.robBuilding(robber);
		}
	}
}

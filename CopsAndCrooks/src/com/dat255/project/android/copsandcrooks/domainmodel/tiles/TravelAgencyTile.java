package com.dat255.project.android.copsandcrooks.domainmodel.tiles;


import com.dat255.project.android.copsandcrooks.utils.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;

/**
 * A class representing a travel agency.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class TravelAgencyTile extends RobbableBuildingTile {

	/**
	 * Create a new travel agency tile.
	 * @param position the position
	 */
	public TravelAgencyTile(Point position) {
		super(position, 0);
	}

	@Override
	public void interact(IMovable target){
		robBuilding((Crook)target);
	}
	
	@Override
	public void robBuilding(Crook robber){
		if(getValue() > 0){
			this.setValue(0);
			super.robBuilding(robber);
		}
	}
	
	/**
	 * Adds an amount of cash to the travel agency.
	 * @param amount the amount of cash to be added
	 */
	public void addCash(int amount){
		setValue(getValue() + amount);
	}
}

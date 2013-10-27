package com.dat255.project.android.copsandcrooks.model;


import com.dat255.project.android.copsandcrooks.utils.Point;

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
	TravelAgencyTile(Point position, IMediator mediator) {
		super(position, mediator, 0);
	}

	@Override
	public void interact(IPawn target){
		getRobbedBy((Crook) target);
	}
	
	@Override
	public void getRobbedBy(Crook robber){
		if(getValue() > 0){
			super.getRobbedBy(robber);
			this.setValue(0);
		}
	}
	
	/**
	 * Adds an amount of cash to the travel agency.
	 * @param amount the amount of cash to be added
	 */
	void addCash(int amount){
		setValue(getValue() + amount);
	}
}

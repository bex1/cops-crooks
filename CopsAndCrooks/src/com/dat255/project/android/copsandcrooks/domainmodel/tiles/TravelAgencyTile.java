package com.dat255.project.android.copsandcrooks.domainmodel.tiles;


import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMediator;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.utils.Point;

/**
 * A class representing a travel agency.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class TravelAgencyTile extends RobbableBuildingTile {
	
	private static TravelAgencyTile travelAgencyInstance;
	
	public static TravelAgencyTile getInstance(){
		return travelAgencyInstance;
	}
	
	public static void createTravelAgency(Point position, IMediator mediator){
		if(travelAgencyInstance == null){
			travelAgencyInstance = new TravelAgencyTile(position, mediator);
		}
	}

	/**
	 * Create a new travel agency tile.
	 * @param position the position
	 */
	private TravelAgencyTile(Point position, IMediator mediator) {
		super(position, mediator, 0);
	}

	@Override
	public void interact(IMovable target){
		robBuilding((Crook)target);
	}
	
	@Override
	public void robBuilding(Crook robber){
		if(getValue() > 0){
			super.robBuilding(robber);
			this.setValue(0);
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

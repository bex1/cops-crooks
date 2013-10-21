package com.dat255.project.android.copsandcrooks.domainmodel;

import com.dat255.project.android.copsandcrooks.utils.Point;

/**
 * This class represents all tiles that a crook can rob.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class RobbableBuildingTile extends AbstractWalkableTile implements IInteractiveTile {
	
	private int value;

	/**
	 * Create a new RobbableBuilding with a position and a value.
	 * @param position the building's position
	 * @param value the building's value
	 */
	RobbableBuildingTile(Point position, IMediator mediator, int value) {
		super(position, mediator);
		this.value = value;
		
		pawnTypes.add(PawnType.Crook);
	}

	@Override
	public void interact(IPawn target) {
		if(value > 0 && target instanceof Crook){
			Crook crook = (Crook) target;
			if (crook.getWallet().getCash() == 0) {
				getRobbedBy(crook); 
			}
		}
	}
	
	/**
	 * Determines what happens when the building is robbed.
	 * @param robber the crook that's robbing the building
	 */
	protected void getRobbedBy(Crook robber){
		Wallet robberWallet = robber.getWallet();
		robberWallet.incrementCash(value);
		robber.setWanted(robberWallet.getCash() > 0);
	}
	
	/**
	 * Get the value of this building.
	 * @return the building's value
	 */
	public int getValue(){
		return this.value;
	}
	
	/**
	 * Set the value of this building.
	 * @param value the new value, all values < 0 are considered as equal to 0
	 */
	void setValue(int value){
		if (value < 0) {
			value = 0;
		}
		this.value = value;
	}
}

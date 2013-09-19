package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import java.awt.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;

/**
 * This class represents all tiles that a crook can rob.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class RobbableBuilding extends AbstractTile implements IInteractiveTile {
	
	private int value;

	/**
	 * Create a new RobbableBuilding with a position and a value.
	 * @param position the building's position
	 * @param value the building's value
	 */
	public RobbableBuilding(Point position, int value) {
		super(position);
		this.value = value;
		
		pawnTypes.add(PawnType.Crook);
	}

	@Override
	public void interact(IMovable target) {
		if(value <= 0 && target instanceof Crook){
			robBuilding((Crook)target); //TODO fix this... interaction(?)
		}
	}
	
	/**
	 * Determines what happens when the building is robbed.
	 * @param robber the crook that's robbing the building
	 */
	protected void robBuilding(Crook robber){
		if(value > 0){
			robber.getWallet().incrementCash(value);
		}
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
	 * @param value the new value
	 */
	public void setValue(int value){
		this.value = value;
	}
}

package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import java.awt.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;

public class Hideout extends AbstractTile implements IInteractiveTile {
	
	// TODO Storing the hideout's cash along with the owner.

	public Hideout(Point position) {
		super(position);
	}

	@Override
	public void interact(IMovable target) {
		// TODO Auto-generated method stub
		
	}

}

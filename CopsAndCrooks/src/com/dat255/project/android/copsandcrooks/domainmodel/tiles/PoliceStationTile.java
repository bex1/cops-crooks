package com.dat255.project.android.copsandcrooks.domainmodel.tiles;


import com.dat255.project.android.copsandcrooks.utils.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;

public class PoliceStationTile extends AbstractTile implements IInteractiveTile {

	public PoliceStationTile(Point position) {
		super(position);
		
		pawnTypes.add(PawnType.Officer);
	}

	@Override
	public void interact(IMovable target) {
		
	}

}

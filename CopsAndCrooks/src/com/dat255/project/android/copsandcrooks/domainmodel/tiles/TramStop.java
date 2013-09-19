package com.dat255.project.android.copsandcrooks.domainmodel.tiles;


import java.awt.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;

public class TramStop extends AbstractTile implements IInteractiveTile{
	
	public TramStop(Point position) {
		super(position);
		
		pawnTypes.add(PawnType.Crook);
		pawnTypes.add(PawnType.Car);
		pawnTypes.add(PawnType.Officer);
	}

	@Override
	public void interact(IMovable target) {
		// TODO Auto-generated method stub
		
	}

}

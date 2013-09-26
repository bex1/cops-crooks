package com.dat255.project.android.copsandcrooks.domainmodel.tiles;


import com.dat255.project.android.copsandcrooks.domainmodel.IMediator;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.utils.Point;

public class TramStopTile extends AbstractTile implements IInteractiveTile{
	
	public TramStopTile(Point position, IMediator mediator) {
		super(position, mediator);
		
		pawnTypes.add(PawnType.Crook);
		pawnTypes.add(PawnType.Car);
		pawnTypes.add(PawnType.Officer);
	}

	@Override
	public void interact(IMovable target) {
		// TODO Auto-generated method stub
	}

}

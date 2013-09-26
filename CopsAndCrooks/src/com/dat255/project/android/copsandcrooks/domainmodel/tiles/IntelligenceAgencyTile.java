package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import java.util.ArrayList;

import com.dat255.project.android.copsandcrooks.utils.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMediator;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;

public class IntelligenceAgencyTile extends AbstractTile implements IInteractiveTile {

	public IntelligenceAgencyTile(Point position, IMediator mediator) {
		super(position, mediator);

		pawnTypes.add(PawnType.Officer);
	}
	
	@Override
	public void interact(IMovable target) {
		hinderGetAway();
	}
	
	private void hinderGetAway(){
		//TODO access the crooks that are escaping and stop them
		ArrayList<Crook> crooks = new ArrayList<Crook>();
		for(Crook crook: crooks){
			if(crook.isAttemptingGetAway()){
				crook.setAttemptingGetAway(false);
			}
		}
	}
}
package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.Mediator;
import com.dat255.project.android.copsandcrooks.utils.Point;

public class TramStopTileTest {

	@Test
	public void test() {
		Crook crook = new Crook(new Mediator());
		TramStopTile tramStopTile = new TramStopTile(new Point(2,2), new Mediator());
		
		crook.standingOnTramstop(false);
		tramStopTile.interact(crook);
		assertTrue(crook.isWaitingOnTram());
	}

}

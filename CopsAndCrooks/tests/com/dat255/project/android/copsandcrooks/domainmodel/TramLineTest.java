package com.dat255.project.android.copsandcrooks.domainmodel;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.utils.Point;

public class TramLineTest {

	@Test
	public void test() {
		Mediator mediator = new Mediator();
		List<TramStopTile> tramStops = new ArrayList<TramStopTile>();
		TramStopTile occupiedStop = new TramStopTile(new Point(3,3), mediator);
		tramStops.add(occupiedStop);
		tramStops.add(new TramStopTile(new Point(0,0), mediator));
		tramStops.add(new TramStopTile(new Point(1,1), mediator));
		tramStops.add(new TramStopTile(new Point(2,2), mediator));
		TramLine tramLine = new TramLine(tramStops);
		
		assertTrue(tramLine.getPossibleStops().size() == 4);
		occupiedStop.setOccupiedBy(PawnType.Crook);
		assertTrue(tramLine.getPossibleStops().size() == 3);
		
	}

}

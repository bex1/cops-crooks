package com.dat255.project.android.copsandcrooks.model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.dat255.project.android.copsandcrooks.model.Mediator;
import com.dat255.project.android.copsandcrooks.model.MetroLine;
import com.dat255.project.android.copsandcrooks.model.MetroStopTile;
import com.dat255.project.android.copsandcrooks.model.PawnType;
import com.dat255.project.android.copsandcrooks.utils.Point;

public class TramLineTest {

	@Test
	public void test() {
		Mediator mediator = new Mediator();
		List<MetroStopTile> tramStops = new ArrayList<MetroStopTile>();
		MetroStopTile occupiedStop = new MetroStopTile(new Point(3,3), mediator);
		tramStops.add(occupiedStop);
		tramStops.add(new MetroStopTile(new Point(0,0), mediator));
		tramStops.add(new MetroStopTile(new Point(1,1), mediator));
		tramStops.add(new MetroStopTile(new Point(2,2), mediator));
		MetroLine metroLine = new MetroLine(tramStops);
		
		assertTrue(metroLine.getPossibleStops().getPathLength() == 4);
		occupiedStop.setOccupiedBy(PawnType.Crook);
		assertTrue(metroLine.getPossibleStops().getPathLength() == 3);
		
	}

}

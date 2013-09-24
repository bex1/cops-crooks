package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.ArrayList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.TramStopTile;

public class TramLine {
	
	private List<IWalkableTile> tramLine;

	public TramLine(List<IWalkableTile> tramLine){
		this.tramLine = tramLine;
	}
	
	public List<IWalkableTile> getTramStops(){
		List<IWalkableTile> tramStops = new ArrayList<IWalkableTile>();;
		for(IWalkableTile stops: tramLine){
			if(stops instanceof TramStopTile){
				tramStops.add(stops);
			}
		}
		return tramStops;
	}
	
}

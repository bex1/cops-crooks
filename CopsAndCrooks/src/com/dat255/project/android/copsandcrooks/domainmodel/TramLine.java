package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.ArrayList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.TramStopTile;

/**
 * A class representing a tramline.
 * @author Grupp 25, course DAT255 at Chalmers Uni.
 *
 */
public class TramLine {
	
	private List<TramStopTile> tramLine;

	public TramLine(List<TramStopTile> tramLine){
		this.tramLine = tramLine;
	}
	
	/**
	 * Calculates where you can travel to.
	 * @return a list of TilePaths that one can travel to.
	 */
	public List<TilePath> getPossibleStops(){
		List<TilePath> possibleStops = new ArrayList<TilePath>();
		for(TramStopTile stopTile: tramLine){
			if(!stopTile.isOccupied()){
				TilePath path = new TilePath();
				path.addTile(stopTile);
				possibleStops.add(path);
			}
		}
		return possibleStops;
	}
	
}

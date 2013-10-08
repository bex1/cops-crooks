package com.dat255.project.android.copsandcrooks.domainmodel;

import java.io.Serializable;
import java.util.List;


/**
 * A class representing a tramline.
 * @author Grupp 25, course DAT255 at Chalmers Uni.
 *
 */
public class TramLine implements Serializable{
	
	private List<TramStopTile> tramLine;

	public TramLine(List<TramStopTile> tramLine){
		this.tramLine = tramLine;
	}
	
	boolean contains(AbstractWalkableTile tile) {
		return tramLine.contains(tile);
	}
	
	/**
	 * Calculates where you can travel to.
	 * @return a list of TilePaths that one can travel to.
	 */
	TilePath getPossibleStops(){
		TilePath path = new TilePath();
		for(TramStopTile stopTile: tramLine){
			if(!stopTile.isOccupied()){
				path.addTileLast(stopTile);
			}
		}
		return path;
	}
	
	List<TramStopTile> getTramStops(){
		return tramLine;
	}
	
}

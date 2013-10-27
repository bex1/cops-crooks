package com.dat255.project.android.copsandcrooks.model;

import java.io.Serializable;
import java.util.List;


/**
 * A class representing a tramline.
 * @author Grupp 25, course DAT255 at Chalmers Uni.
 *
 */
public class MetroLine implements Serializable {
	
	private static final long serialVersionUID = 2268515654782127L;
	
	private List<MetroStopTile> tramLine;

	MetroLine(List<MetroStopTile> tramLine){
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
		for(MetroStopTile stopTile: tramLine){
			if(!stopTile.isOccupied()){
				path.addTileLast(stopTile);
			}
		}
		return path;
	}
	
	List<MetroStopTile> getTramStops(){
		return tramLine;
	}
	
}

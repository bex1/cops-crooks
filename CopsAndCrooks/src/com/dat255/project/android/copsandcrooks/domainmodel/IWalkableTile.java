package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Map;

import com.dat255.project.android.copsandcrooks.utils.IObservable;
import com.dat255.project.android.copsandcrooks.utils.Point;



/**
 * This class represents a walkable tile.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public interface IWalkableTile extends IObservable{
	
	/**
	 * Get the position of this tile
	 * @return the position of this tile
	 */
	public Point getPosition();
}

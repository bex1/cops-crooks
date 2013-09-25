package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.utils.IObservable;

/**
 * Represents a path along tiles in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class TilePath implements IObservable {

	private LinkedList<IWalkableTile> pathList;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public TilePath() {
		pathList = new LinkedList<IWalkableTile>();
	}
	
	/**
	 * Returns and consumes the next tile on the path.
	 * @return the next tile on path.
	 */
	public IWalkableTile consumeNextTile() {
		return pathList.pollLast();
	}
	
	/**
	 * Returns the next tile on the path.
	 * @return the next tile on path.
	 */
	public IWalkableTile getTile(int i) {
		return pathList.get(i);
	}
	
	/**
	 * Checks to see if the path is empty.
	 * @return true if the path list is empty.
	 */
	public boolean isEmpty() {
		return pathList.isEmpty();
	}
	/**
	 * Add a tile to the path.
	 * @param tile - the tile to be added to the path.
	 */
	public void addTile(IWalkableTile tile){
		pathList.addLast(tile);
	}
	/**
	 * Returns the size of the path.
	 * @return the size of the path.
	 */
	public int getPathSize(){
		return pathList.size();
	}
	
	@Override
	public void addObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	@Override
	public void removeObserver(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}
}

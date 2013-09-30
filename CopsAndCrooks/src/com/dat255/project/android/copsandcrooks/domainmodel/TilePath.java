package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import com.dat255.project.android.copsandcrooks.utils.IObservable;

/**
 * Represents a path along tiles in the game Cops&Crooks.
 * 
 * Uses a LinkedList and acts as a stack. 
 * Where the next tile is in the last position of the list.
 * 
 * Tiles are added to the path in the reverse order, ie the last tile
 * is added first.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public final class TilePath implements IObservable {

	private final LinkedList<AbstractWalkableTile> pathList;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public TilePath() {
		pathList = new LinkedList<AbstractWalkableTile>();
	}
	
	/**
	 * Returns and consumes the next tile on the path.
	 * @return the next tile on path.
	 */
	synchronized AbstractWalkableTile consumeNextTile() {
		return pathList.pollLast();
	}
	
	/**
	 * Returns the tile with the specified index.
	 * @return the specified tile.
	 */
	public AbstractWalkableTile getTile(int i) {
		return pathList.get(i);
	}
	
	/**
	 * Checks to see if the path is empty.
	 * @return true if the path list is empty.
	 */
	boolean isEmpty() {
		return pathList.isEmpty();
	}
	/**
	 * Add a tile to the path.
	 * @param tile - the tile to be added to the path.
	 */
	synchronized void addTileLast(AbstractWalkableTile tile){
		pathList.addLast(tile);
	}
	/**
	 * Returns the size of the path.
	 * @return the size of the path.
	 */
	public int getPathLength(){
		return pathList.size();
	}
	/**
	 * Checks to see if the list already contains a tile.
	 * @param tile
	 * @return true if the tile already is in the list
	 */
	synchronized boolean contains(AbstractWalkableTile tile){
		return pathList.contains(tile);
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

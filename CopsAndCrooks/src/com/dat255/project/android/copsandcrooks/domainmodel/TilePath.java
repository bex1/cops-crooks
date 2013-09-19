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
	
	public TilePath(LinkedList<IWalkableTile> path) {
		pathList = path;
	}
	
	public IWalkableTile getNextTile() {
		return pathList.pollFirst();
	}
	
	public boolean isEmpty() {
		return pathList.isEmpty();
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

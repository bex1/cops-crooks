package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.utils.Point;


/**
 * This class represents all tiles with a position 
 * and stores whether it's occupied or not.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public abstract class AbstractWalkableTile implements IWalkableTile{

	private PawnType occupiedBy;
	private Point position;
	protected List<PawnType> pawnTypes;
	protected IMediator mediator;
	
	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	/**
	 * Construct a new AbstractTile with a position.
	 * @param position the tile's position
	 */
	protected AbstractWalkableTile(Point position, IMediator mediator) {
		if (position == null)
			throw new IllegalArgumentException("Position not allowed to be null");
		if (mediator == null)
			throw new IllegalArgumentException("Mediator not allowed to be null");
		
		this.position = position;
		this.mediator = mediator;
		
		pawnTypes = new ArrayList<PawnType>();
	}

	/**
	 * Checks if the tile is occupied by an IMovable object
	 * @return true if this tile is occupied
	 */
	boolean isOccupied(){
		return this.occupiedBy!=null;
	}

	/**
	 * Set the occupied-status of this tile
	 */
	void setOccupiedBy(PawnType pawnType) {
		this.occupiedBy = pawnType;
	}

	/**
	 * Set the occupied-status of this tile to none
	 */
	void setNotOccupied(){
		this.occupiedBy = null;
	}

	/**
	 * Get the occupied-status of this tile
	 */
	PawnType getOccupiedBy(){
		return occupiedBy;
	}
	
	@Override
	public Point getPosition(){
		return this.position;
	}
	
	/**
	 * Get the allowed pawntypes for this tile
	 * @return the allowed pawntypes
	 */
	Collection<PawnType> getAllowedPawnTypes() {
		return Collections.unmodifiableCollection(pawnTypes);
	}
	
	@Override
	public void addObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	@Override
	public void removeObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}
	
}

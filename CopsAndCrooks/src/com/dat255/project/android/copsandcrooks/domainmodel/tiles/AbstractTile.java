package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.IMediator;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.utils.Point;


/**
 * This class represents all tiles with a position 
 * and stores whether it's occupied or not.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
//TODO: CHANGE NAME TO AbstractWalkableTile
public abstract class AbstractTile implements IWalkableTile{

	private PawnType occupiedBy;
	private Point position;
	protected List<PawnType> pawnTypes;
	protected IMediator mediator;
	
	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	/**
	 * Construct a new AbstractTile with a position.
	 * @param position2 the tile's position
	 */
	public AbstractTile(Point pos, IMediator mediator) {
		if (pos == null) 
			throw new IllegalArgumentException("Position not allowed to be null");
		if (mediator == null)
			throw new IllegalArgumentException("Mediator not allowed to be null");
		
		this.position = pos;
		this.mediator = mediator;
		
		pawnTypes = new ArrayList<PawnType>();
	}

	@Override
	public boolean isOccupied(){
		return this.occupiedBy!=null;
	}

	@Override
	public void setOccupiedBy(PawnType pawnType) {
		this.occupiedBy = pawnType;
	}

	@Override
	public void setNotOccupied(){
		this.occupiedBy = null;
	}

	@Override
	public PawnType getOccupiedBy(){
		return occupiedBy;
	}
	
	@Override
	public Point getPosition(){
		return this.position;
	}
	
	@Override
	public void setPosition(Point position){
		this.position = position;
	}

	@Override
	public Collection<PawnType> getAllowedPawnTypes() {
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

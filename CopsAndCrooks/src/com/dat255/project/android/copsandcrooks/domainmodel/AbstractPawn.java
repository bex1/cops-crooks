package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.badlogic.gdx.Gdx;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IInteractiveTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.utils.Values;
import com.dat255.project.android.copsandcrooks.utils.Point;

/**
 * This class represents an abstract pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public abstract class AbstractPawn implements IMovable {
	
	private Role pawnRole;
	private PawnType pawnType;
	
	// Used to communicate within the module
	protected final IMediator mediator;
	
	protected IWalkableTile currentTile;
	protected IWalkableTile nextTile;
	protected Direction direction;
	private int tilesMovedEachStep;
	
	// TODO likely add a previous tile field so we know which tile we should animate the player move from
	private TilePath pathToMove;
	
	private boolean isMoving;
	private float moveTimer;
	
	private boolean isWaitingOnTram= false;
	
	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	
	protected AbstractPawn(Role pawnRole, PawnType pawnType, IMediator mediator, int tilesMovedEachStep) {
		if (mediator == null) {
			throw new IllegalArgumentException("mediator not allowed to be null");
		}
		this.pawnRole = pawnRole;
		this.pawnType = pawnType;
		this.mediator = mediator;
		this.direction = Direction.SOUTH;
		this.tilesMovedEachStep = tilesMovedEachStep;
	}

	@Override
	public void setCurrentTile(IWalkableTile currTile) {
		IWalkableTile oldTile = currentTile;
		this.currentTile = currTile;
		
		 // The current tiled changed, someone moved us -> notify
        pcs.firePropertyChange(PROPERTY_CURRENT_TILE, oldTile, currentTile);
		
	}
	
	@Override
	public boolean isWaitingOnTram(){
		return isWaitingOnTram;
	}
	
	@Override
	public void standingOnTramstop(boolean standing){
		isWaitingOnTram = standing;
	}
	
	@Override
	public IWalkableTile getCurrentTile() {
		return currentTile;
	}
	
	private void setNextTile(IWalkableTile nextTile) {
		IWalkableTile oldTile = this.nextTile;
		this.nextTile = nextTile;
		
        pcs.firePropertyChange(PROPERTY_NEXT_TILE, oldTile, nextTile);
	}

	@Override
	public IWalkableTile getNextTile() {
		return nextTile;
	}

	@Override
	public void move(TilePath path) {
		if (path == null || path.isEmpty()) {
			throw new IllegalArgumentException("path is null or empty");
		}
		this.pathToMove = path;
		IWalkableTile next = pathToMove.consumeNextTile();
		updateDirection(currentTile, next);
		this.isMoving = true;
		setNextTile(next);
	}

	@Override
	public void update(float deltaTime) {
		// Check if we are walking
		if (isMoving) {
			// Take steps with delay
			moveTimer += deltaTime;
		    if (moveTimer >= Values.PAWN_MOVE_DELAY) {
		    	
		        // Check if we stepped on the endtile of the path
		        if (pathToMove.isEmpty()) {
		        	isMoving = false;
		        	this.setCurrentTile(nextTile);
		        	nextTile = null;
		        	
		        	
		        	if (currentTile != null && currentTile.isOccupied()) {
		        		// We collided, communicate with the module via the mediator	
		        		mediator.didCollideAfterMove(this);
		        	}
		        	// Try to interact with the tile
		        	this.interactWithTile();
		        	
		        } else {
		        	this.setCurrentTile(nextTile);
		        	IWalkableTile next = pathToMove.consumeNextTile();
		        	updateDirection(currentTile, next);
		        	this.setNextTile(next);
		        }
		        // Reset timer
		        moveTimer -= Values.PAWN_MOVE_DELAY;
		    }
		}
	}
	
	private void updateDirection(IWalkableTile current, IWalkableTile next) {
		Point currentPos = current.getPosition();
		Point nextPos = next.getPosition();
		int deltaX = nextPos.x - currentPos.x;
		int deltaY = nextPos.y - currentPos.y;
		setDirection(deltaY < 0 ? Direction.SOUTH : deltaY > 0 ? Direction.NORTH : 
									deltaX > 0 ? Direction.EAST : Direction.WEST);
	}

	private void interactWithTile() {
		// Check if the endTile is an interactive tile
		if (currentTile instanceof IInteractiveTile) {
			IInteractiveTile interactableTile = (IInteractiveTile) currentTile;
			// Lets interact with it
		    interactableTile.interact(this);
		}
	}
	
	@Override
	public Role getPawnRole() {
		return pawnRole;
	}

	@Override
	public PawnType getPawnType() {
		return pawnType;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	private void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	@Override
	public boolean isMoving() {
		return isMoving;
	}
	
	@Override
	public int tilesMovedEachStep() {
		return tilesMovedEachStep;
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

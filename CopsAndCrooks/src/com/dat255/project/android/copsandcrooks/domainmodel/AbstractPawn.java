package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;

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
	
	protected AbstractWalkableTile currentTile;
	protected AbstractWalkableTile nextTile;
	protected Direction direction;
	private int tilesMovedEachStep;
	private final int id;
	
	// TODO likely add a previous tile field so we know which tile we should animate the player move from
	private TilePath pathToMove;
	
	private boolean isMoving, isPlaying, isActivePawn;
	private float moveTimer;
	
	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	protected AbstractPawn(AbstractWalkableTile startTile, Role pawnRole, PawnType pawnType, IMediator mediator, int tilesMovedEachStep, int id) {		if (mediator == null) {
			throw new IllegalArgumentException("mediator not allowed to be null");
		}
		this.currentTile = startTile;
		if(startTile!=null)
			startTile.setOccupiedBy(pawnType);
		this.pawnRole = pawnRole;
		this.pawnType = pawnType;
		this.mediator = mediator;
		this.direction = Direction.SOUTH;
		this.tilesMovedEachStep = tilesMovedEachStep;

		this.id = id;
		isPlaying = true;
	}

	/**
	 * Sets the current tile on which the movable is standing on.
	 *
	 * @param currTile the current tile on which the movable is standing on.
	 * Allowed to be null to move the pawn out of the game.
	 */
	protected void setCurrentTile(AbstractWalkableTile currTile) {
		if(currentTile!=null)
			currentTile.setNotOccupied();
		AbstractWalkableTile oldTile = currentTile;
		this.currentTile = currTile;
		if(currentTile!=null)
			currentTile.setOccupiedBy(pawnType);

		// The current tiled changed, someone moved us -> notify
		pcs.firePropertyChange(PROPERTY_CURRENT_TILE, oldTile, currentTile);
	}

	@Override
	public AbstractWalkableTile getCurrentTile() {
		return currentTile;
	}
	
	private void setNextTile(AbstractWalkableTile nextTile) {
		AbstractWalkableTile oldTile = this.nextTile;
		this.nextTile = nextTile;
		
        pcs.firePropertyChange(PROPERTY_NEXT_TILE, oldTile, nextTile);
	}

	@Override
	public AbstractWalkableTile getNextTile() {
		return nextTile;
	}

	/**
	 * Moves the movable object along the given path.
	 * @param path the path of walkable tiles in the path. Not allowed to be null or empty.
	 */
	protected void move(TilePath path) {
		if (path == null || path.isEmpty()) {
			throw new IllegalArgumentException("path is null or empty");
		}
		this.pathToMove = path;
		AbstractWalkableTile next = pathToMove.consumeNextTile();
		updateDirection(currentTile, next);
		currentTile.setNotOccupied();
		this.setMoving(true);
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
		        	currentTile = nextTile;
		        	this.setMoving(false);
		        	nextTile = null;
		        	
		        	if (currentTile != null && currentTile.isOccupied()) {
		        		// We collided, communicate with the module via the mediator	
		        		mediator.didCollideAfterMove(this);
		        		mediator.playerTurnDone(3f);
		        		return;
		        	}
		        	if (!(currentTile instanceof HideoutTile)) {
		        		currentTile.setOccupiedBy(pawnType);
		        		mediator.playerTurnDone(2f);
		        	}
		        	
		        	// Try to interact with the tile
		        	this.interactWithTile();
		        	
		        } else {
		        	currentTile = nextTile;
		        	AbstractWalkableTile next = pathToMove.consumeNextTile();
		        	updateDirection(currentTile, next);
		        	this.setNextTile(next);
		        }
		        // Reset timer
		        moveTimer -= Values.PAWN_MOVE_DELAY;
		    }
		}
	}
	
	private void setMoving(boolean moving) {
		boolean oldValue = isMoving;
		isMoving = moving;
		pcs.firePropertyChange(PROPERTY_IS_MOVING, oldValue, isMoving);
	}

	private void updateDirection(AbstractWalkableTile current, AbstractWalkableTile next) {
		Point currentPos = current.getPosition();
		Point nextPos = next.getPosition();
		int deltaX = nextPos.x - currentPos.x;
		int deltaY = nextPos.y - currentPos.y;
		setDirection(deltaY < 0 ? Direction.SOUTH : deltaY > 0 ? Direction.NORTH : 
									deltaX > 0 ? Direction.EAST : Direction.WEST);
	}

	protected void interactWithTile() {
		// Check if the endTile is an interactive tile
		if (currentTile instanceof IInteractiveTile) {
			IInteractiveTile interactableTile = (IInteractiveTile) currentTile;
			// Lets interact with it
		    interactableTile.interact(this);
		}
	}
	
	/**
	 * Returns the role of this pawn.
	 * @return the role of this pawn.
	 */
	Role getPawnRole() {
		return pawnRole;
	}

	/**
	 * Returns the type of this pawn.
	 * @return the type of this pawn.
	 */
	PawnType getPawnType() {
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
	
	void setIsntPlaying() {
		this.isPlaying = false;
		this.isActivePawn = false;
		pcs.firePropertyChange(PROPERTY_IS_PLAYING, null, isPlaying);
		currentTile = null;
	}
	
	@Override
	public boolean isPlaying() {
		return isPlaying;
	}
	
	void setIsActivePawn(boolean isActive) {
		this.isActivePawn = isActive;
		pcs.firePropertyChange(PROPERTY_IS_ACTIVE_PAWN, null, isActive);
	}
	
	@Override
	public boolean isActivePawn() {
		return isActivePawn;
	}
	
	/**
	 * Returns the number of tiles the pawn can move over in one step.
	 * @return the number of tiles the pawn can move over in one step.
	 */
	int tilesMovedEachStep() {
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

	@Override
	public int getID(){
		return id;
	}

	/**
	 * Alerts the IMovable that it has collided with another IMovable after it has moved.
	 * @param pawn the IMovable pawn that collided with this one.
	 */
	protected abstract void collisionAfterMove(IMovable pawn);

	protected void moveByTram(TramStopTile metroStop) {
		currentTile.setNotOccupied();
		currentTile = metroStop;
		this.interactWithTile();
		currentTile.setOccupiedBy(getPawnType());
		
		pcs.firePropertyChange(PROPERTY_CURRENT_TILE, null, currentTile);
	}
}

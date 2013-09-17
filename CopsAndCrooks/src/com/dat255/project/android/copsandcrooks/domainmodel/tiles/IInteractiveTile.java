package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;

public interface IInteractiveTile extends IWalkableTile {
	
	/**
	 * Allows an IMovable and an IInteractiveTile to interact.
	 * 
	 * @param target the movable to target.
	 */
	void interact(IMovable target);
}

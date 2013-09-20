package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;

/**
 * This class represents all tiles that an IMovable object can interact with.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public interface IInteractiveTile{
	
	/**
	 * Allows an IMovable and an IInteractiveTile to interact.
	 * 
	 * @param target the movable to target.
	 */
	void interact(IMovable target);
}

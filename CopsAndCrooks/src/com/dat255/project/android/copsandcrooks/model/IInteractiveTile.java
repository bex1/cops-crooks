package com.dat255.project.android.copsandcrooks.model;


/**
 * This class represents all tiles that an IPawn object can interact with.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
interface IInteractiveTile extends IWalkableTile {

	/**
	 * Allows an IPawn and an IInteractiveTile to interact.
	 * 
	 * @param target the movable to target.
	 */
	void interact(IPawn target);
}

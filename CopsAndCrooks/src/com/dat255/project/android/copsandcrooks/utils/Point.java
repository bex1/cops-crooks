package com.dat255.project.android.copsandcrooks.utils;

import java.io.Serializable;

/**
 * A simple integer based 2-d coordinate point.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public final class Point implements Serializable{
	
	private static final long serialVersionUID = 188533447833369527L;
	
	public int x, y;
	
	public Point() {
		this(0,0);
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

package com.dat255.project.android.copsandcrooks.utils;

import java.io.Serializable;

public final class Point implements Serializable{
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

package com.dat255.project.android.copsandcrooks;

public class GameItem {

	private String name;
	
	public GameItem(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public String toString(){
		return getName();
	}

}

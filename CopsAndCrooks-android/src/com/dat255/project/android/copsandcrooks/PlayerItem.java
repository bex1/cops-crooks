package com.dat255.project.android.copsandcrooks;

import java.io.Serializable;

public class PlayerItem implements Serializable{

	private String name;
	
	public PlayerItem(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

}

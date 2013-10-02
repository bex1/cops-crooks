package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.Map;

import com.dat255.project.android.copsandcrooks.utils.Point;

public class GhostPlayer {
	
	public final String name;
	public final int score;
	public final Role role;
	public final Map<Point, Integer> pawn;
	
	public GhostPlayer(String name, int score, Role role, Map<Point, Integer> pawn){
		this.name = name;
		this.score = score;
		this.role = role;
		this.pawn = pawn;
	}

}

package com.dat255.project.android.copsandcrooks.domainmodel;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.dat255.project.android.copsandcrooks.utils.Point;

public class PathFinderTest {
	
	private AbstractPawn crook;
	private IMediator mediator;
	private PathFinder pathFinder;
	private Collection<TilePath> pathList;

	@Test
	public void testCalculatePossiblePaths1() {
		mediator = new Mediator();
		
		AbstractWalkableTile[][] tiles = {{new RoadTile(new Point(0,0), new Mediator()), null, new RoadTile(new Point(0,2), new Mediator())}, 
									{new RoadTile(new Point(1,0), new Mediator()), null, new RoadTile(new Point(1,2), new Mediator())}, 
									{new RoadTile(new Point(2,0), new Mediator()), new RoadTile(new Point(2,1), new Mediator()), new RoadTile(new Point(2,2), new Mediator())}};

		
		crook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), mediator, 0);
		crook.setCurrentTile(tiles[0][0]);
		
		pathFinder = new PathFinder(tiles, mediator, new ArrayList<TramLine>());
		
		pathList = pathFinder.calculatePossiblePaths(crook, 6);
		
		assertTrue(pathList.size() == 1);
	}
	
	@Test
	public void testCalculatePossiblePaths2() {
		mediator = new Mediator();
		
		AbstractWalkableTile[][] tiles;

		tiles = new AbstractWalkableTile[3][3];
		for(int i =0; i<3; i++){
			for(int j =0; j<3; j++){
				tiles[i][j] = new RoadTile(new Point(i,j), new Mediator());
			}
		}
		
		
		crook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), mediator, 0);
		crook.setCurrentTile(tiles[0][0]);
		
		pathFinder = new PathFinder(tiles, mediator, new ArrayList<TramLine>());
		
		pathList = pathFinder.calculatePossiblePaths(crook, 3);
		
		assertTrue(pathList.size() == 8);
	}
	
	@Test
	public void testCalculatePossiblePaths3() {
		mediator = new Mediator();
		
		AbstractWalkableTile[][] tiles = {{new RoadTile(new Point(0,0), new Mediator()), 
									null, new RoadTile(new Point(0,2), new Mediator())}, 
									{new RoadTile(new Point(1,0), new Mediator()), 
									null, new RoadTile(new Point(1,2), new Mediator())},
									{new RoadTile(new Point(2,0), new Mediator()), 
									null, new RoadTile(new Point(2,2), new Mediator())}, 
									{new RoadTile(new Point(3,0), new Mediator()), 
									null, new RoadTile(new Point(3,2), new Mediator())}, 
									{new RoadTile(new Point(4,0), new Mediator()), 
									null, new RoadTile(new Point(4,2), new Mediator())}, 
									{new RoadTile(new Point(5,0), new Mediator()), 
									null, new RoadTile(new Point(5,2), new Mediator())}, 
									{new RoadTile(new Point(6,0), new Mediator()), 
									new RoadTile(new Point(6,1), new Mediator()), 
									new RoadTile(new Point(6,2), new Mediator())}};

		
		crook = new Crook(new RoadTile(new Point(0, 0), new Mediator()), mediator, 0);
		crook.setCurrentTile(tiles[0][0]);
		
		pathFinder = new PathFinder(tiles, mediator, new ArrayList<TramLine>());
		
		pathList = pathFinder.calculatePossiblePaths(crook, 14);
		
		assertTrue(pathList.size() == 1);
	}
	
	@Test
	public void testCalculatePossiblePaths4() {
		mediator = new Mediator();
		
		AbstractWalkableTile[][] tiles;

		tiles = new AbstractWalkableTile[3][3];
		for(int i =0; i<3; i++){
			for(int j =0; j<3; j++){
				tiles[i][j] = new RoadTile(new Point(i,j), new Mediator());
			}
		}
		
		
		crook = new Crook(null, mediator, 0);
		// crook.setCurrentTile(tiles[0][0]); this should force the calculate method to return null
		
		pathFinder = new PathFinder(tiles, mediator, new ArrayList<TramLine>());
		
		pathList = pathFinder.calculatePossiblePaths(crook, 3);
		
		assertTrue(pathList == null);
	}
}

package com.dat255.project.android.copsandcrooks.domainmodel;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.List;

import org.junit.Test;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.RoadTile;
import org.junit.Test;

public class PathFinderTest {
	
	private IWalkableTile[][] tiles;
	private IMovable crook;
	private IMediator mediator;
	private PathFinder pathFinder;
	private List<TilePath> pathList;

	@Test
	public void test() {
		mediator = new Mediator();
		
		tiles = new IWalkableTile[3][3];
		for(int i =0; i<3; i++){
			for(int j =0; j<3; j++){
				tiles[i][j] = new RoadTile(new Point(i,j));
			}
		}
		
		crook = new Crook(mediator);
		crook.setCurrentTile(tiles[0][0]);
		
		pathFinder = new PathFinder(tiles, mediator);
		
		pathList = pathFinder.calculatePossiblePaths(crook, 2);
		IWalkableTile[][] tiles = new IWalkableTile[3][3];
		
			System.out.println(pathList.size());
		for(TilePath path: pathList){
			System.out.println(path.getPathSize());
			while(!path.isEmpty()){
				System.out.println(path.getNextTile().getPosition());
			}
		}
			assertTrue(pathList.size() == 2);
	}

}

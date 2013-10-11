package com.dat255.project.android.copsandcrooks;

import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.screens.Assets;
import com.dat255.project.android.copsandcrooks.screens.LoadingScreen;
import com.dat255.project.android.copsandcrooks.screens.MenuScreen;


/**
 * TODO docs here
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class CopsAndCrooks extends Game {
	// constant useful for logging
    public static final String LOG = CopsAndCrooks.class.getSimpleName();

    // whether we are in development mode
    public static final boolean DEV_MODE = true;

    // a libgdx helper class that logs the current FPS each second
    private FPSLogger fpsLogger;
    private Assets assets;
    private GameModel game;
	
    public CopsAndCrooks(){
    	this(null);
    }
    
    public CopsAndCrooks(GameModel game){
    	super();
    	this.game = game;
    }
    
    @Override
    public void create()
    {
        Gdx.app.log( CopsAndCrooks.LOG, "Creating game on " + Gdx.app.getType() );
        
//      Gdx.app.log(CopsAndCrooks.LOG, "Creating and connecting network client");
//		GameClient.getInstance().connectToServer();
        
        GameFactory.getInstance().init(new Assets());
        fpsLogger = new FPSLogger();
        assets = GameFactory.getInstance().getAssets();
        if (game != null) {
        	setScreen(GameFactory.getInstance().getGameScreenFor(game, this));
        } else {
        	setScreen(new LoadingScreen(assets, this));
        }
    }

    @Override
    public void resize(int width, int height )
    {
        super.resize( width, height );
        Gdx.app.log( CopsAndCrooks.LOG, "Resizing game to: " + width + " x " + height );
    }

    @Override
    public void render()
    {
        super.render();
        // output the current FPS if in dev mode
        //if( DEV_MODE ) 
        //	fpsLogger.log();
    }

    @Override
    public void pause()
    {
        super.pause();
        Gdx.app.log( CopsAndCrooks.LOG, "Pausing game" );
    }

    @Override
    public void resume()
    {
        super.resume();
        Gdx.app.log( CopsAndCrooks.LOG, "Resuming game" );
    }

    @Override
    public void setScreen(Screen screen )
    {
        super.setScreen( screen );
        Gdx.app.log( CopsAndCrooks.LOG, "Setting screen: " + screen.getClass().getSimpleName() );
    }

    @Override
    public void dispose()
    {
        super.dispose();
        Gdx.app.log( CopsAndCrooks.LOG, "Disposing game" );
    }

	public GameModel getModel() {
		return game;
	}
}

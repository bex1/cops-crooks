package com.dat255.project.android.copsandcrooks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.dat255.project.android.copsandcrooks.screens.SplashScreen;
import com.dat255.project.android.copsandcrooks.utils.Utilities;

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
	
    @Override
    public void create()
    {
        Gdx.app.log( CopsAndCrooks.LOG, "Creating game on " + Gdx.app.getType() );
        fpsLogger = new FPSLogger();
        setScreen(new SplashScreen(this));
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
        if( DEV_MODE ) 
        	fpsLogger.log();
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
        Utilities.disposeUtils();
    }
}

package com.dat255.project.android.copsandcrooks.view;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.dat255.project.android.copsandcrooks.model.GameModel;
import com.dat255.project.android.copsandcrooks.model.ModelFactory;
import com.dat255.project.android.copsandcrooks.model.Role;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;
import com.dat255.project.android.copsandcrooks.utils.MusicManager;
import com.dat255.project.android.copsandcrooks.utils.PreferencesManager;
import com.dat255.project.android.copsandcrooks.utils.SoundManager;


/**
 * Cops and crooks is an active boardgame developed for android.
 * 
 * It is mostly based on the swedish boardgame "Tjuv & Polis".
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
@SuppressWarnings("unused")
public class CopsAndCrooks extends Game {
	// constant useful for logging
    public static final String LOG = CopsAndCrooks.class.getSimpleName();

    // whether we are in development mode
    public static final boolean DEV_MODE = false;

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

        PreferencesManager prefs = PreferencesManager.getInstance();
        
        // create the music manager
        MusicManager musicManager = MusicManager.getInstance();
        musicManager.setVolume( prefs.getVolume() );

        // create the sound manager
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.setVolume( prefs.getVolume() );
        soundManager.setEnabled( prefs.isSoundEnabled() );

        assets = new Assets();
        ViewFactory.getInstance().init(assets);
        fpsLogger = new FPSLogger();
        if (game != null) {
        	setScreen(ViewFactory.getInstance().loadGameScreen(game, this));
        } else {
        	GameItem gameToPlay = GameClient.getInstance().getChosenGameItem();
    		ViewFactory factory = ViewFactory.getInstance();
    		ModelFactory modelFactory = ModelFactory.getInstance();
    		if(!gameToPlay.hasGameStarted()){
    			// Clients hosts a game
    			game = modelFactory.loadGameModel(gameToPlay, factory.getInteract(), false);
    		}else if(gameToPlay.hasGameStarted() && !modelFactory.hasLoadedThisGameModel(gameToPlay)){
    			// Client joins a hosted game
    			game = modelFactory.loadGameModel(gameToPlay, factory.getInteract(), true);
    		}else if(gameToPlay.hasGameStarted() && modelFactory.hasLoadedThisGameModel(gameToPlay)){
    			// Localy loaded game
    			game = modelFactory.loadModelFromFile(gameToPlay.getName());
    		}else{
    			assert false;
    			game = null;
    		}
    		GameClient.getInstance().setCurrentGameModel(game);
        	setScreen(ViewFactory.getInstance().loadGameScreen(game, this));
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
        
        // dispose some services
        MusicManager.getInstance().dispose();
        SoundManager.getInstance().dispose();
    }

	public GameModel getModel() {
		return game;
	}
	
	
}

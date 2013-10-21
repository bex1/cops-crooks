package com.dat255.project.android.copsandcrooks;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.ModelFactory;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;
import com.dat255.project.android.copsandcrooks.screens.Assets;
import com.dat255.project.android.copsandcrooks.utils.MusicManager;
import com.dat255.project.android.copsandcrooks.utils.PreferencesManager;
import com.dat255.project.android.copsandcrooks.utils.SoundManager;


/**
 * TODO docs here
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
@SuppressWarnings("unused")
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

        PreferencesManager prefs = PreferencesManager.getInstance();
        
        // create the music manager
        MusicManager musicManager = MusicManager.getInstance();
        musicManager.setVolume( prefs.getVolume() );

        // create the sound manager
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.setVolume( prefs.getVolume() );
        soundManager.setEnabled( prefs.isSoundEnabled() );
        
        GameFactory.getInstance().init(new Assets());
        fpsLogger = new FPSLogger();
        assets = GameFactory.getInstance().getAssets();
        if (game != null) {
        	setScreen(GameFactory.getInstance().loadGameScreen(game, this));
        } else {
        	GameItem gameToPlay = GameClient.getInstance().getChosenGameItem();
    		GameFactory factory = GameFactory.getInstance();
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
        	setScreen(GameFactory.getInstance().loadGameScreen(game, this));
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
        
        // dispose some services
        MusicManager.getInstance().dispose();
        SoundManager.getInstance().dispose();
    }

	public GameModel getModel() {
		return game;
	}
	
	
}

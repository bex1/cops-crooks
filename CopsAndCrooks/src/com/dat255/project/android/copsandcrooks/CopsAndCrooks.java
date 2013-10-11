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
        	game = ModelFactory.getInstance().loadLocalGameModel(game);
        	setScreen(GameFactory.getInstance().loadGameScreen(game, this));
        } else {
        	GameItem gameToPlay;
        	if (Gdx.app.getType() == ApplicationType.Android) {
        		gameToPlay = GameClient.getInstance().getChosenGameItem();
        		/*gameToPlay = new GameItem("spel", 2);
        		PlayerItem player = new PlayerItem("Kalle", "1ad");
        		player.setRole(Role.Cop);
        		PlayerItem player2 = new PlayerItem("Kalle", "2ad");
        		player2.setRole(Role.Crook);
        		gameToPlay.addPlayer(player);
        		gameToPlay.addPlayer(player2);//*/
        	} else {
        		gameToPlay = new GameItem("spel", 2);
        		PlayerItem player = new PlayerItem("Kalle", "1ad");
        		player.setRole(Role.Cop);
        		PlayerItem player2 = new PlayerItem("Kalle", "2ad");
        		player2.setRole(Role.Crook);
        		gameToPlay.addPlayer(player);
        		gameToPlay.addPlayer(player2);
        	}
    		GameFactory factory = GameFactory.getInstance();
    		ModelFactory modelFactory = ModelFactory.getInstance();
    		if(!gameToPlay.hasGameStarted()){
    			game = modelFactory.loadGameModel(gameToPlay, factory.getInteract(), false);
    		}else if(gameToPlay.hasGameStarted() && !factory.hasLoadedThisGameModel(gameToPlay)){
    			game = modelFactory.loadGameModel(gameToPlay, factory.getInteract(), true);
    		}else if(gameToPlay.hasGameStarted() && factory.hasLoadedThisGameModel(gameToPlay)){
    			game = modelFactory.loadLocalGameModel(factory.loadModelFromFile(gameToPlay.getName()));
    		}else{
    			assert false;
    			game = null;
    		}
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
    }

	public GameModel getModel() {
		return game;
	}
}

package com.dat255.project.android.copsandcrooks.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * This class is and abstraction of a screen, with the purpose to remove duplicated code between the screens.
 * 
 * Also makes it easier to implement more screens.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
abstract class AbstractScreen implements Screen {

    protected final CopsAndCrooks game;
    protected final Stage stage;
    protected final Assets assets;
    
    /**
     * 
     * @param assets The assets of the game.
     * @param game The CopsAndCrooks instance.
     * @param stageWidth The width of the game stage.
     * @param stageHeight The height of the game stage.
     */
    protected AbstractScreen(final Assets assets, final CopsAndCrooks game, float stageWidth, float stageHeight) {
        this.game = game;
        this.assets = assets;
        this.stage = new Stage(stageWidth, stageHeight, true);
    }

    protected String getName() {
        return getClass().getSimpleName();
    }
    
    @Override
    public void show() {
        Gdx.app.log(CopsAndCrooks.LOG, "Showing screen: " + getName());

        // set the stage as the input processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height ) {
        Gdx.app.log(CopsAndCrooks.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
        
        		
        stage.setViewport(Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT, true, 0, 0, width, height);
    }

    @Override
    public void render(float delta ) {
        // update the actors
        stage.act(delta);

        // draw the actors
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.app.log(CopsAndCrooks.LOG, "Hiding screen: " + getName());

        dispose();
    }

    @Override
    public void pause() {
        Gdx.app.log(CopsAndCrooks.LOG, "Pausing screen: " + getName());
    }

    @Override
    public void resume() {
        Gdx.app.log(CopsAndCrooks.LOG, "Resuming screen: " + getName());
    }

    @Override
    public void dispose() {
        Gdx.app.log(CopsAndCrooks.LOG, "Disposing screen: " + getName());

        stage.dispose();
    }
}
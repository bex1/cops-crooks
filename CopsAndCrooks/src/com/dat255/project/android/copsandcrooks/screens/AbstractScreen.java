package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen {

    protected final CopsAndCrooks game;
    protected Stage stage;
    protected Assets assets;

    private Table table;

    public AbstractScreen(Assets assets, CopsAndCrooks game, float stageWidth, float stageHeight) {
        this.game = game;
        this.assets = assets;
        this.stage = new Stage(stageWidth, stageHeight, true);
    }

    protected String getName() {
        return getClass().getSimpleName();
    }

    protected Table getTable()
    {
        if( table == null ) {
            table = new Table(assets.getSkin());
            table.setFillParent( true );
            if(CopsAndCrooks.DEV_MODE ) {
                table.debug();
            }
            stage.addActor( table );
        }
        return table;
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

package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen {
    // the fixed viewport dimensions (ratio: 1.6)
    public static final int GAME_VIEWPORT_WIDTH = 800, GAME_VIEWPORT_HEIGHT = 480;

    protected final CopsAndCrooks game;
    protected final Stage stage;

    private TextureAtlas atlas;

    public AbstractScreen(CopsAndCrooks game ) {
        this.game = game;
        this.stage = new Stage(GAME_VIEWPORT_WIDTH, GAME_VIEWPORT_HEIGHT, true);
    }

    protected String getName() {
        return getClass().getSimpleName();
    }

    public TextureAtlas getAtlas() {
        if( atlas == null ) {
            atlas = new TextureAtlas(Gdx.files.internal("image-atlases/pages.atlas"));
        }
        return atlas;
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
    }

    @Override
    public void render(float delta ) {
        // update the actors
        stage.act(delta);
        
        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

        // lazily loaded, may be null
        if( atlas != null ) atlas.dispose();
    }
}

package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class MenuScreen extends AbstractScreen {

	public MenuScreen(Assets assets, CopsAndCrooks game) {
		super(assets, game, Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT);
	}
	
	@Override
    public void show() {
        super.show();
        // retrieve the default table actor
        Table table = super.getTable();
        
        table.add("Cops & crooks!").spaceBottom(50);
        table.row();

        // register the button "start game"
        TextButton startGameButton = new TextButton("Start game", assets.getSkin());
        startGameButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                // TODO click sound
                // TODO switch to game screen or (in online -> start new or join existing game) 
                game.setScreen(new LoadingScreen(assets, game));
            }
        } );
        table.add(startGameButton).size(350, 60).uniform().spaceBottom(10);
        table.row();

        // register the button "options"
        TextButton optionsButton = new TextButton( "Options", assets.getSkin() );
        optionsButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                // TODO click sound
                // TODO switch to options screen
            }
        } );
        table.add(optionsButton).uniform().fill().spaceBottom(10);
        table.row();

        // register the button "exit"
        TextButton quitButton = new TextButton("Quit", assets.getSkin());
        quitButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                // TODO click sound
                Gdx.app.exit();
            }
        } );
        table.add(quitButton).uniform().fill();
    }
	
	@Override
	public void render(float delta) {
        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render(delta);
	}
}

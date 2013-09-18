package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;

public class MenuScreen extends AbstractScreen {

	public MenuScreen(CopsAndCrooks game) {
		super(game);
	}
	
	@Override
    public void show() {
        super.show();
        // retrieve the default table actor
        Table table = super.getTable();
        
        table.add("Cops & crooks!").spaceBottom(50);
        table.row();

        // register the button "start game"
        TextButton startGameButton = new TextButton("Start game", getSkin());
        startGameButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                // TODO click sound
                // TODO switch to game screen or (in online -> start new or join existing game) 
                game.setScreen(new GameScreen(game));
            }
        } );
        table.add(startGameButton).size(350, 60).uniform().spaceBottom(10);
        table.row();

        // register the button "options"
        TextButton optionsButton = new TextButton( "Options", getSkin() );
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
        TextButton highScoresButton = new TextButton("Quit", getSkin());
        highScoresButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                // TODO click sound
                // TODO kill app
            }
        } );
        table.add(highScoresButton).uniform().fill();
    }
}

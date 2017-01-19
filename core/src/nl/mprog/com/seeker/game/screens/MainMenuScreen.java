package nl.mprog.com.seeker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.io.BufferedReader;

import nl.mprog.com.seeker.game.Seeker;

/**
 * Created by Fjodor on 2017/01/18.
 */

public class MainMenuScreen implements Screen {

    private Seeker game;
    private Stage stage;
    int col_width = 100;
    int row_height = 150;

    public MainMenuScreen(final Seeker game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        final Button button = new TextButton("Play", mySkin);
        final Button button2 = new TextButton("Leaderboard",mySkin);
        button.setSize(col_width*4,row_height);
        button.setPosition(col_width*4,Gdx.graphics.getHeight()-row_height*3);
        button2.setSize(col_width*4,row_height);
        button2.setPosition(col_width*9,Gdx.graphics.getHeight()-row_height*3);

                button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.setScreen(new PlayScreen(game));
            }
        });
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.playServices.showScore();
            }
        });
        stage.addActor(button);
        stage.addActor(button2);
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}

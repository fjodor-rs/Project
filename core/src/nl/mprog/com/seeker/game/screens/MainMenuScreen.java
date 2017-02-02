package nl.mprog.com.seeker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import nl.mprog.com.seeker.game.Seeker;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * The Main Menu screen. Displays the game's title and lets you play the game or go to the leaderboard
 *
 */
public class MainMenuScreen implements Screen {

    private Seeker game;
    private Stage stage;
    int col_width = 100;
    int row_height = 150;
    private Sprite sprite;
    private Texture titleTexture;
    PlayScreen screen;

    public MainMenuScreen(final Seeker game) {


        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        final Button button = new TextButton("Play", mySkin);
        final Button button2 = new TextButton("Leaderboard", mySkin);
        final Button button3 = new TextButton("Achievements", mySkin);

        titleTexture = new Texture("jaap_title.png");
        sprite = new Sprite(titleTexture);
        sprite.setBounds(0, 0, 1200, 250);
        sprite.setPosition(275, 600);

        button2.setSize(col_width*4,row_height);
        button2.setPosition(col_width*2,Gdx.graphics.getHeight()-row_height*6);
        button.setSize(col_width*4,row_height);
        button.setPosition(col_width*7,Gdx.graphics.getHeight()-row_height*6);
        button3.setSize(col_width*4,row_height);
        button3.setPosition(col_width*12,Gdx.graphics.getHeight()-row_height*6);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.levelOne = true;
                game.setScreen(new PlayScreen(game));
            }
        });
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.playServices.showTime();
            }
        });
        button3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.playServices.showAchievement();
            }
        });

        stage.addActor(button);
        stage.addActor(button2);
        stage.addActor(button3);
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
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        game.batch.begin();
        sprite.draw(game.batch);
        game.batch.end();
    }
}

package nl.mprog.com.seeker.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import nl.mprog.com.seeker.game.Seeker;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * Screen that shows when the player dies.
 * Gives you the option to play again or to go back to the main menu.
 */

public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Game game;
    int col_width = 100;
    int row_height = 150;

    public GameOverScreen(final Seeker game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setPosition(900, 600);
        Label gameOverLabel = new Label("GAME OVER", font);
        gameOverLabel.setSize(1000, 250);
        gameOverLabel.setFontScale(10, 10);
        table.add(gameOverLabel).expandX();
        table.row();

        Skin mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        final Button button = new TextButton("Play Again", mySkin);
        final Button button2 = new TextButton("Leaderboard",mySkin);
        final Button button3 = new TextButton("Achievements", mySkin);

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
                game.levelTwo = false;
                game.levelThree = false;
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
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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
}

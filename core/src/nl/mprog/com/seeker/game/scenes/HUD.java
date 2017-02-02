package nl.mprog.com.seeker.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import nl.mprog.com.seeker.game.Seeker;


/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * Places text labels over the PlayScreen and keeps track of the time and score.
 * Also tracks if the time is up and kills Jaap if that is the case.
 */

public class HUD implements Disposable {
    public Stage stage;
    private Viewport viewPort;

    public Integer worldTimer;
    private boolean timeUp;
    private float timeCount;
    private static Integer score;

    Label countdownLabel;
    static Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label jaapLabel;

    public HUD(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewPort = new FitViewport(Seeker.V_WIDTH, Seeker.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        jaapLabel = new Label("JAAP", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(jaapLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);

        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
    }

    /**
     * Updates the game timer.
     */

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    /**
     * Keeps track of the in game score.
     */

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTimeUp() { return timeUp; }

}

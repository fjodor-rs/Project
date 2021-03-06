package nl.mprog.com.seeker.game.tools;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * Implements the onscreen controller that can be used to control the player.
 */


    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.scenes.scene2d.InputEvent;
    import com.badlogic.gdx.scenes.scene2d.InputListener;
    import com.badlogic.gdx.scenes.scene2d.Stage;
    import com.badlogic.gdx.scenes.scene2d.ui.Image;
    import com.badlogic.gdx.scenes.scene2d.ui.Table;
    import com.badlogic.gdx.utils.viewport.FitViewport;
    import com.badlogic.gdx.utils.viewport.Viewport;

    import nl.mprog.com.seeker.game.Seeker;

public class Controller {

    public Viewport viewport;
    public Stage stage;
    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;
    public Image buttonUp;
    public Image buttonDown;
    public Image buttonLeft;
    public Image buttonRight;
    public OrthographicCamera camera;
    public Table table;
    public Table table2;

    /**
     * Draws the controller on the screen, and checks for input on a specific button.
     */

    public Controller(SpriteBatch spriteBatch) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Seeker.V_WIDTH, Seeker.V_HEIGHT, camera);
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);

        buttonUp = new Image(new Texture("buttonup.png"));
        buttonUp.setSize(35, 35);
        buttonUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });
        buttonDown = new Image(new Texture("buttondown.png"));
        buttonDown.setSize(35, 35);
        buttonDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });
        buttonLeft = new Image(new Texture("buttonleft.png"));
        buttonLeft.setSize(35, 35);
        buttonLeft.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });
        buttonRight = new Image(new Texture("buttonright.png"));
        buttonRight.setSize(35, 35);
        buttonRight.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        table = new Table();
        table2 = new Table();

        table.left().bottom().padLeft(20);
        table2.setFillParent(true);
        table2.right().bottom().padRight(20);

        table.add(buttonLeft).size(buttonLeft.getWidth(), buttonLeft.getHeight()).padRight(20);
        table.add(buttonRight).size(buttonRight.getWidth(), buttonRight.getHeight());
        table.row().padBottom(5);

        table2.add(buttonDown).size(buttonDown.getWidth(), buttonDown.getHeight()).padRight(20);
        table2.add(buttonUp).size(buttonUp.getWidth(), buttonUp.getHeight());
        table2.row().padBottom(5);

        stage.addActor(table);
        stage.addActor(table2);
    }

    public void draw() {
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
}


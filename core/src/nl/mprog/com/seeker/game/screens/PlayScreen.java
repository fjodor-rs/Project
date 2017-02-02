package nl.mprog.com.seeker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.LinkedBlockingQueue;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.scenes.HUD;
import nl.mprog.com.seeker.game.sprites.Jaap;
import nl.mprog.com.seeker.game.sprites.enemies.Enemy;
import nl.mprog.com.seeker.game.sprites.items.Coin;
import nl.mprog.com.seeker.game.sprites.items.Item;
import nl.mprog.com.seeker.game.sprites.items.ItemDef;
import nl.mprog.com.seeker.game.sprites.items.Hulkifier;
import nl.mprog.com.seeker.game.tools.B2WorldCreator;
import nl.mprog.com.seeker.game.tools.Controller;
import nl.mprog.com.seeker.game.tools.WorldContactListener;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * The game screen implements the game, takes in the tiled textures, objects and sprites
 * and renders them and updates them. It also has a orthographic camera to follow the
 * player across the level.
 *
 * From here the screen is also changed to the GameOverScreen or the GameWonScreen when appropriate
 */

public class PlayScreen implements Screen {

    private Seeker game;
    private TextureAtlas textureAtlas;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private HUD hud;
    private Music music;

    private TmxMapLoader mapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private B2WorldCreator creator;

    private Jaap jaap;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    private Controller controller;
    private boolean achievementUnlock;

    public PlayScreen(Seeker game) {
        textureAtlas = new TextureAtlas("hulk_it.pack");

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Seeker.V_WIDTH / Seeker.PPM, Seeker.V_HEIGHT / Seeker.PPM, gameCam);
        hud = new HUD(game.batch);

        mapLoader = new TmxMapLoader();
        if (game.levelOne)
            tiledMap = mapLoader.load("second_level.tmx");
        if (game.levelTwo && !game.levelOne)
            tiledMap = mapLoader.load("demo_level.tmx");
        if (game.levelThree && (!game.levelOne || game.levelTwo))
            tiledMap = mapLoader.load("third_level.tmx");


        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / Seeker.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);

        creator = new B2WorldCreator(this);

        jaap = new Jaap(this);
        controller = new Controller(game.batch);

        world.setContactListener(new WorldContactListener());

        music = Seeker.manager.get("audio/music/factory_time_loop.ogg");
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
    }

    /**
     * Adds an item to itemsToSpawn to spawn them in handleSpawningItems
     */

    public void spawnItem(ItemDef itemDef){
        itemsToSpawn.add(itemDef);
    }

    /**
     * Checks what item needs to be spawned and spawns them at the position in itemDef.
     */

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef itemDef = itemsToSpawn.poll();
            if(itemDef.type == Hulkifier.class){
                items.add(new Hulkifier(this, itemDef.position.x, itemDef.position.y));
            }
            else if(itemDef.type == Coin.class){
                items.add(new Coin(this, itemDef.position.x, itemDef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas(){
        return textureAtlas;
    }

    @Override
    public void show() {

    }

    /**
     *  Takes in the controller input and sets off the necessary response.
     */

    public void handleInput(float dt){
        if(jaap.currentState != Jaap.State.DEAD) {
            if (controller.isUpPressed() && jaap.b2body.getLinearVelocity().y == 0)
                jaap.b2body.applyLinearImpulse(new Vector2(0, 4f), jaap.b2body.getWorldCenter(), true);
            if (controller.isRightPressed() && jaap.b2body.getLinearVelocity().x <= 2)
                jaap.b2body.applyLinearImpulse(new Vector2(0.1f, 0), jaap.b2body.getWorldCenter(), true);
            if (controller.isLeftPressed() && jaap.b2body.getLinearVelocity().x >= -2)
                jaap.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), jaap.b2body.getWorldCenter(), true);
            jaap.smashMode = controller.isDownPressed();
        }
    }

    /**
     * Updates everything that needs to be rendered and updates the camera's position.
     * Also uses worldContactListener to check for collision during a world step.
     */

    public void update(float dt){

        handleInput(dt);
        handleSpawningItems();
        world.step(1/60f, 6, 2);
        jaap.update(dt);
        for(Enemy enemy : creator.getEnemies()){
            enemy.update(dt);
            if(enemy.getX() < jaap.getX() + 224 / Seeker.PPM)
                enemy.b2body.setActive(true);
        }
        for(Item item: items){
            item.update(dt);
        }
        hud.update(dt);

        if(jaap.currentState != Jaap.State.DEAD) {
            gameCam.position.x = jaap.b2body.getPosition().x;
        }

        gameCam.update();
        renderer.setView(gameCam);
    }

    /**
     * Draws all textures, sprites, controller buttons and backgrounds.
     */

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        jaap.draw(game.batch);
        for(Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);
        for(Item item : items)
            item.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        controller.draw();

        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

        if(gameWon()){

            game.playServices.submitTime(hud.worldTimer * 1000);
            game.setScreen(new GameWonScreen(game));
            dispose();
        }
        if(jaap.isHulk() && !achievementUnlock){
            game.playServices.unlockAchievement();
            achievementUnlock = true;
        }
    }

    public boolean gameOver(){
        if(jaap.currentState == Jaap.State.DEAD && jaap.getStateTimer() > 3){
            return true;
        }
        return false;
    }

    public boolean gameWon(){
        if(jaap.currentState == Jaap.State.WON && jaap.getStateTimer() > 3){
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {

        gamePort.update(width, height);
        controller.resize(width, height);
    }

    public TiledMap getMap(){
        return tiledMap;
    }

    public World getWorld(){
        return world;
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
        tiledMap.dispose();
        renderer.dispose();
        world.dispose();
        hud.dispose();

    }

    public HUD getHud(){ return hud; }

}

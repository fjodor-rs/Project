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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.LinkedBlockingQueue;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.scenes.HUD;
import nl.mprog.com.seeker.game.sprites.enemies.Enemy;
import nl.mprog.com.seeker.game.sprites.Mario;
import nl.mprog.com.seeker.game.sprites.items.Item;
import nl.mprog.com.seeker.game.sprites.items.ItemDef;
import nl.mprog.com.seeker.game.sprites.items.Mushroom;
import nl.mprog.com.seeker.game.tools.B2WorldCreator;
import nl.mprog.com.seeker.game.tools.Controller;
import nl.mprog.com.seeker.game.tools.WorldContactListener;

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
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private Mario mario;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    private Controller controller;

    public PlayScreen(Seeker game) {
        textureAtlas = new TextureAtlas("hulk_it.pack");

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Seeker.V_WIDTH / Seeker.PPM, Seeker.V_HEIGHT / Seeker.PPM, gameCam);
        hud = new HUD(game.batch);

        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("demo_level.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / Seeker.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        mario = new Mario(this);
        controller = new Controller(game.batch);

        world.setContactListener(new WorldContactListener());

        music = Seeker.manager.get("audio/music/factory_time_loop.ogg");
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

    }

    public void spawnItem(ItemDef itemDef){
        itemsToSpawn.add(itemDef);
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef itemDef = itemsToSpawn.poll();
            if(itemDef.type == Mushroom.class){
                items.add(new Mushroom(this, itemDef.position.x, itemDef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas(){
        return textureAtlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if(mario.currentState != Mario.State.DEAD) {
            if (controller.isUpPressed() && mario.b2body.getLinearVelocity().y == 0)
                mario.b2body.applyLinearImpulse(new Vector2(0, 4f), mario.b2body.getWorldCenter(), true); //true - will this impulse wake object.
            if (controller.isRightPressed() && mario.b2body.getLinearVelocity().x <= 2)
                mario.b2body.applyLinearImpulse(new Vector2(0.1f, 0), mario.b2body.getWorldCenter(), true);
            if (controller.isLeftPressed() && mario.b2body.getLinearVelocity().x >= -2)
                mario.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), mario.b2body.getWorldCenter(), true);
            if(controller.isDownPressed())
                mario.smashMode = true;
            else
                mario.smashMode = false;
        }
    }

    public void update(float dt){

        handleInput(dt);
        handleSpawningItems();
        world.step(1/60f, 6, 2);
        mario.update(dt);
        for(Enemy enemy : creator.getEnemies()){
            enemy.update(dt);
            if(enemy.getX() < mario.getX() + 224 / Seeker.PPM)
                enemy.b2body.setActive(true);
        }
        for(Item item: items){
            item.update(dt);
        }
        hud.update(dt);

        if(mario.currentState != Mario.State.DEAD) {
            gameCam.position.x = mario.b2body.getPosition().x;
        }

        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        mario.draw(game.batch);
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
            game.playServices.submitScore(hud.worldTimer * 1000);
            game.setScreen(new GameWonScreen(game));
            dispose();
        }
    }

    public boolean gameOver(){
        if(mario.currentState == Mario.State.DEAD && mario.getStateTimer() > 3){
            return true;
        }

        return false;
    }

    public boolean gameWon(){
        if(mario.currentState == Mario.State.WON && mario.getStateTimer() > 3){
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
        b2dr.dispose();
        hud.dispose();

    }

    public HUD getHud(){ return hud; }

}

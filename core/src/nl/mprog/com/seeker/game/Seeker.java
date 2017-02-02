package nl.mprog.com.seeker.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.mprog.com.seeker.game.screens.MainMenuScreen;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * The main game class holds the static bits used for collision detection.
 * It creates the SpriteBatch that is used throughout all the other screens.
 * It also loads in the music used in the game and sets the screen to the Main Menu.
 * Finally it holds the static values for the width and height used for the game and the scale used to work with Tiled.
 */

public class Seeker extends Game {
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;
	public static final float PPM = 100;

	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short JAAP_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short JAAP_HEAD_BIT = 512;
	public static final short END_BIT = 1024;
	public static final short JAAP_SMASH_BIT = 2048;

	public boolean levelOne;
	public boolean levelTwo;
	public boolean levelThree;

	public SpriteBatch batch;

	public static AssetManager manager;

	public static PlayServices playServices;

	public Seeker(PlayServices playServices)
	{
		this.playServices = playServices;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();

		manager = new AssetManager();
		manager.load("audio/music/factory_time_loop.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
		manager.load("audio/sounds/powerup.wav", Sound.class);
		manager.load("audio/sounds/stomp.wav", Sound.class);
		manager.load("audio/sounds/powerdown.wav", Sound.class);
		manager.load("audio/sounds/mariodie.wav", Sound.class);
		manager.load("audio/sounds/win.wav", Sound.class);

		manager.finishLoading();

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}

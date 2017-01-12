package nl.mprog.com.seeker.game.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.scenes.HUD;
import nl.mprog.com.seeker.game.screens.PlayScreen;

/**
 * Created by Fjodor on 2017/01/10.
 */

public class CoinBlock extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    public CoinBlock(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        tileSet = tiledMap.getTileSets().getTileSet("NES - Super Mario Bros - Tileset");
        fixture.setUserData(this);
        setCategoryFilter(Seeker.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        if(getCell().getTile().getId() == BLANK_COIN)
            Seeker.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else
            Seeker.manager.get("audio/sounds/coin.wav", Sound.class).play();


        getCell().setTile(tileSet.getTile(BLANK_COIN));
        HUD.addScore(100);
    }
}


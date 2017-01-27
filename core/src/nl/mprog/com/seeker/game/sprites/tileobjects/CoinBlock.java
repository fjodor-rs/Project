package nl.mprog.com.seeker.game.sprites.tileobjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.scenes.HUD;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.sprites.Mario;
import nl.mprog.com.seeker.game.sprites.items.Coin;
import nl.mprog.com.seeker.game.sprites.items.ItemDef;
import nl.mprog.com.seeker.game.sprites.items.Mushroom;

/**
 * Created by Fjodor on 2017/01/10.
 */

public class CoinBlock extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    public CoinBlock(PlayScreen screen, MapObject object){
        super(screen, object);
        tileSet = tiledMap.getTileSets().getTileSet("NES - Super Mario Bros - Tileset");
        fixture.setUserData(this);
        setCategoryFilter(Seeker.COIN_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(getCell().getTile().getId() == BLANK_COIN)
            Seeker.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else{
            if(object.getProperties().containsKey("mushroom")){
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / Seeker.PPM),
                        Mushroom.class));
                Seeker.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            }
            else{
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / Seeker.PPM),
                        Coin.class));
                Seeker.manager.get("audio/sounds/coin.wav", Sound.class).play();
            }
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
    }
}


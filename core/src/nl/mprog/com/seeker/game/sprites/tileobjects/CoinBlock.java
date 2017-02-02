package nl.mprog.com.seeker.game.sprites.tileobjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.sprites.Jaap;
import nl.mprog.com.seeker.game.sprites.items.Coin;
import nl.mprog.com.seeker.game.sprites.items.ItemDef;
import nl.mprog.com.seeker.game.sprites.items.Hulkifier;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * Sets the unique method and bits for all coin blocks.
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

    /**
     * Checks if a coin block contains a hulkifier and spawns it if it does.
     * If it doesn't it spawns a coin. In both cases it sets the tile to an empty coin block afterwards.
     */

    @Override
    public void onHeadHit(Jaap jaap) {
        if(getCell().getTile().getId() == BLANK_COIN)
            Seeker.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else{
            if(object.getProperties().containsKey("mushroom")){
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / Seeker.PPM),
                        Hulkifier.class));
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


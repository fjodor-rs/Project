package nl.mprog.com.seeker.game.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.scenes.HUD;
import nl.mprog.com.seeker.game.screens.PlayScreen;

/**
 * Created by Fjodor on 2017/01/10.
 */

public class Brick extends InteractiveTileObject {

    public Brick(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Seeker.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        setCategoryFilter(Seeker.DESTROYED_BIT);
        getCell().setTile(null);
        HUD.addScore(200);
        Seeker.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }

}

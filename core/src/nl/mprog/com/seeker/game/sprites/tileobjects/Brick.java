package nl.mprog.com.seeker.game.sprites.tileobjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.scenes.HUD;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.sprites.Jaap;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * Sets a brick's unique bit and onHeadHit method.
 */

public class Brick extends InteractiveTileObject {

    public Brick(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Seeker.BRICK_BIT);
    }

    /**
     * Breaks the brick, changes the score and makes the cell empty.
     */

    @Override
    public void onHeadHit(Jaap jaap) {
        if(jaap.isHulk()){
            setCategoryFilter(Seeker.DESTROYED_BIT);
            getCell().setTile(null);
            HUD.addScore(200);
            Seeker.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }
        Seeker.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }
}

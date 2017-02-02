package nl.mprog.com.seeker.game.sprites.tileobjects;

import com.badlogic.gdx.maps.MapObject;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.sprites.Jaap;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * If Jaap collides with this block, he wins the game, its unique bit is set here.
 */

public class EndBlock extends InteractiveTileObject {
    public EndBlock(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Seeker.END_BIT);
    }

    @Override
    public void onHeadHit(Jaap jaap) {

    }
}

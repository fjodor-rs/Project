package nl.mprog.com.seeker.game.sprites.tileobjects;

import com.badlogic.gdx.maps.MapObject;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.sprites.Jaap;

/**
 * Created by Fjodor on 2017/01/19.
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

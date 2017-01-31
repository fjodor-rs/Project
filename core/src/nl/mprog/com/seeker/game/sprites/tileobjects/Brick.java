package nl.mprog.com.seeker.game.sprites.tileobjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.scenes.HUD;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.sprites.Jaap;

/**
 * Created by Fjodor on 2017/01/10.
 */

public class Brick extends InteractiveTileObject {

    public Brick(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Seeker.BRICK_BIT);
    }

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

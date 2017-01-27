package nl.mprog.com.seeker.game.sprites.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.scenes.HUD;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.sprites.Mario;

/**
 * Created by Fjodor on 2017/01/27.
 */

public class Coin extends Item {

    public Coin(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("coin"), 0, 0, 256, 256);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / Seeker.PPM);
        fdef.filter.categoryBits = Seeker.ITEM_BIT;
        fdef.filter.maskBits = Seeker.MARIO_BIT | Seeker.OBJECT_BIT | Seeker.GROUND_BIT | Seeker.COIN_BIT | Seeker.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Mario mario) {
        destroy();
        Seeker.manager.get("audio/sounds/coin.wav", Sound.class).play();
        HUD.addScore(100);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }
}

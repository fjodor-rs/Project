package nl.mprog.com.seeker.game.sprites.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.sprites.Jaap;


/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * an item that lets Jaap go into his Hulk Mode and only spawns out of specific coin blocks.
 */

public class Hulkifier extends Item {

    public Hulkifier(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("computer"), 0, 0, 256, 256);
        velocity = new Vector2(0.7f, 0);
    }

    /**
     * Defines the hulkifier, giving it a body and attaching a fixture, setting a bit and telling it what it can collide with.
     */

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7/ Seeker.PPM);
        fdef.filter.categoryBits = Seeker.ITEM_BIT;
        fdef.filter.maskBits = Seeker.JAAP_BIT | Seeker.OBJECT_BIT
                | Seeker.GROUND_BIT | Seeker.COIN_BIT | Seeker.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        setBounds(getX(), getY(), 24 / Seeker.PPM, 24 / Seeker.PPM);
    }


    /**
     * Is called when Jaap comes into contact with the Hulkifier, makes Jaap hulk out.
     */

    @Override
    public void use(Jaap jaap) {
        destroy();
        jaap.hulkOut();
    }

    /**
     * Updates the position and sets the velocity of the hulkifier.
     */

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}

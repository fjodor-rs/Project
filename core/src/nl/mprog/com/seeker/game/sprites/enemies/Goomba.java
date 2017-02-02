package nl.mprog.com.seeker.game.sprites.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.sprites.Jaap;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * Class that sets the body, textures and animations for the goomba sprite, updates them
 * and implements them for all the goombas.
 */

public class Goomba extends nl.mprog.com.seeker.game.sprites.enemies.Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / Seeker.PPM, 16 / Seeker.PPM);
        setToDestroy = false;
        destroyed = false;
    }

    /**
     * Checks if the body needs to be destroyed and destroys it using its dieing texture.
     * If it is not destroyed it sets it positions, applies velocity and updates the texture frames.
     */

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
        }
        else if(!destroyed){
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    /**
     * Defines the Goomba's body and attaches two fixtures. Making a fixture for its body and for above its head.
     * Also sets the Goomba's unique bit and decides what the Goomba can come into contact with.
     */

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / Seeker.PPM);
        fdef.filter.categoryBits = Seeker.ENEMY_BIT;
        fdef.filter.maskBits = Seeker.GROUND_BIT | Seeker.COIN_BIT | Seeker.BRICK_BIT | Seeker.ENEMY_BIT | Seeker.OBJECT_BIT | Seeker.JAAP_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertix = new Vector2[4];
        vertix[0] = new Vector2(-6, 10).scl(1 / Seeker.PPM);
        vertix[1] = new Vector2(6, 10).scl(1 / Seeker.PPM);
        vertix[2] = new Vector2(-4, 3).scl(1 / Seeker.PPM);
        vertix[3] = new Vector2(4, 3).scl(1 / Seeker.PPM);
        head.set(vertix);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = Seeker.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * Sets to destroy when hit on the head by Jaap.
     */

    @Override
    public void hitOnHead(Jaap jaap) {
        setToDestroy = true;
        Seeker.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }

    /**
     * Sets what it needs to do when hit by another enemy.
     */

    @Override
    public void hitByEnemy(Enemy enemy) {
        if(enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL)
            setToDestroy = true;
        else
            reverseVelocity(true, false);
    }

    /**
     * Draws the sprite.
     */

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }
}

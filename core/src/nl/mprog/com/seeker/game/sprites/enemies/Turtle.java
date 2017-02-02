package nl.mprog.com.seeker.game.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
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
 * Class that sets the body, textures and animations for the turtle sprite, updates them
 * and implements them for all the turtles. Turtles use states, defaulting to WALKING as their current state.
 */

public class Turtle extends Enemy {
    public static final int KICK_LEFT = -2;
    public static final int KICK_RIGHT = 2;

    public enum State {WALKING, MOVING_SHELL, STANDING_SHELL, DEAD}

    public State currentState;
    public State previousState;
    private float stateTime;
    private Animation walkAnimation;
    private TextureRegion shell;
    private float deadRotationDegrees;
    private boolean setToDestroy;
    private boolean destroyed;

    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;
        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
        setBounds(getX(), getY(), 16 / Seeker.PPM, 24 / Seeker.PPM);
        deadRotationDegrees = 0;
    }

    /**
     * Defines the Turtle's body and attaches two fixtures. Making a fixture for its body and for above its head.
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
        shape.setRadius(6 / Seeker.PPM);
        fdef.filter.categoryBits = Seeker.ENEMY_BIT;
        fdef.filter.maskBits = Seeker.GROUND_BIT | Seeker.COIN_BIT | Seeker.BRICK_BIT
                | Seeker.ENEMY_BIT | Seeker.OBJECT_BIT | Seeker.JAAP_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertix = new Vector2[4];
        vertix[0] = new Vector2(-5, 8).scl(1 / Seeker.PPM);
        vertix[1] = new Vector2(5, 8).scl(1 / Seeker.PPM);
        vertix[2] = new Vector2(-3, 3).scl(1 / Seeker.PPM);
        vertix[3] = new Vector2(3, 3).scl(1 / Seeker.PPM);
        head.set(vertix);

        fdef.shape = head;
        fdef.restitution = 1.8f;
        fdef.filter.categoryBits = Seeker.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * gets the correct texture frames for the turtle's different states.
     */

    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (currentState) {
            case MOVING_SHELL:
            case STANDING_SHELL:
                region = shell;
                break;
            case WALKING:
            default:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if (velocity.x > 0 && !region.isFlipX()) {
            region.flip(true, false);
        } else if (velocity.x < 0 && region.isFlipX()) {
            region.flip(true, false);
        }
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    /**
     * Updates the frame region and positions and sets the velocity if walking.
     * Sets the state from standing shell to walking after 5 seconds.
     * Also checks if the turtle's state is DEAD to remove it from the game when it dies.
    */

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / Seeker.PPM);

        if (currentState == State.STANDING_SHELL && stateTime > 5) {
            currentState = State.WALKING;
            velocity.x = 1;
        }

        if(currentState == State.DEAD){
            deadRotationDegrees += 3;
            rotate(deadRotationDegrees);
            if(stateTime > 5 && !destroyed){
                world.destroyBody(b2body);
                destroyed = true;
            }
        }
        else
            b2body.setLinearVelocity(velocity);
    }

    /**
     * Lets Jaap kick the turtle, depending on his position, to either side.
     */

    @Override
    public void hitOnHead(Jaap jaap) {
        if (currentState != State.STANDING_SHELL) {
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        } else {
            kick(jaap.getX() > this.getX() ? KICK_RIGHT : KICK_LEFT);
        }
    }

    /**
     * Deals with what happens to the turtle if he is hit by an enemy.
     */

    @Override
    public void hitByEnemy(Enemy enemy) {
        if(enemy instanceof Turtle){
            if(((Turtle) enemy).currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL){
                killed();
            }
            else if(currentState == State.MOVING_SHELL && ((Turtle) enemy).currentState == State.WALKING)
                return;
            else
                reverseVelocity(true, false);
        }
        else if(currentState != State.MOVING_SHELL)
            reverseVelocity(true,false);
    }

    public void kick(int speed) {
        velocity.x = speed;
        currentState = State.MOVING_SHELL;
    }

    public State getCurrentState(){
        return currentState;
    }

    /**
     * Makes the turtle spin through the level and die when called.
     */

    public void killed(){
        currentState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = Seeker.NOTHING_BIT;

        for(Fixture fixture : b2body.getFixtureList())
            fixture.setFilterData(filter);
        b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
    }
}
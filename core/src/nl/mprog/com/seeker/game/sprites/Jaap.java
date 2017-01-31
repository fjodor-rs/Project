package nl.mprog.com.seeker.game.sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


import nl.mprog.com.seeker.game.PlayServices;
import nl.mprog.com.seeker.game.scenes.HUD;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.sprites.enemies.Enemy;
import nl.mprog.com.seeker.game.sprites.enemies.Turtle;
import nl.mprog.com.seeker.game.sprites.tileobjects.InteractiveTileObject;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * Deals with all the animations, textures and states of Jaap and his Hulk Mode.
 * Also has methods for when Jaap or Hulk collide with something.
 * Finally implements Jaap dieing or winning the game.
 */

public class Jaap extends Sprite{


    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD, WON, SMASHING};
    public State currentState;
    public State previousState;
    public boolean smashMode;

    private PlayScreen screen;

    private TextureRegion jaapStand;
    private Animation jaapRun;
    private TextureRegion jaapJump;
    private TextureRegion jaapDead;
    private TextureRegion hulkStand;
    private TextureRegion hulkJump;
    private TextureRegion jaapWinning;
    private TextureRegion hulkWinning;
    private Animation hulkRun;
    private Animation hulkAnimation;
    private Animation jaapSmash;
    private Animation hulkSmash;

    public InteractiveTileObject touching;
    public World world;
    public Body b2body;

    private float stateTimer;

    private boolean runningRight;
    private boolean jaapIsHulk;
    private boolean runGrowAnimation;
    private boolean timeToDefineHulk;
    private boolean timeToRedefineJaap;
    private boolean jaapIsDead;
    private boolean jaapWon;

    public Jaap(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 1; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), i * 64, 24, 64, 64));
        jaapRun = new Animation(0.1f, frames);

        frames.clear();

        for(int i = 1; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("hulk_it"), i * 64, 24, 64, 64));
        hulkRun = new Animation(0.1f, frames);

        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 0, 96, 70, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 72, 96, 70, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 144, 96, 70, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 216, 96, 70, 64));

        jaapSmash = new Animation(0.15f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("hulk_it"), 0, 96, 70, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("hulk_it"), 72, 96, 70, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("hulk_it"), 144, 96, 70, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("hulk_it"), 216, 96, 70, 64));

        hulkSmash = new Animation(0.15f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 0, 24, 64, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("hulk_it"), 0, 24, 64, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 0, 24, 64, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("hulk_it"), 0, 24, 64, 64));
        hulkAnimation = new Animation(0.2f, frames);

        jaapJump = new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 384, 8, 64, 80);
        hulkJump = new TextureRegion(screen.getAtlas().findRegion("hulk_it"), 384, 8, 64, 80);

        jaapStand = new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 0, 24, 64, 64);
        hulkStand = new TextureRegion(screen.getAtlas().findRegion("hulk_it"), 0, 24, 64, 64);

        jaapDead = new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 160, 244, 75, 64);

        jaapWinning = new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 290, 158, 64, 75);
        hulkWinning = new TextureRegion(screen.getAtlas().findRegion("hulk_it"), 290, 158, 64, 75);

        defineJaap();
        setBounds(0, 0, 52 / Seeker.PPM, 52 / Seeker.PPM);
        setRegion(jaapStand);
    }

    public void defineHulk(){
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);
        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16 / Seeker.PPM);
        fdef.filter.categoryBits = Seeker.JAAP_BIT;
        fdef.filter.maskBits = Seeker.GROUND_BIT | Seeker.COIN_BIT | Seeker.BRICK_BIT | Seeker.ENEMY_BIT | Seeker.ENEMY_HEAD_BIT | Seeker.OBJECT_BIT | Seeker.ITEM_BIT | Seeker.END_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-9 / Seeker.PPM, -16 / Seeker.PPM), new Vector2(9 / Seeker.PPM, -16 / Seeker.PPM));
        fdef.filter.categoryBits = Seeker.JAAP_SMASH_BIT;
        fdef.shape = feet;
        fdef.isSensor = false;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Seeker.PPM, 16 / Seeker.PPM), new Vector2(2 / Seeker.PPM, 16 / Seeker.PPM));
        fdef.filter.categoryBits = Seeker.JAAP_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        timeToDefineHulk = false;
    }

    public void defineJaap(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / Seeker.PPM, 32 / Seeker.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16 / Seeker.PPM);
        fdef.filter.categoryBits = Seeker.JAAP_BIT;
        fdef.filter.maskBits = Seeker.GROUND_BIT | Seeker.COIN_BIT | Seeker.BRICK_BIT | Seeker.ENEMY_BIT | Seeker.ENEMY_HEAD_BIT | Seeker.OBJECT_BIT | Seeker.ITEM_BIT | Seeker.END_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-9 / Seeker.PPM, -16 / Seeker.PPM), new Vector2(9 / Seeker.PPM, -16 / Seeker.PPM));
        fdef.filter.categoryBits = Seeker.JAAP_SMASH_BIT;
        fdef.shape = feet;
        fdef.isSensor = false;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Seeker.PPM, 16 / Seeker.PPM), new Vector2(2 / Seeker.PPM, 16 / Seeker.PPM));
        fdef.filter.categoryBits = Seeker.JAAP_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void update(float dt){

        if (screen.getHud().isTimeUp() && !isDead()) {
            die();
        }

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        setRegion(getFrame(dt));
        if(timeToDefineHulk){
            defineHulk();
        }
        if(timeToRedefineJaap){
            redefineJaap();
        }
    }

    public TextureRegion getFrame(Float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case WON:
                region = jaapIsHulk ? hulkWinning : jaapWinning;
                break;
            case DEAD:
                region = jaapDead;
                break;
            case GROWING:
                region = (TextureRegion) hulkAnimation.getKeyFrame(stateTimer);
                if(hulkAnimation.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = jaapIsHulk ? hulkJump : jaapJump;
                break;
            case RUNNING:
                region = jaapIsHulk ? (TextureRegion) hulkRun.getKeyFrame(stateTimer, true) : (TextureRegion) jaapRun.getKeyFrame(stateTimer, true);
                break;
            case SMASHING:
                region = jaapIsHulk ? (TextureRegion) hulkSmash.getKeyFrame(stateTimer, true) : (TextureRegion) jaapSmash.getKeyFrame(stateTimer, true);
                if (touching != null) {
                    onSmashBrick(this.touching);
                }
                break;
            case FALLING:
                region = jaapIsHulk ? hulkJump : jaapJump;
                break;
            case STANDING:
            default:
                region = jaapIsHulk ? hulkStand : jaapStand;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }

        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if(jaapWon)
            return State.WON;
        else if( jaapIsDead)
            return State.DEAD;
        else if(runGrowAnimation)
            return State.GROWING;
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else if(smashMode)
            return State.SMASHING;
        else
            return State.STANDING;
        }

    public void grow() {
        runGrowAnimation = true;
        jaapIsHulk = true;
        timeToDefineHulk = true;
        Seeker.manager.get("audio/sounds/powerup.wav", Sound.class).play();

    }

    public void die() {

        if (!isDead()) {

            Seeker.manager.get("audio/music/factory_time_loop.ogg", Music.class).stop();
            Seeker.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
             jaapIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = Seeker.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }

    public boolean isDead(){
        return  jaapIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public boolean isHulk() {
        return jaapIsHulk;
    }

    public void setTouching(InteractiveTileObject object) {
        touching = object;
    }

    public void onSmashBrick(InteractiveTileObject brick) {

        brick.setCategoryFilter(Seeker.DESTROYED_BIT);
        brick.getCell().setTile(null);
        HUD.addScore(200);
        Seeker.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        setTouching(null);
    }

    public void hit(Enemy enemy){
        if(enemy instanceof Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.STANDING_SHELL)
            ((Turtle) enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT : Turtle.KICK_LEFT);
        else {
            if (jaapIsHulk) {
                jaapIsHulk = false;
                timeToRedefineJaap = true;
                Seeker.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            } else {
                die();
            }
        }
    }

    public void win(){
        if(!jaapWon) {
            jaapWon = true;
            Seeker.manager.get("audio/sounds/win.wav", Sound.class).play();
            Seeker.manager.get("audio/music/factory_time_loop.ogg", Music.class).stop();

            b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
        }
    }

    public void redefineJaap() {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16 / Seeker.PPM);
        fdef.filter.categoryBits = Seeker.JAAP_BIT;
        fdef.filter.maskBits = Seeker.GROUND_BIT | Seeker.COIN_BIT | Seeker.BRICK_BIT | Seeker.ENEMY_BIT | Seeker.ENEMY_HEAD_BIT | Seeker.OBJECT_BIT | Seeker.ITEM_BIT | Seeker.END_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-9 / Seeker.PPM, -16 / Seeker.PPM), new Vector2(9 / Seeker.PPM, -16 / Seeker.PPM));
        fdef.filter.categoryBits = Seeker.JAAP_SMASH_BIT;
        fdef.shape = feet;
        fdef.isSensor = false;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Seeker.PPM, 16 / Seeker.PPM), new Vector2(2 / Seeker.PPM, 16 / Seeker.PPM));
        fdef.filter.categoryBits = Seeker.JAAP_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        timeToRedefineJaap = false;
    }
}

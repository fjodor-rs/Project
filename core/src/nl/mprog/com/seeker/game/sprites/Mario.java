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


import nl.mprog.com.seeker.game.scenes.HUD;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.sprites.enemies.Enemy;
import nl.mprog.com.seeker.game.sprites.enemies.Turtle;
import nl.mprog.com.seeker.game.sprites.tileobjects.Brick;
import nl.mprog.com.seeker.game.sprites.tileobjects.InteractiveTileObject;
import nl.mprog.com.seeker.game.sprites.weapons.Axe;
import nl.mprog.com.seeker.game.tools.Controller;

/**
 * Created by Fjodor on 2017/01/10.
 */

public class Mario extends Sprite{


    private PlayScreen screen;
    public boolean smashMode;
    public boolean brickContact;

    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD, WON, SMASHING};
    public State currentState;
    public State previousState;

    private TextureRegion marioStand;
    private Animation marioRun;
    private TextureRegion marioJump;
    private TextureRegion marioDead;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation bigMarioRun;
    private Animation growMario;
    private Animation marioSmash;

    public InteractiveTileObject touching;
    public World world;
    public Body b2body;

    private float stateTimer;

    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean marioIsDead;
    private boolean marioWon;

    private Array<Axe> axes;

    public Mario(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 1; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), i * 64, 24, 64, 64));
        marioRun = new Animation(0.1f, frames);

        frames.clear();

        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
        bigMarioRun = new Animation(0.1f, frames);

        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 0, 96, 70, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 72, 96, 70, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 144, 96, 70, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 216, 96, 70, 64));

        marioSmash = new Animation(0.15f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation(0.2f, frames);

        marioJump = new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 384, 8, 64, 80);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);

        marioStand = new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 0, 24, 64, 64);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

        marioDead = new TextureRegion(screen.getAtlas().findRegion("wreck_it"), 160, 244, 75, 64);

        defineMario();
        setBounds(0, 0, 52 / Seeker.PPM, 52 / Seeker.PPM);
        setRegion(marioStand);
        axes = new Array<Axe>();
    }

    public void defineBigMario(){
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);
        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / Seeker.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Seeker.PPM);
        fdef.filter.categoryBits = Seeker.MARIO_BIT;
        fdef.filter.maskBits = Seeker.GROUND_BIT | Seeker.COIN_BIT | Seeker.BRICK_BIT | Seeker.ENEMY_BIT | Seeker.ENEMY_HEAD_BIT | Seeker.OBJECT_BIT | Seeker.ITEM_BIT | Seeker.END_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / Seeker.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-2 / Seeker.PPM, -6 / Seeker.PPM), new Vector2(2/ Seeker.PPM, -6 / Seeker.PPM));
        fdef.shape = feet;
        fdef.isSensor = false;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Seeker.PPM, 6 / Seeker.PPM), new Vector2(2 / Seeker.PPM, 6 / Seeker.PPM));
        fdef.filter.categoryBits = Seeker.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;
    }

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / Seeker.PPM, 32 / Seeker.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(18 / Seeker.PPM);
        fdef.filter.categoryBits = Seeker.MARIO_BIT;
        fdef.filter.maskBits = Seeker.GROUND_BIT | Seeker.COIN_BIT | Seeker.BRICK_BIT | Seeker.ENEMY_BIT | Seeker.ENEMY_HEAD_BIT | Seeker.OBJECT_BIT | Seeker.ITEM_BIT | Seeker.END_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-6 / Seeker.PPM, -18 / Seeker.PPM), new Vector2(6 / Seeker.PPM, -18 / Seeker.PPM));
        fdef.filter.categoryBits = Seeker.MARIO_SMASH_BIT;
        fdef.shape = feet;
        fdef.isSensor = false;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Seeker.PPM, 18 / Seeker.PPM), new Vector2(2 / Seeker.PPM, 18 / Seeker.PPM));
        fdef.filter.categoryBits = Seeker.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        smashMode = false;

    }

    public void update(float dt){

        if (screen.getHud().isTimeUp() && !isDead()) {
            die();
        }

        if(marioIsBig){
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 6 / Seeker.PPM);
        }
        else{
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }
        setRegion(getFrame(dt));
        if(timeToDefineBigMario){
            defineBigMario();
        }
        if(timeToRedefineMario){
            redefineMario();
        }

        for(Axe axe: axes){
            axe.update(dt);
            if(axe.isDestroyed())
                axes.removeValue(axe, true);
        }
    }

    public TextureRegion getFrame(Float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case WON:
                region = marioDead;
                break;
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = marioIsBig ? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true) : (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case SMASHING:
                region = (TextureRegion) marioSmash.getKeyFrame(stateTimer, true);
                if (touching != null) {
                    onSmashBrick(this.touching);
                }
                break;
            case FALLING:
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
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
        if(marioWon)
            return State.WON;
        else if(marioIsDead)
            return State.DEAD;
        else if(runGrowAnimation)
            return State.GROWING;
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(smashMode)
            return State.SMASHING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;


        }

    public void grow() {
        runGrowAnimation = true;
        marioIsBig = true;
        timeToDefineBigMario = true;
        setBounds(getX(), getY(),getWidth(), getHeight() * 2);
        Seeker.manager.get("audio/sounds/powerup.wav", Sound.class).play();
    }

    public void die() {

        if (!isDead()) {

            Seeker.manager.get("audio/music/factory_time_loop.ogg", Music.class).stop();
            Seeker.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            marioIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = Seeker.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }

    public boolean isDead(){
        return marioIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public boolean isBig() {
        return marioIsBig;
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

    public void onStopSmash() {
        brickContact = false;
    }

    public void hit(Enemy enemy){
        if(enemy instanceof Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.STANDING_SHELL)
            ((Turtle) enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT : Turtle.KICK_LEFT);
        else {
            if (marioIsBig) {
                marioIsBig = false;
                timeToRedefineMario = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                Seeker.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            } else {
                die();
            }
        }
    }

    public void win(){
        marioWon = true;
        Seeker.manager.get("audio/sounds/mariodie.wav", Sound.class).play();

    }

    public void redefineMario() {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Seeker.PPM);
        fdef.filter.categoryBits = Seeker.MARIO_BIT;
        fdef.filter.maskBits = Seeker.GROUND_BIT | Seeker.COIN_BIT | Seeker.BRICK_BIT | Seeker.ENEMY_BIT | Seeker.ENEMY_HEAD_BIT | Seeker.OBJECT_BIT | Seeker.ITEM_BIT | Seeker.END_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-2 / Seeker.PPM, -6 / Seeker.PPM), new Vector2(2/ Seeker.PPM, -6 / Seeker.PPM));
        fdef.shape = feet;
        fdef.isSensor = false;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Seeker.PPM, 6 / Seeker.PPM), new Vector2(2 / Seeker.PPM, 6 / Seeker.PPM));
        fdef.filter.categoryBits = Seeker.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        timeToRedefineMario = false;
    }

    public void throwAxe(){
        axes.add(new Axe(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
    }

    public void draw(Batch batch) {
        super.draw(batch);
        for (Axe ball : axes)
            ball.draw(batch);
    }

}

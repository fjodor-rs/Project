package nl.mprog.com.seeker.game.sprites.weapons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.screens.PlayScreen;

/**
 * Created by Fjodor on 2017/01/24.
 */

public class Axe extends Sprite {

    PlayScreen screen;
    World world;
    Array<TextureRegion> frames;
    TextureRegion axeTexture;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;

    Body body;
    public Axe(PlayScreen screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = world;
        setRegion(screen.getAtlas().findRegion("viking_axe"), 112, 112, 32, 32);
        setBounds(x, y, 12 / Seeker.PPM, 12 / Seeker.PPM);
        defineAxe();
    }

    private void defineAxe() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / Seeker.PPM);
        fdef.filter.categoryBits = Seeker.AXE_BIT;
        fdef.filter.maskBits = Seeker.GROUND_BIT |
                Seeker.COIN_BIT |
                Seeker.BRICK_BIT |
                Seeker.ENEMY_BIT |
                Seeker.OBJECT_BIT;

        fdef.shape = shape;
        fdef.restitution = 1;
        fdef.friction = 0;
        body.createFixture(fdef).setUserData(this);
        body.setLinearVelocity(new Vector2(fireRight ? 2 : -2, 2.5f));
    }

    public void update(float dt){
        stateTime += dt;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        if((stateTime > 3 || setToDestroy) && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
        if(body.getLinearVelocity().y > 2f)
            body.setLinearVelocity(body.getLinearVelocity().x, 2f);
        if((fireRight && body.getLinearVelocity().x < 0) || (!fireRight && body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}

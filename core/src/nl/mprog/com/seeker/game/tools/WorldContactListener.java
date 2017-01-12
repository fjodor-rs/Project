package nl.mprog.com.seeker.game.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.awt.event.ContainerListener;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.sprites.Enemy;
import nl.mprog.com.seeker.game.sprites.InteractiveTileObject;

/**
 * Created by Fjodor on 2017/01/12.
 */

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if(object.getUserData() instanceof InteractiveTileObject){
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        switch(cDef){
            case Seeker.ENEMY_HEAD_BIT | Seeker.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == Seeker.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            case Seeker.ENEMY_BIT | Seeker.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Seeker.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

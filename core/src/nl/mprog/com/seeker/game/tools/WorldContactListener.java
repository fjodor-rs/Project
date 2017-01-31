package nl.mprog.com.seeker.game.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.sprites.Jaap;
import nl.mprog.com.seeker.game.sprites.enemies.Enemy;
import nl.mprog.com.seeker.game.sprites.tileobjects.InteractiveTileObject;
import nl.mprog.com.seeker.game.sprites.items.Item;
import nl.mprog.com.seeker.game.sprites.weapons.Axe;

/**
 * Created by Fjodor on 2017/01/12.
 */

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch(cDef){
            case Seeker.JAAP_HEAD_BIT | Seeker.BRICK_BIT:
            case Seeker.JAAP_HEAD_BIT | Seeker.COIN_BIT:
                if(fixA.getFilterData().categoryBits == Seeker.JAAP_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Jaap) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Jaap) fixB.getUserData());
                break;
            case Seeker.ENEMY_HEAD_BIT | Seeker.JAAP_BIT:
                if(fixA.getFilterData().categoryBits == Seeker.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead((Jaap) fixB.getUserData()) ;
                else
                    ((Enemy)fixB.getUserData()).hitOnHead((Jaap) fixA.getUserData()) ;
                break;
            case Seeker.ENEMY_BIT | Seeker.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Seeker.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Seeker.JAAP_BIT | Seeker.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Seeker.JAAP_BIT)
                    ((Jaap) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                else
                    ((Jaap) fixB.getUserData()).hit((Enemy)fixA.getUserData());
                break;
            case Seeker.ENEMY_BIT | Seeker.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).hitByEnemy((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).hitByEnemy((Enemy)fixA.getUserData());
                break;
            case Seeker.ITEM_BIT | Seeker.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Seeker.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Seeker.ITEM_BIT | Seeker.JAAP_BIT:
                if(fixA.getFilterData().categoryBits == Seeker.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Jaap) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Jaap) fixA.getUserData());
                break;
            case Seeker.JAAP_BIT | Seeker.END_BIT:
                if(fixA.getFilterData().categoryBits == Seeker.JAAP_BIT)
                    ((Jaap) fixA.getUserData()).win();
                else
                    ((Jaap) fixB.getUserData()).win();
                break;
            case Seeker.AXE_BIT | Seeker.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Seeker.AXE_BIT)
                    ((Axe)fixA.getUserData()).setToDestroy();
                else
                    ((Axe)fixB.getUserData()).setToDestroy();
                break;
            case Seeker.BRICK_BIT | Seeker.JAAP_SMASH_BIT:
                if(fixA.getFilterData().categoryBits == Seeker.JAAP_SMASH_BIT) {
                    ((Jaap) fixA.getUserData()).setTouching((InteractiveTileObject) fixB.getUserData());
                }
                else {
                    ((Jaap) fixB.getUserData()).setTouching((InteractiveTileObject) fixA.getUserData());
                }
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {


        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch(cDef) {
            case Seeker.BRICK_BIT | Seeker.JAAP_SMASH_BIT:
                if (fixA.getFilterData().categoryBits == Seeker.JAAP_SMASH_BIT) {
                    ((Jaap) fixA.getUserData()).setTouching((null));
                } else {
                    ((Jaap) fixB.getUserData()).setTouching((null));
                }
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

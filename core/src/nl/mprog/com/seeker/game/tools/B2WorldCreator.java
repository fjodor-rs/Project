package nl.mprog.com.seeker.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.sprites.enemies.Enemy;
import nl.mprog.com.seeker.game.sprites.enemies.Turtle;
import nl.mprog.com.seeker.game.sprites.tileobjects.Brick;
import nl.mprog.com.seeker.game.sprites.tileobjects.CoinBlock;
import nl.mprog.com.seeker.game.sprites.enemies.Goomba;
import nl.mprog.com.seeker.game.sprites.tileobjects.EndBlock;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * Builds all the object layers loaded in from tiled into the correct objects.
 * This is used for blocks and for enemy sprites.
 */

public class B2WorldCreator {
    private static final int OBJECT_LAYERS = 10;
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap tiledMap = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        goombas = new Array<Goomba>();
        turtles = new Array<Turtle>();

        for (int i = 2; i < OBJECT_LAYERS; i++){
            for (MapObject object : tiledMap.getLayers().get(i).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                if (i < 4){
                    bdef.type = BodyDef.BodyType.StaticBody;
                    bdef.position.set((rect.getX() + rect.getWidth() / 2) / Seeker.PPM,
                            (rect.getY() + rect.getHeight() / 2) / Seeker.PPM);

                    body = world.createBody(bdef);

                    shape.setAsBox(rect.getWidth() / 2 / Seeker.PPM, rect.getHeight() / 2 / Seeker.PPM);
                    fdef.shape = shape;
                    body.createFixture(fdef);
                }
                else if (i == 4){
                    bdef.type = BodyDef.BodyType.StaticBody;
                    bdef.position.set((rect.getX() + rect.getWidth() / 2) / Seeker.PPM,
                            (rect.getY() + rect.getHeight() / 2) / Seeker.PPM);

                    body = world.createBody(bdef);

                    shape.setAsBox(rect.getWidth() / 2 / Seeker.PPM, rect.getHeight() / 2 / Seeker.PPM);
                    fdef.shape = shape;
                    fdef.filter.categoryBits = Seeker.OBJECT_BIT;
                    body.createFixture(fdef);

                }
                else if (i == 5){
                    new Brick(screen, object);
                }
                else if (i == 6){
                    new CoinBlock(screen, object);
                }
                else if (i == 7){
                    goombas.add(new Goomba(screen, rect.getX() / Seeker.PPM, rect.getY() / Seeker.PPM));
                }
                else if (i == 8){
                    turtles.add(new Turtle(screen, rect.getX() / Seeker.PPM, rect.getY() / Seeker.PPM));
                }
                else if (i == 9){
                   new EndBlock(screen, object);
                }
            }
        }
    }
    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }
}

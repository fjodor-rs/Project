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

import nl.mprog.com.seeker.game.Seeker;
import nl.mprog.com.seeker.game.screens.PlayScreen;
import nl.mprog.com.seeker.game.sprites.Brick;
import nl.mprog.com.seeker.game.sprites.CoinBlock;

/**
 * Created by Fjodor on 2017/01/10.
 */

public class B2WorldCreator {
    private static final int OBJECT_LAYERS = 7;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap tiledMap = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (int i = 2; i < OBJECT_LAYERS; i++){
            for (MapObject object : tiledMap.getLayers().get(i).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                if (i < 4){
                    bdef.type = BodyDef.BodyType.StaticBody;
                    bdef.position.set((rect.getX() + rect.getWidth() / 2) / Seeker.PPM, (rect.getY() + rect.getHeight() / 2) / Seeker.PPM);

                    body = world.createBody(bdef);

                    shape.setAsBox(rect.getWidth() / 2 / Seeker.PPM, rect.getHeight() / 2 / Seeker.PPM);
                    fdef.shape = shape;
                    body.createFixture(fdef);
                }
                else if (i == 5){
                    new Brick(screen, rect);
                }
                else if (i == 6){
                    new CoinBlock(screen, rect);
                }
            }
        }
    }
}

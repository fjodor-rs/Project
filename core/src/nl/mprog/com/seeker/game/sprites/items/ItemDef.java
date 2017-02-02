package nl.mprog.com.seeker.game.sprites.items;

import com.badlogic.gdx.math.Vector2;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * This class defines am item's type and position.
 */

public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type){
        this.position = position;
        this.type = type;
    }
}

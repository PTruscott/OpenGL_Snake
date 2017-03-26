package client.blocks;

import client.graphics.Colour;

/**
 * Created by peran on 3/25/17.
 * An object used as the foundation for all game objects
 */
public abstract class Block {
    boolean collidable;
    Colour colour;
    int phase;

    public boolean isCollidable() {
        return collidable;
    }

    public Colour getColour() {
        return colour;
    }
}

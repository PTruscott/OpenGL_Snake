package client.blocks;

import client.graphics.Colour;

public class Air extends Block {
    public Air() {
        collidable = false;
        colour = new Colour(0, 0, 0,0);
    }
}

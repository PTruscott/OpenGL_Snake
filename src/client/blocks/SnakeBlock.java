package client.blocks;

import client.Vector2;
import client.graphics.Colour;

/**
 * Created by peran on 3/26/17.
 * the main snake class
 */
public class SnakeBlock extends Block{
    private boolean head;
    private Vector2 pos;
    private int phase;

    public SnakeBlock(boolean head, Vector2 pos, int phase) {
        this.head = head;
        this.phase = phase;
        this.colour = new Colour(1,1,1);
        this.collidable = true;
        this.pos = pos;
    }

    public boolean isHead() {
        return head;
    }

    public void demote() {
        head = false;
    }

    public Vector2 getPos() {
        return pos;
    }

    public int getPhase() {
        return phase;
    }
}

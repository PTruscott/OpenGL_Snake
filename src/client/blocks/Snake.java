package client.blocks;

import client.Vector2;
import client.graphics.Colour;

/**
 * Created by peran on 3/26/17.
 * the main snake class
 */
public class Snake extends Block{
    private boolean head;
    private Vector2 tail;

    public Snake(boolean head, Vector2 tail) {
        this.head = head;
        this.colour = new Colour(1,1,1);
        this.collidable = true;
        this.tail = tail;
    }

    public boolean isHead() {
        return head;
    }

    public Vector2 getTail() {
        return tail;
    }

    public void setTail(Vector2 tail) {
        this.tail = tail;
    }
}

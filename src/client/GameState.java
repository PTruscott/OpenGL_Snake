package client;

import client.blocks.Block;
import client.blocks.Snake;

import static client.ClientSettings.STARTING_LENGTH;

/**
 * Created by peran on 3/26/17.
 * Used to control the current game state
 */
public class GameState {
    private Block[][] blocks;
    private int phase;
    private Vector2 head;
    private int snakeLength;

    GameState(Block[][] blocks, int phase, Vector2 head) {
        this.blocks = blocks;
        this.phase = phase;
        this.head = head;
        snakeLength = STARTING_LENGTH;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public Block getBlock(Vector2 pos) {
        return blocks[(int)pos.getX()][(int)pos.getY()];
    }

    public void setBlock(Block b, Vector2 pos) {
        blocks[(int)pos.getX()][(int)pos.getY()] = b;
    }

    public void clearBlock(Vector2 pos) {
        blocks[(int)pos.getX()][(int)pos.getY()] = null;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public Vector2 getHeadPos() {
        return head;
    }

    public Block getHead() {
        return getBlock(head);
    }

    public void setHeadPos(Vector2 head) {
        this.head = head;
    }

    public int getSnakeLength() {
        return snakeLength;
    }

    public void growSnake(int growth) {
        snakeLength+=growth;
    }

    public void setSnakeLength(int length) {
        snakeLength = length;
    }
}

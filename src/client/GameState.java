package client;

import client.blocks.Block;
import client.blocks.Food;
import client.blocks.Snake;
import client.blocks.Wall;

import java.util.Random;

import static client.ClientSettings.*;

/**
 * Created by peran on 3/26/17.
 * Used to control the current game state
 */
public class GameState {
    private Block[][][] blocks;
    private int phase;
    private Vector2 head;
    private int snakeLength;
    private boolean gameRunning;

    public GameState() {
        head = new Vector2(7, 10);
        snakeLength = STARTING_LENGTH;
        gameRunning = false;
        Random rand = new Random();
        phase = rand.nextInt(4);

        blocks = new Block[4][getMapWidth()][getMapHeight()];
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < getMapWidth(); x++) {
                for (int y = 0; y < getMapHeight(); y++) {
                    if (x == 0 || y == 0 || y == getMapHeight()-1 || x == getMapWidth()-1) {
                        blocks[i][x][y] = new Wall(i);
                    }
                }
            }
        }

        blocks[phase][7][10] = new Snake(true, null);

        blocks[phase][10][17] = new Food(phase);

    }

    public int getMapWidth() {
        return SCREEN_WIDTH/BLOCK_SIZE;
    }

    public int getMapHeight() {
        return SCREEN_HEIGHT/BLOCK_SIZE;
    }

    public boolean isRunning() {
        return gameRunning;
    }

    public void endGame() {
        gameRunning = false;
    }

    public void startGame() {
        gameRunning = true;
    }

    public Block[][] getBlocks() {
        return blocks[phase];
    }

    public Block getBlock(Vector2 pos) {
        return blocks[phase][(int)pos.getX()][(int)pos.getY()];
    }

    public void setBlock(Block b, Vector2 pos) {
        blocks[phase][(int)pos.getX()][(int)pos.getY()] = b;
    }

    public void clearBlock(Vector2 pos) {
        blocks[phase][(int)pos.getX()][(int)pos.getY()] = null;
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

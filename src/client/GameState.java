package client;

import client.blocks.*;
import client.graphics.PhaseRipple;
import simplexNoise.SimplexNoise;

import java.util.ArrayList;
import java.util.Random;

import static client.ClientSettings.*;

/**
 * Created by peran on 3/26/17.
 * Used to control the current game state
 */
public class GameState {
    private Block[][][] blocks;
    private int phase;
    private int snakeLength;
    private boolean gameRunning;
    private int portalRotation;
    private PhaseRipple ripple;
    private boolean menu;

    GameState(boolean menu) {
        this.menu = menu;
        snakeLength = STARTING_LENGTH;
        gameRunning = false;
        Random rand = new Random();
        phase = rand.nextInt(4);
        portalRotation = 0;

        ripple = new PhaseRipple(0, 0, 0, 0);
        ripple.kill();

        generateMap();
        generatePortals();

        blocks[phase][10][17] = new Food(phase);
        int nextPhase = (phase+1)%4;
        blocks[nextPhase][getMapWidth()/2][STARTING_RUNWAY] = new Food(nextPhase);

    }

    private void generatePortals() {
        if (!menu) {
            ArrayList<Integer> phases = new ArrayList<>();
            phases.add(0);
            phases.add(1);
            phases.add(2);
            phases.add(3);

            Random rand = new Random();
            while (phases.size() > 2) {
                int otherPhase = phases.get(rand.nextInt(phases.size() - 2) + 1);
                int otherPhase2 = phases.get(rand.nextInt(phases.size() - 2) + 1);
                if (otherPhase == otherPhase2) otherPhase2 = phases.get(phases.size() - 1);

                int phase = phases.get(0);

                Vector2 pos = generateCoords(phase);

                makeRunway(phase, pos);
                makeRunway(otherPhase, pos);
                setBlock(phase, new Portal(phase, otherPhase), pos);
                setBlock(otherPhase, new Portal(otherPhase, phase), pos);

                pos = generateCoords(phase);

                makeRunway(phase, pos);
                makeRunway(otherPhase2, pos);
                setBlock(phase, new Portal(phase, otherPhase2), pos);
                setBlock(otherPhase2, new Portal(otherPhase2, phase), pos);

                phases.remove(0);
            }
        }
        else {
            int phase;
            int otherPhase;
            for (int i = 0; i < 4; i++) {
                phase = (this.phase+i)%4;
                otherPhase = (phase+1)%4;
                Vector2 pos;
                switch (i) {
                    default:
                    case 0:
                        pos = new Vector2(STARTING_RUNWAY, STARTING_RUNWAY);
                        break;
                    case 1:
                        pos = new Vector2(getMapWidth()-STARTING_RUNWAY, STARTING_RUNWAY);
                        break;
                    case 2:
                        pos = new Vector2(getMapWidth()-STARTING_RUNWAY, getMapHeight()-STARTING_RUNWAY);
                        break;
                    case 3:
                        pos = new Vector2(STARTING_RUNWAY, getMapHeight()-STARTING_RUNWAY);
                        break;
                }

                setBlock(phase, new Portal(phase, otherPhase), pos);
                setBlock(otherPhase, new Portal(otherPhase, phase), pos);
            }
        }
    }

    private void makeRunway(int phase, Vector2 pos) {
        int x = (int) pos.getX();
        int y = (int) pos.getY();
        for (int i = 0; i < STARTING_RUNWAY*2+1; i++) {
            clearBlock(phase, new Vector2(x+i-STARTING_RUNWAY, y));
            clearBlock(phase, new Vector2(x, y+i-STARTING_RUNWAY));

        }
    }

    private Vector2 generateCoords(int phase) {
        Random rand = new Random();
        Vector2 pos = new Vector2(0, 0);
        while (!(getBlock(phase, pos) instanceof Air)) {
            int x = rand.nextInt(getMapWidth()-(STARTING_RUNWAY+1)*2)+(STARTING_RUNWAY+1);
            int y = rand.nextInt(getMapHeight()-(STARTING_RUNWAY+1)*2)+(STARTING_RUNWAY+1);
            pos = new Vector2(x, y);
        }

        return pos;
    }

    private void generateMap() {
        Random rand = new Random();

        blocks = new Block[4][getMapWidth()][getMapHeight()];

        double xStart=0;
        double XEnd=500;
        double yStart=0;
        double yEnd=500;


        for (int i = 0; i < 4; i++) {
            SimplexNoise simplexNoise=new SimplexNoise(100,0.1, rand.nextInt());
            for (int x = 0; x < getMapWidth(); x++) {
                for (int y = 0; y < getMapHeight(); y++) {
                    if (x == 0 || y == 0 || y == getMapHeight()-1 || x == getMapWidth()-1) {
                        blocks[i][x][y] = new Wall(i);
                    }
                    else if (RANDOM_MAP && !menu){
                        int xNoise = (int)(xStart+x*((XEnd-xStart)/getMapWidth()));
                        int yNoise = (int)(yStart+y*((yEnd-yStart)/getMapHeight()));
                        double val = simplexNoise.getNoise(xNoise,yNoise);
                        if (val < -0.06) {
                            blocks[i][x][y] = new Wall(i);
                        }
                        else {
                            blocks[i][x][y] = new Air();
                        }
                    }
                    else {
                        blocks[i][x][y] = new Air();
                    }
                }
            }
        }
    }

    public int getMapWidth() {
        return SCREEN_WIDTH/BLOCK_SIZE;
    }

    public int getMapHeight() {
        return SCREEN_HEIGHT/BLOCK_SIZE;
    }

    boolean isRunning() {
        return gameRunning;
    }

    void endGame() {
        gameRunning = false;
    }

    void startGame() {
        gameRunning = true;
    }

    public Block[][] getBlocks(int phase) {
        return blocks[phase];

    }

    private Block getBlock(int phase, Vector2 pos) {
        return blocks[phase][(int)pos.getX()][(int)pos.getY()];
    }

    Block getBlock(Vector2 pos) {
        return getBlock(phase, pos);
    }

    void setBlock(int phase, Block b, Vector2 pos) {
        blocks[phase][(int)pos.getX()][(int)pos.getY()] = b;

    }

    private void clearBlock(int phase, Vector2 pos) {
        blocks[phase][(int)pos.getX()][(int)pos.getY()] = new Air();
    }

    void clearBlock(Vector2 pos) {
        clearBlock(phase, pos);
    }

    public int getPhase() {
        return phase;
    }

    void setPhase(int phase) {
        this.phase = phase;
    }

    public int getSnakeLength() {
        return snakeLength;
    }

    void growSnake(int growth) {
        snakeLength+=growth;
    }

    void rotatePortal(float delta) {
        portalRotation %= 360;
        portalRotation += PORTAL_SPEED*delta;
    }

    public int getPortalRotation() {
        return portalRotation;
    }

    public PhaseRipple getRipple() {
        return ripple;
    }

    void setRipple(PhaseRipple ripple) {
        this.ripple = ripple;
    }

    public boolean isMenu() {
        return menu;
    }
}

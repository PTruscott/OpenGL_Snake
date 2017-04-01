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

    public GameState() {
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
        blocks[nextPhase][13][4] = new Food(nextPhase);

    }

    private void generatePortals() {
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
            blocks[phases.get(0)][7*phases.size()][20] = new Portal(phases.get(0), otherPhase);
            blocks[otherPhase][7*phases.size()][20] = new Portal(otherPhase, phases.get(0));

            blocks[phases.get(0)][12][7*phases.size()] = new Portal(phases.get(0), otherPhase2);
            blocks[otherPhase2][12][7*phases.size()] = new Portal(otherPhase2, phases.get(0));

            phases.remove(0);
        }

    }

    private void generateMap() {
        Random rand = new Random();

        blocks = new Block[4][getMapWidth()][getMapHeight()];

        SimplexNoise simplexNoise=new SimplexNoise(100,0.1, rand.nextInt(10000));

        double xStart=0;
        double XEnd=500;
        double yStart=0;
        double yEnd=500;

        double[][] result=new double[getMapWidth()][getMapHeight()];

        for (int  i= 0; i < getMapWidth(); i++) {
            for (int j = 0; j < getMapHeight(); j++){
                int x = (int)(xStart+i*((XEnd-xStart)/getMapWidth()));
                int y = (int)(yStart+j*((yEnd-yStart)/getMapHeight()));
                result[i][j]=0.5*(1+simplexNoise.getNoise(x,y));
            }
        }



        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < getMapWidth(); x++) {
                for (int y = 0; y < getMapHeight(); y++) {
                    if (x == 0 || y == 0 || y == getMapHeight()-1 || x == getMapWidth()-1) {
                        blocks[i][x][y] = new Wall(i);
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

    public boolean isRunning() {
        return gameRunning;
    }

    public void endGame() {
        gameRunning = false;
    }

    public void startGame() {
        gameRunning = true;
    }

    public Block[][] getBlocks(int phase) {
        return blocks[phase];

    }

    public Block getBlock(Vector2 pos) {
        return blocks[phase][(int)pos.getX()][(int)pos.getY()];
    }

    public void setBlock(Block b, Vector2 pos) {
        setBlock(phase, b, pos);
    }

    public void setBlock(int phase, Block b, Vector2 pos) {
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

    public int getSnakeLength() {
        return snakeLength;
    }

    public void growSnake(int growth) {
        snakeLength+=growth;
    }

    public void rotatePortal(float delta) {
        portalRotation %= 360;
        portalRotation += PORTAL_SPEED*delta;
    }

    public int getPortalRotation() {
        return portalRotation;
    }

    public PhaseRipple getRipple() {
        return ripple;
    }

    public void setRipple(PhaseRipple ripple) {
        this.ripple = ripple;
    }
}

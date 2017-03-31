package client;

import client.graphics.Colour;

/**
 * Stores all variables that should be accessible anywhere for the Client.
 */
public class ClientSettings {

    public static final boolean DEBUG = true;

    // ***** DISPLAY SETTINGS *****

    static final String WINDOW_TITLE = "Snake";
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    static final boolean FULLSCREEN = false;

    // ***** GAME SETTINGS *****

    public static final int BLOCK_SIZE = 20;
    public static final int STARTING_LENGTH = 10;
    public static final float SNAKE_SPEED = 1;

    // ***** GRAPHICS SETTINGS *****
    public static Colour[] PHASE_COLOURS = {
      new Colour(1, 0, 0), //phase 0
      new Colour(0, 1, 0), //phase 1
      new Colour(0, 0, 1), //phase 2
      new Colour(1, 1, 0)  //phase 3
    };
    public static float MIN_SNAKE_GLOW = 0.2f;



}

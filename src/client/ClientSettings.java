package client;

import client.graphics.Colour;

/**
 * Stores all variables that should be accessible anywhere for the Client.
 */
public class ClientSettings {

    static final boolean DEBUG = true;

    // ***** DISPLAY SETTINGS *****

    static final String WINDOW_TITLE = "SnakeBlock";
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    static final boolean FULLSCREEN = false;

    // ***** GAME SETTINGS *****

    public static final int BLOCK_SIZE = 20;
    public static final int STARTING_LENGTH = 4;
    static final float SNAKE_SPEED = 1;
    public static final int FOOD_REWARD  = 2;
    static final Vector2 STARTING_POS = new Vector2(10, 10);
    static final int STARTING_RUNWAY = 6;
    static boolean RANDOM_MAP = true;

    // ***** GRAPHICS SETTINGS *****
    public static Colour[] PHASE_COLOURS = {
      new Colour(1, 0, 0), //phase 0
      new Colour(0, 1, 0), //phase 1
      new Colour(0, 0, 1), //phase 2
      new Colour(1, 1, 0)  //phase 3
    };
    static final float MIN_SNAKE_GLOW = 0.2f;
    static final float PORTAL_SPEED = 6;
    public static final float RIPPLE_SPEED = 1;
    public static boolean DISPLAY_FPS = false;
    public static boolean DISPLAY_SCORE = true;
}

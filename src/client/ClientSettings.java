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
    static final int STARTING_LENGTH = 10;
    public static final float SNAKE_SPEED = 1;

    // ***** AUDIO SETTINGS *****
    public static final float MIN_VOLUME = -80f;
    public static final float AMBIENT_VOL = 0.8f;
    public static final float SHOOTING_VOL = 0.95f;
    public static final float HEALTH_UP_VOL = 1f;
    public static final float PULSE_VOL = 0.9f;
    public static final float WARNING_VOL = 0.45f;
    public static final float WARNING_THRES = 40;
    public static final float CAUTION_VOL = 0.9f;
    public static final float VOICE_VOL = 0.9f;
    //User configurable
    public static float MUSIC_VOL = 0.5f;
    public static float SOUND_VOL = 0.5f;

    // ***** GRAPHICS SETTINGS *****
    public static Colour[] PHASE_COLOURS = {
      new Colour(1, 0, 0), //phase 0
      new Colour(0, 1, 0), //phase 1
      new Colour(0, 0, 1), //phase 2
      new Colour(0, 1, 1)  //phase 3
    };



}

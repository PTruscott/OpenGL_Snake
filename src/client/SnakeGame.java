package client;

import client.graphics.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import static client.ClientSettings.SCREEN_HEIGHT;
import static client.ClientSettings.SCREEN_WIDTH;
import static org.lwjgl.opengl.GL11.*;

public class SnakeGame {

    private GameManager gameManager;

    private TextRenderer[] textRenderers = new TextRenderer[3];

    private boolean running = true;

    private SnakeGame() {
        initialise();
        changeDisplaySettings();
        beginGame();
        loop();
    }

    private void loop() {
        while (!Display.isCloseRequested() && running) {
            // clear screen
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

            // let subsystem paint
            gameManager.run();

            // update window contents
            Display.update();
            Display.sync(60);
        }
    }

    private void changeDisplaySettings() {
        GL11.glDisable(GL_TEXTURE_2D); // Enable Texture Mapping
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, SCREEN_WIDTH, 0, ClientSettings.SCREEN_HEIGHT, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void initialise() {
        // initialize the window beforehand
        try {
            //setDisplayMode();
            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            Display.setTitle(ClientSettings.WINDOW_TITLE);
            Display.setFullscreen(ClientSettings.FULLSCREEN);
            Display.create();

            GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
            GL11.glClearDepth(1.0f); // Depth Buffer Setup
            GL11.glDisable(GL11.GL_DEPTH_TEST); // Disables Depth Testing
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDepthMask(false);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            textRenderers[0] = new TextRenderer(20);
            textRenderers[1] = new TextRenderer(25);
            textRenderers[2] = new TextRenderer(60);

            //Audio.init();
            //AudioManager.playAmbiance();
        } catch (LWJGLException le) {
            System.err.println("Game exiting - exception in initialization:");
            le.printStackTrace();
            running = false;
        }
    }

    private void beginGame() {
        gameManager = new GameManager(textRenderers);
    }

    public static void main(String argv[]) {
        new SnakeGame();
    }
}
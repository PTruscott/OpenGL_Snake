package client.graphics;

import client.GameState;
import client.Vector2;
import client.blocks.Food;
import client.blocks.Snake;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.Random;

import static client.ClientSettings.*;

/**
 * Provides the visuals for the game itself.
 */
public class GameManager {


    private GameRenderer gameRenderer;

    private long lastFrame;
    private int fps;
    private long lastFPS;
    private Vector2 dir;
    private GameState game;
    private float counter;


    public GameManager(GameState game, TextRenderer[] textRenderers) {
        super();
        this.game = game;
        counter = 0;
        dir = new Vector2(1, 0);
        gameRenderer = new GameRenderer(game, textRenderers);
        lastFrame = getTime();
    }

    public void run() {
        update();
        gameRenderer.render();
    }

    private void update() {
        makeMovement();
        pollKeyboard();
        updateSnake();

        //pollMouse();
        //AudioManager.playWarningSounds(gameData.getPlayer(myPlayerID).getHealth());
        //updateFPS(); // update FPS Counter
    }

    private void updateSnake() {
        Snake head = (Snake) game.getHead();
        updateSnake(head, 0);
    }

    private void updateSnake(Snake snake, int length) {
        if (snake != null) {
            if (snake.getTail() != null) {
                updateSnake((Snake) game.getBlock(snake.getTail()), length + 1);
                if (length == game.getSnakeLength()) {
                    game.clearBlock(snake.getTail());
                    snake.setTail(null);
                }
            }
            if (length < game.getSnakeLength()) {
                snake.getColour().intensity = (game.getSnakeLength() - (float)length) / game.getSnakeLength();
            }
        }
        else out("Why is this null??");
    }


    private void pollMouse() {

        while (Mouse.next()) {
            switch (Mouse.getEventButton()) {
                //left click
                case 0:
                    if (Mouse.getEventButtonState()) {
                        //click down
                    } else {
                        //click up
                    }
            }
        }

    }

    private void makeMovement() {
        counter += SNAKE_SPEED*getDelta();
        if (counter > 100) {
            Vector2 pos = game.getHeadPos();

            Vector2 newPos = pos.add(dir);

            if (validPos(newPos)) {
                if (game.getBlock(newPos) instanceof Food) {
                    game.growSnake(((Food) game.getBlock(newPos)).getGrowth());
                    game.setBlock(new Food(0), respawnPos());
                }
                game.setBlock(new Snake(true, pos), newPos);
                game.setBlock(new Snake(false, ((Snake) game.getBlock(game.getHeadPos())).getTail()), pos);
                game.setHeadPos(newPos);
            }

            counter -= 100;
        }
    }

    private Vector2 respawnPos() {
        Random r = new Random();
        int x=-1;
        int y=-1;
        boolean valid = false;
        while (!valid) {
            x = r.nextInt(SCREEN_WIDTH/BLOCK_SIZE-1);
            y = r.nextInt(SCREEN_HEIGHT/BLOCK_SIZE-1);
            valid = validPos(x, y);
        }
        return new Vector2(x, y);
    }

    private boolean validPos(int x, int y) {
        return validPos(new Vector2(x, y));
    }

    private boolean validPos(Vector2 pos) {
        if (pos.getX() > SCREEN_WIDTH / BLOCK_SIZE - 1) {
            return false;
        } else if (pos.getY() > SCREEN_HEIGHT / BLOCK_SIZE - 1) {
            return false;
        } else if (pos.getX() < 0 || pos.getY() < 0) {
            return false;
        } else if (game.getBlock(pos) == null) {
            return true;
        }
        return !game.getBlock(pos).isCollidable();
    }

    private void pollKeyboard() {
        while (Keyboard.next()) {
            // Runs if next key has been PRESSED.
            gameKeyboard();
        }
    }

    private void gameKeyboard() {
        switch (Keyboard.getEventKey()) {
            case Keyboard.KEY_W:
                if (!dir.equals(0, 1)) {
                    dir = new Vector2(0, -1);
                }
                break;
            case Keyboard.KEY_A:
                if (!dir.equals(1, 0)) {
                    dir = new Vector2(-1, 0);
                }
                break;
            case Keyboard.KEY_S:
                if (!dir.equals(0, -1)) {
                    dir = new Vector2(0, 1);
                }
                break;
            case Keyboard.KEY_D:
                if (!dir.equals(-1, 0)) {
                    dir = new Vector2(1, 0);
                }

            case Keyboard.KEY_M:
                if (SOUND_VOL == 0) {
                    MUSIC_VOL = 1;
                    SOUND_VOL = 1;
                } else {
                    SOUND_VOL = 0;
                    MUSIC_VOL = 0;
                    //AudioManager.muteEverything();
                }
                break;
        }
    }

    private long getTime() {
        return (System.currentTimeMillis());
    }

    private int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;
        return delta;
    }

    private void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    public static void out(Object o) {
        if (DEBUG) System.out.println(o);
    }
}

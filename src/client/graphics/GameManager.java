package client.graphics;

import client.GameState;
import client.Vector2;
import client.blocks.Food;
import client.blocks.Snake;
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
    private boolean firstGame;


    public GameManager(TextRenderer[] textRenderers) {
        super();
        firstGame = true;
        resetGame();
        gameRenderer = new GameRenderer(game, textRenderers);
        lastFrame = getTime();
    }

    public void run() {
        update();
        if (game.isRunning()) {
            gameRenderer.render();
        }
        else {
            gameRenderer.drawMenu(!firstGame);
        }
    }

    private void update() {
        pollKeyboard();
        if (game.isRunning()) {
            makeMovement();
            updateSnake();
        }

        //pollMouse();
        //updateFPS(); // update FPS Counter
    }

    private void updateSnake() {
        Snake head = (Snake) game.getHead();
        updateSnake(head, 0);
    }

    private void updateSnake(Snake snake, int length) {
        if (snake.getTail() != null) {
            updateSnake((Snake) game.getBlock(snake.getTail()), length + 1);
            if (length == game.getSnakeLength()) {
                game.clearBlock(snake.getTail());
                snake.setTail(null);
            }
        }
        if (length < game.getSnakeLength()) {
            snake.getColour().intensity = ((game.getSnakeLength() - (float)length) / game.getSnakeLength())*(1-MIN_SNAKE_GLOW) + MIN_SNAKE_GLOW;
        }
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
                    game.setBlock(new Food(game.getPhase()), respawnPos());
                }
                game.setBlock(new Snake(true, pos), newPos);
                game.setBlock(new Snake(false, ((Snake) game.getBlock(game.getHeadPos())).getTail()), pos);
                game.setHeadPos(newPos);
            }

            else game.endGame();

            counter -= 100;
        }
    }

    private Vector2 respawnPos() {
        Random r = new Random();
        int x=-1;
        int y=-1;
        boolean valid = false;
        while (!valid) {
            x = r.nextInt(game.getMapWidth()-1);
            y = r.nextInt(game.getMapHeight()-1);
            valid = validPos(x, y);
        }
        return new Vector2(x, y);
    }

    private boolean validPos(int x, int y) {
        return validPos(new Vector2(x, y));
    }

    private boolean validPos(Vector2 pos) {
        if (pos.getX() > game.getMapWidth() - 1) {
            return false;
        } else if (pos.getY() > game.getMapHeight() - 1) {
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
            if (game.isRunning()) {
                gameKeyboard();
            }
            //If it's game over
            else {
                menuKeyboard();
            }
        }
    }

    private void menuKeyboard() {
        switch ((Keyboard.getEventKey())) {
            case Keyboard.KEY_SPACE:
                firstGame = false;
                resetGame();
                break;
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

    private void resetGame() {
        counter = 0;
        game = new GameState(new Vector2(7, 10));
        if (gameRenderer != null) {
            gameRenderer.updateGameState(game);
        }

        if (!firstGame) {
            game.startGame();
            dir = new Vector2(1, 0);
        }
    }

    public static void out(Object o) {
        if (DEBUG) System.out.println(o);
    }
}

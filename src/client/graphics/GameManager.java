package client.graphics;

import client.GameState;
import client.Vector2;
import client.blocks.Food;
import client.blocks.Portal;
import client.blocks.SnakeBlock;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.LinkedList;
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
    private LinkedList<SnakeBlock> snake;


    public GameManager(TextRenderer[] textRenderers) {
        super();
        firstGame = true;
        snake = new LinkedList<>();
        resetGame();
        gameRenderer = new GameRenderer(game, textRenderers, snake);
        lastFrame = getTime();
    }

    public void run() {
        update();
        if (game.isRunning()) {
            if (game.getRipple().isAlive()) {
                gameRenderer.drawStencil();
            }
            else {
                gameRenderer.render();
            }
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

        //updateFPS(); // update FPS Counter
    }

    private void updateSnake() {
        int length = game.getSnakeLength();

        for (int i = 1; i < snake.size(); i++) {
            snake.get(i).demote();
            snake.get(i).getColour().intensity = ((game.getSnakeLength() - (float)i) / game.getSnakeLength())*(1-MIN_SNAKE_GLOW) + MIN_SNAKE_GLOW;
        }

        if (snake.size() > length) {
            snake.subList(length, snake.size()).clear();
        }

    }

    private void makeMovement() {
        float delta = getDelta();

        counter += SNAKE_SPEED*delta;
        game.rotatePortal(delta);

        if (game.getRipple().isAlive()) {
            game.getRipple().spread(delta);
        }

        if (counter > 100) {
            Vector2 pos = snake.get(0).getPos();

            Vector2 newPos = pos.add(dir);

            if (game.getBlock(newPos) instanceof Portal) {
                int oldPhase = game.getPhase();
                int newPhase = ((Portal) game.getBlock(newPos)).getOtherPhase();
                game.setPhase(newPhase);
                Vector2 temp = newPos.clone();
                temp = temp.mult(BLOCK_SIZE);
                game.setRipple(new PhaseRipple(temp.getX()+BLOCK_SIZE/2, temp.getY() + BLOCK_SIZE/2, oldPhase, newPhase));
                gameRenderer.updateGameState(game);
            }

            if (validPos(newPos)) {
                if (game.getBlock(newPos) instanceof Food) {
                    game.growSnake(((Food) game.getBlock(newPos)).getGrowth());
                    game.clearBlock(newPos);
                    Random rand = new Random();
                    int foodPhase = rand.nextInt(4)
                    game.setBlock(foodPhase, new Food(foodPhase), respawnPos());
                }
                snake.add(0, new SnakeBlock(true, newPos, game.getPhase()));
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
        for (SnakeBlock s: snake) {
            if (s.getPos().equals(pos) && s.getPhase() == game.getPhase()) return false;
        }
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
            case Keyboard.KEY_UP:
            case Keyboard.KEY_W:
                if (!dir.equals(0, 1)) {
                    dir = new Vector2(0, -1);
                }
                break;
            case Keyboard.KEY_LEFT:
            case Keyboard.KEY_A:
                if (!dir.equals(1, 0)) {
                    dir = new Vector2(-1, 0);
                }
                break;
            case Keyboard.KEY_DOWN:
            case Keyboard.KEY_S:
                if (!dir.equals(0, -1)) {
                    dir = new Vector2(0, 1);
                }
                break;
            case Keyboard.KEY_RIGHT:
            case Keyboard.KEY_D:
                if (!dir.equals(-1, 0)) {
                    dir = new Vector2(1, 0);
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
        snake.clear();
        game = new GameState();
        if (gameRenderer != null) {
            gameRenderer.updateGameState(game);
        }

        if (!firstGame) {
            game.startGame();
            dir = new Vector2(1, 0);
        }
        snake.add(0, new SnakeBlock(true, new Vector2(10, 10), game.getPhase()));
        getDelta();
    }

    static void out(Object o) {
        if (DEBUG) System.out.println(o);
    }
}

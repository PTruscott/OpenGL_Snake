package client.graphics;
import client.GameState;
import client.blocks.Air;
import client.blocks.Block;
import client.blocks.Portal;
import client.blocks.SnakeBlock;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;

import static client.ClientSettings.*;
import static client.graphics.Draw.*;
import static org.lwjgl.Sys.getTime;

public class GameRenderer {

    private GameState game;
    private TextRenderer[] textRenderers;
    private LinkedList<SnakeBlock> snake;
    private int fps;
    private long lastFPS;

    /**
     * Sets up a new game renderer to show the game on screen
     * @param textRenderers how to display text
     */
    public GameRenderer(GameState game, TextRenderer[] textRenderers, LinkedList<SnakeBlock> snake) {
        this.textRenderers = textRenderers;
        this.game = game;
        this.snake = snake;
    }

    /**
     * The main render method
     */
    public void render() {
        colourBackground(game.getPhase());
        drawMap(game.getPhase());
        if (DISPLAY_SCORE) {
            drawScore();
        }
        if (DISPLAY_FPS) {
            drawFPS();
        }
    }

    public void updateGameState(GameState gs) {
        this.game = gs;
    }

    private void drawFPS() {
        drawText(textRenderers[0], ""+fps, SCREEN_WIDTH-BLOCK_SIZE*1.5f, 100, Colour.WHITE(), TextRenderer.Alignment.RIGHT);
        updateFPS();
    }
    private void drawScore() {
        int score = (game.getSnakeLength()-STARTING_LENGTH)/FOOD_REWARD;
        drawText(textRenderers[0], ""+score, SCREEN_WIDTH-BLOCK_SIZE*1.5f, BLOCK_SIZE*1.5f, Colour.WHITE(), TextRenderer.Alignment.RIGHT);
    }

    public void drawMenu(boolean drawScore) {

        float y = SCREEN_HEIGHT/4;
        float x = SCREEN_WIDTH/2;

        drawText(textRenderers[1], "WELCOME TO SNAKE", x, y, Colour.WHITE(), TextRenderer.Alignment.CENTRE);
        if (drawScore) {
            y += textRenderers[1].getCharHeight()*2;
            int score = (game.getSnakeLength()-STARTING_LENGTH)/FOOD_REWARD;
            drawText(textRenderers[0], "YOUR SCORE WAS "+score, x, y, Colour.WHITE(), TextRenderer.Alignment.CENTRE);
        }

        y = SCREEN_HEIGHT/5*3;
        float textHeight = textRenderers[0].getCharHeight();
        y -= textHeight*1.5;
        float textLength = textRenderers[0].getStringWidth("DISPLAY SCORE");
        //each rectangle would be length * 1.5
        //want a gap of .5
        //centre of left would be mid - length
        Colour colour;
        if (MENU_STATE == 0) {
            colour = PHASE_COLOURS[0].clone();
        }
        else {
            colour = Colour.WHITE();
        }
        if (DISPLAY_SCORE) {
            invertedRectGlow(x - textLength * 1.75f, y + 4, textLength * 1.5f, textHeight * 1.5f, 5, colour);
        }
        else {
            invertedRectGlow(x + textLength * .25f, y + 4, textLength * 1.5f, textHeight * 1.5f, 5, colour);
        }
        drawText(textRenderers[0], "DISPLAY SCORE", x - textLength, y, Colour.WHITE(), TextRenderer.Alignment.CENTRE);
        drawText(textRenderers[0], "NAY SCORE", x + textLength, y, Colour.WHITE(), TextRenderer.Alignment.CENTRE);

        y += textHeight*3;
        if (MENU_STATE == 1) {
            colour = PHASE_COLOURS[0].clone();
        }
        else {
            colour = Colour.WHITE();
        }
        if (RANDOM_MAP) {
            invertedRectGlow(x - textLength * 1.75f, y + 4, textLength * 1.5f, textHeight * 1.5f, 5, colour);
        }
        else {
            invertedRectGlow(x + textLength * .25f, y + 4, textLength * 1.5f, textHeight * 1.5f, 5, colour);
        }
        drawText(textRenderers[0], "RANDOM MAP", x - textLength, y, Colour.WHITE(), TextRenderer.Alignment.CENTRE);
        drawText(textRenderers[0], "BORING MAP", x + textLength, y, Colour.WHITE(), TextRenderer.Alignment.CENTRE);

        y += textHeight*4;
        drawText(textRenderers[0], "PRESS SPACE TO START", x, y, Colour.WHITE(), TextRenderer.Alignment.CENTRE);
    }

    private void drawMap(int phase) {
        Block[][] b = game.getBlocks(phase);
        for (int i = 0; i < game.getMapWidth(); i++) {
            for (int j = 0; j < game.getMapHeight(); j++) {
                if (!(b[i][j] instanceof Air)) {
                    if (b[i][j] instanceof Portal) {
                        drawPortal(i,j,(Portal) b[i][j], game.getPortalRotation());
                    }
                    else {
                        drawBlock(i, j, b[i][j]);
                    }
                }
            }
        }
        for (SnakeBlock s: snake) {
            if (s.getPhase() == game.getPhase()) {
                drawSnake(s);
            }
        }
    }

    /**
     * Draws the the stencil for the pulse, including the layer underneath
     */
    public void drawStencil() {
         int oldPhase = game.getRipple().getOldPhase();
         int newPhase = game.getRipple().getNewPhase();

         //draws the old phase
         colourBackground(oldPhase);
         drawMap(oldPhase);

         GL11.glEnable(GL11.GL_STENCIL_TEST);

         GL11.glColorMask(false, false, false, false);
         GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF); // Set any stencil to 1
         GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
         GL11.glStencilMask(0xFF); // Write to stencil buffer
         GL11.glDepthMask(false); // Don't write to depth buffer
         GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT); // Clear stencil buffer (0 by default)

         //sets up the layer to be over drawn
         PhaseRipple p = game.getRipple();
         float r = p.getRadius();
         drawRect(p.getStartX()-r, p.getStartY()-r, 0, r*2, r*2, new Colour(0, 0, 0));


         GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF); // Pass test if stencil value is 1
         GL11.glStencilMask(0x00); // Don't write anything to stencil buffer
         GL11.glDepthMask(true); // Write to depth buffer
         GL11.glColorMask(true, true, true, true);

         //GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

         GL11.glColor3f(0, 0, 0);
         drawRect(p.getStartX()-r, p.getStartY()-r, 0, r*2, r*2, new Colour(0, 0, 0));

         //draws the new phase in the circle
         colourBackground(newPhase);
         drawMap(newPhase);

         GL11.glDisable(GL11.GL_STENCIL_TEST);


         drawRectGlow(p.getStartX()-r, p.getStartY()-r, 0, r*2, r*2, PHASE_COLOURS[newPhase].clone(), 5);
    }

    private void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
}
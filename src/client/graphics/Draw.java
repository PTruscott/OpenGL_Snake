package client.graphics;

import client.ClientSettings;
import client.Vector2;
import client.blocks.Block;
import client.blocks.Food;
import client.blocks.Portal;
import client.blocks.SnakeBlock;

import static client.ClientSettings.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by peran on 3/5/17.
 *  used for draw methods
 */
class Draw {
    private int width;
    private int height;
    private TextRenderer smallText;
    private TextRenderer largeText;

    /**
     * Sets up a new draw object
     * @param textRenderers the text renders
     */
    Draw(TextRenderer[] textRenderers) {
        this.width = ClientSettings.SCREEN_WIDTH;
        this.height = ClientSettings.SCREEN_HEIGHT;
        smallText = textRenderers[0];
        largeText = textRenderers[1];
    }


    private static void drawQuad(float x, float y, float rotation, float width, float height, Colour colour) {
        setColour(colour);
        glPushMatrix();
        float cx = x;
        float cy = SCREEN_HEIGHT-y;
        glTranslatef(cx, cy, 0);
        glRotatef(rotation, 0f, 0f, 1f);
        glTranslatef(-cx, -cy, 0);
        glBegin(GL_QUADS);
        glVertex2f(cx, SCREEN_HEIGHT-y);
        glVertex2f(cx+width, SCREEN_HEIGHT-y);
        glVertex2f(cx+width, SCREEN_HEIGHT-(y+height));
        glVertex2f(cx, SCREEN_HEIGHT-(y+height));
        glEnd();

        glPopMatrix();
    }

    /**
     * Flashes the damage on the screen
     * @param intensity how bright the glow is
     * @param hurt whether red or green
     */
    private void flashDamage(float intensity, boolean hurt) {
        intensity = Math.min(1, intensity);
        float buffer = 60;
        float red = 0;
        float green = 1;
        if (hurt) {
            red = 1;
            green = 0;
        }

        invertedQuadGlow(0, height, width, height, buffer, red, green, 0, intensity);
    }

    /**
     * draws glow round circles
     * @param centre the centre of the glow
     * @param radius the radius from the centre
     * @param strokeWidth how think the line is
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     */
    void drawAura(Vector2 centre, float radius, float strokeWidth, float red, float green, float blue) {
        drawAura(centre, radius, strokeWidth, red, green, blue, 1);
    }

    /**
     * draws glow round circles
     * @param centre the centre of the glow
     * @param radius the radius from the centre
     * @param strokeWidth how think the line is
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     * @param intensity the brightness of the glow
     */
    void drawAura(Vector2 centre, float radius, float strokeWidth, float red, float green, float blue, float intensity) {
        glColor4f(red, green, blue, intensity);
        glBegin(GL_QUAD_STRIP);
        float cx = centre.getX();
        float cy = height-centre.getY();
        glVertex2f(cx, cy+(radius-strokeWidth));
        glColor4f(red, green, blue, intensity);
        glVertex2f(cx, cy+radius);
        for (int i = 1; i < 360; i++) {
            glColor4f(red, green, blue, 0);
            glVertex2d(cx+((radius-strokeWidth)*Math.sin(Math.toRadians(i))), cy+((radius-strokeWidth)*Math.cos(Math.toRadians(i))));
            glColor4f(red, green, blue, intensity);
            glVertex2d(cx+((radius)*Math.sin(Math.toRadians(i))), cy+((radius)*Math.cos(Math.toRadians(i))));
        }
        glColor4f(red, green, blue, 0);
        glVertex2f(cx, cy+(radius-strokeWidth));
        glColor4f(red, green, blue, intensity);
        glVertex2f(cx, cy+radius);
        glEnd();
    }

    void drawBlock(int x, int y, Block b, int phase) {
        Colour colour = b.getColour();
        if (b instanceof Food) {
            drawAura(new Vector2((x+.5f)*BLOCK_SIZE, (y+.5f)*BLOCK_SIZE), BLOCK_SIZE/2, BLOCK_SIZE/5, colour.red, colour.green, colour.blue);
        }
        else {
            if (b instanceof Portal) {
                Colour other = ((Portal) b).getOtherColour(phase);
                colour = ((Portal) b).getColour(phase);

                drawQuad(x*BLOCK_SIZE, y*BLOCK_SIZE, 0, BLOCK_SIZE, BLOCK_SIZE, other);
            }

            invertedQuadGlow(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE / 5, colour.red, colour.green, colour.blue, colour.intensity);
        }
    }

    void drawSnake(SnakeBlock s) {
        int x = (int)s.getPos().getX();
        int y = (int)s.getPos().getY();
        Colour colour = s.getColour();

        if (s.isHead()) {
            drawQuad(x*BLOCK_SIZE, y*BLOCK_SIZE, 0, BLOCK_SIZE, BLOCK_SIZE, colour);
        }
        invertedQuadGlow(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE / 5, colour.red, colour.green, colour.blue, colour.intensity);
    }

    /**
     * draws a glow inwards in a rectangle
     * @param xStart the left of the glow
     * @param yStart the top of the glow
     * @param rectWidth the width of the rectangle
     * @param rectHeight the height of the rectangle
     * @param strokeWidth the width of the line
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     * @param intensity the brightness of the glow
     */
    private void invertedQuadGlow(float xStart, float yStart, float rectWidth, float rectHeight, float strokeWidth, float red, float green, float blue, float intensity) {
        yStart = SCREEN_HEIGHT-yStart-rectHeight;

        glBegin(GL_QUAD_STRIP);
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart,yStart);
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart+strokeWidth, yStart+strokeWidth);
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart+rectWidth,yStart);
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart+rectWidth-strokeWidth, yStart+strokeWidth);
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart+rectWidth, yStart+rectHeight);
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart+rectWidth-strokeWidth, yStart+rectHeight-strokeWidth);
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart,yStart+rectHeight);
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart+strokeWidth, yStart+rectHeight-strokeWidth);
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart,yStart);
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart+strokeWidth, yStart+strokeWidth);
        glEnd();
    }

    /**
     * Draws the scoreboard on the screen in points order
     * Also highlights which player you are
     * And shows total team score
     * If the game has ended, show win or loss
     * @param gameEnded whether the game has ended
     */
    /*
    void drawScoreboard(boolean gameEnded) {
        shadeScreen();
        Scoreboard sb = new Scoreboard(1, 4);

        if (smallText == null) {
            smallText = new TextRenderer(20);
        }
        if (largeText == null) {
            largeText = new TextRenderer(25);
        }

        float intensity = 0.8f;
        glDisable(GL_TEXTURE_2D);

        int[] scores = sb.getPlayerScores();
        float xStart = 800/6;
        float yStart = 150;
        float rectWidth = xStart*2;
        float rectHeight = 50;

        float red = 0;
        float green = 1;
        float blue = 0;
        float buffer = 20;

        //draws the victory or defeat view
        if (gameEnded) {
            float endY = yStart-buffer-rectHeight;
            float team = 0;
            glColor4f(1-team, team, 1-team, intensity);
            drawRect(xStart, endY, rectWidth*2, rectHeight);
            invertedQuadGlow(xStart,endY+rectHeight, rectWidth*2, rectHeight,10,1,1,1,1);

            if (sb.getTeam0Score() > sb.getTeam1Score()) {
                if (team == 0) {
                    //enclave won (you)
                    drawText(largeText, "VICTORY", ClientSettings.SCREEN_WIDTH/2-50, endY+3*rectHeight/4+3);
                }
                else {
                    //enclave won (not you)
                    drawText(largeText,"DEFEAT",ClientSettings.SCREEN_WIDTH/2-40,endY+3*rectHeight/4+3);
                }
            }
            else {
                if (team == 0) {
                    //landscapers won (not you)
                    drawText(largeText, "DEFEAT", ClientSettings.SCREEN_WIDTH/2-40, endY+3*rectHeight/4+3);
                }
                else {
                    //landscapers won (you)
                    drawText(largeText,"VICTORY",ClientSettings.SCREEN_WIDTH/2-50,endY+3*rectHeight/4+3);
                }
            }
        }

        //sets up team headers
        glColor4f(red, green, blue, intensity);
        drawRect(xStart, yStart, rectWidth, rectHeight);

        drawText(largeText, "Landscapers", xStart+10, yStart+3*rectHeight/4+3);
        drawText(largeText, ((Integer) sb.getTeam1Score()).toString(), xStart+rectWidth-((Integer) sb.getTeam1Score()).toString().length()*18-10, yStart+3*rectHeight/4+3);

        glColor4f(1, 0, 1, intensity);
        drawRect(xStart+rectWidth, yStart, rectWidth, rectHeight);
        drawText(largeText, "The Enclave", xStart+rectWidth+10, yStart+3*rectHeight/4+2);
        rectWidth *= 2;
        drawText(largeText, ((Integer) sb.getTeam0Score()).toString(), xStart+rectWidth-((Integer) sb.getTeam0Score()).toString().length()*18-10, yStart+3*rectHeight/4+3);

        yStart += rectHeight+buffer;

        //sets up sorting the scores
        int[] sortedScores = scores.clone();
        int removed = 0;
        while (removed < sortedScores.length) {
            int max = Integer.MIN_VALUE;
            int index = -1;
            for (int i = 0; i < sortedScores.length; i++) {
                if (sortedScores[i] > max) {
                    index = i;
                    max = sortedScores[i];
                }
            }
            if (index != -1) {
                if (index%2 == 0) {
                    red = 1;
                    green = 0;
                    blue = 1;
                }
                else {
                    red = 0;
                    green = 1;
                    blue = 0;
                }
                //draws the rectangle in team colours
                glColor4f(red,green,blue, intensity);
                drawRect(xStart, yStart, rectWidth, rectHeight);

                //lists the names
                String name = "";
                if (index == playerID) name += " (You)";


                drawText(smallText, name, xStart+10, yStart+3*rectHeight/4+1);
                drawText(smallText, ((Integer) max).toString(), xStart+rectWidth-((Integer) max).toString().length()*15-10, yStart+3*rectHeight/4+1);

                yStart+=rectHeight;

                //highlights if it's you or not
                if (index == playerID) {
                    invertedQuadGlow(xStart,yStart,rectWidth,rectHeight,10,1,1,1,1);
                }

                sortedScores[index] = Integer.MIN_VALUE;
            }
            else {
                break;
            }
            removed++;
        }
    } */

    /**
     * draws text in given position
     * @param tx the text renderer
     * @param s the string to draw
     * @param x left of the string
     * @param y the top of the string
     */
     static void drawText(TextRenderer tx, String s, float x, float y) {
         drawText(tx, s, x, y, 0, 0, 0);
     }

    /**
     * draws text in given position
     * @param tx the text renderer
     * @param s the string to draw
     * @param x left of the string
     * @param y the top of the string
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     */
    static void drawText(TextRenderer tx, String s, float x, float y, float red, float green, float blue) {
        glEnable(GL_TEXTURE_2D);
        glColor4f(red,green,blue,1);
        tx.drawText(s, x, y);
        glDisable(GL_TEXTURE_2D);
    }

    /**
     * colours the background of a map depending on the phase
     * @param phase which phase you're in
     */
    void colourBackground(int phase) {
        Colour c = PHASE_COLOURS[phase].clone();
        c.intensity = 0.1f;
        setColour(c);
        drawRect(0,0,width,height);
    }

    /**
     * Draws a circle
     * @param cx centre of the circle
     * @param cy the centre of the circle (from the bottom)
     * @param r the radius of the circle
     * @param num_segments the number of segments, the more the smoother it looks
     */
    void drawCircle(float cx, float cy, float r, int num_segments) {
        float theta = (float) (2 * 3.1415926 / (num_segments));
        float tangetial_factor = (float) Math.tan(theta);//calculate the tangential factor
        float radial_factor = (float) Math.cos(theta);//calculate the radial factor

        float x = r;//we start at angle = 0
        float y = 0;

        glBegin(GL_TRIANGLE_FAN);

        for (int i = 0; i < num_segments; i++) {
            glVertex2f(x + cx, y + cy);//output vertex

            //calculate the tangential vector; remember, the radial vector is (x, y)
            //to get the tangential vector we flip those coordinates and negate one of them
            float tx = -y;
            float ty = x;

            //add the tangential vector
            x += tx * tangetial_factor;
            y += ty * tangetial_factor;

            //correct using the radial factor
            x *= radial_factor;
            y *= radial_factor;
        }
        glEnd();
    }

    /**
     * Draws a rectangle
     * @param xStart the start of the rect from the left
     * @param yStart the start of the rect from the top
     * @param rectWidth the width of the rectangle
     * @param rectHeight the height of the rectange
     */
    private static void drawRect(float xStart, float yStart, float rectWidth, float rectHeight) {
        glBegin(GL_QUADS);
        glVertex2f(checkX(xStart), checkY(ClientSettings.SCREEN_HEIGHT - yStart));
        glVertex2f(checkX(xStart + rectWidth), checkY(SCREEN_HEIGHT - yStart));
        glVertex2f(checkX(xStart + rectWidth), checkY(SCREEN_HEIGHT - (yStart+rectHeight)));
        glVertex2f(checkX(xStart), checkY(SCREEN_HEIGHT - (yStart+rectHeight)));
        glEnd();
    }

    private static void setColour(Colour c) {
        glColor4f(c.red,c.green,c.blue,c.intensity);
    }

    /**
     * Keep x coordinate on the screen
     *
     * @param x Initial x coordinate
     * @return Checked x coordinate
     */
    private static float checkX(float x) {
        if (x < 0) x = 0;
        if (x > SCREEN_WIDTH) x = SCREEN_WIDTH;
        return x;
    }

    /**
     * Keep y coordinate on the screen
     *
     * @param y Initial y coordinate
     * @return Checked y coordinate
     */
    private static float checkY(float y) {
        if (y < 0) y = 0;
        if (y > SCREEN_HEIGHT) y = SCREEN_HEIGHT;
        return y;
    }

}

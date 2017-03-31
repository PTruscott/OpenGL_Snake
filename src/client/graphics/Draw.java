package client.graphics;

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

    private static void drawRect(float x, float y, float rotation, float width, float height, Colour colour) {
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
     * draws glow round circles
     * @param centre the centre of the glow
     * @param radius the radius from the centre
     * @param strokeWidth how think the line is
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     */
    static void drawAura(Vector2 centre, float radius, float strokeWidth, float red, float green, float blue) {
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
    static void drawAura(Vector2 centre, float radius, float strokeWidth, float red, float green, float blue, float intensity) {
        glColor4f(red, green, blue, intensity);
        glBegin(GL_QUAD_STRIP);
        float cx = centre.getX();
        float cy = SCREEN_HEIGHT-centre.getY();
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

    static void drawBlock(int x, int y, Block b, int phase) {
        Colour colour = b.getColour();
        if (b instanceof Food) {
            drawAura(new Vector2((x+.5f)*BLOCK_SIZE, (y+.5f)*BLOCK_SIZE), BLOCK_SIZE/2, BLOCK_SIZE/5, colour.red, colour.green, colour.blue);
        }
        else {
            if (b instanceof Portal) {
                Colour other = ((Portal) b).getOtherColour(phase);
                colour = ((Portal) b).getColour(phase);

                drawRect(x*BLOCK_SIZE, y*BLOCK_SIZE, 0, BLOCK_SIZE, BLOCK_SIZE, other);
            }

            invertedQuadGlow(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE / 5, colour.red, colour.green, colour.blue, colour.intensity);
        }
    }

    static void drawSnake(SnakeBlock s) {
        int x = (int)s.getPos().getX();
        int y = (int)s.getPos().getY();
        Colour colour = s.getColour();

        if (s.isHead()) {
            drawRect(x*BLOCK_SIZE, y*BLOCK_SIZE, 0, BLOCK_SIZE, BLOCK_SIZE, colour);
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
    private static void invertedQuadGlow(float xStart, float yStart, float rectWidth, float rectHeight, float strokeWidth, float red, float green, float blue, float intensity) {
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
    static void colourBackground(int phase) {
        Colour c = PHASE_COLOURS[phase].clone();
        c.intensity = 0.1f;
        drawRect(0,0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, c);
    }

    /**
     * Draws a circle
     * @param cx centre of the circle
     * @param cy the centre of the circle (from the bottom)
     * @param r the radius of the circle
     * @param num_segments the number of segments, the more the smoother it looks
     */
    static void drawCircle(float cx, float cy, float r, int num_segments) {
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

    private static void setColour(Colour c) {
        glColor4f(c.red,c.green,c.blue,c.intensity);
    }

}

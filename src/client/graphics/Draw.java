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
public class Draw {

    public static void drawRect(float x, float y, float rotation, float width, float height, Colour colour) {
        setColour(colour);

        glPushMatrix();

            float cx = x+width/2;
            float cy = SCREEN_HEIGHT-(y+height/2);
            glTranslatef(cx, cy, 0);
            glRotatef(rotation, 0f, 0f, 1f);
            glTranslatef(-cx, -cy, 0);

                glBegin(GL_QUADS);
                glVertex2f(x, SCREEN_HEIGHT-y);
                glVertex2f(x+width, SCREEN_HEIGHT-y);
                glVertex2f(x+width, SCREEN_HEIGHT-(y+height));
                glVertex2f(x, SCREEN_HEIGHT-(y+height));

            glEnd();

        glPopMatrix();
    }

    static void drawRectGlow(float x, float y, float rotation, float width, float height, Colour colour, float strokeWidth) {
        Colour faded = colour.clone();
        faded.intensity = 0;

        glPushMatrix();

            float cx = x+width/2;
            float cy = SCREEN_HEIGHT-(y+height/2);
            glTranslatef(cx, cy, 0);
            glRotatef(rotation, 0f, 0f, 1f);
            glTranslatef(-cx, -cy, 0);

            glBegin(GL_QUAD_STRIP);

                setColour(faded);
                glVertex2f(x - strokeWidth, SCREEN_HEIGHT-(y - strokeWidth));  //outer bottom left
                setColour(colour);
                glVertex2f(x, SCREEN_HEIGHT-y); //inner bottom left
                setColour(faded);
                glVertex2f(x + width+strokeWidth, SCREEN_HEIGHT-(y - strokeWidth)); //outer bottom right
                setColour(colour);
                glVertex2f(x + width, SCREEN_HEIGHT-y); //inner bottom right
                setColour(faded);
                glVertex2f(x + width+strokeWidth, SCREEN_HEIGHT-(y + height + strokeWidth)); //outer top right
                setColour(colour);
                glVertex2f(x + width, SCREEN_HEIGHT-(y + height)); //inner top right
                setColour(faded);
                glVertex2f(x - strokeWidth, SCREEN_HEIGHT-(y + height + strokeWidth)); //outer top left
                setColour(colour);
                glVertex2f(x, SCREEN_HEIGHT-(y + height)); //inner top left
                setColour(faded);
                glVertex2f(x - strokeWidth, SCREEN_HEIGHT-(y - strokeWidth));  //outer bottom left
                setColour(colour);
                glVertex2f(x, SCREEN_HEIGHT-y); //inner bottom left

            glEnd();

        glPopMatrix();

    }

    /**
     * draws glow round circles
     * @param centre the centre of the glow
     * @param radius the radius from the centre
     * @param strokeWidth how think the line is
     * @param colour the colour of the aura
     */
    private static void drawAura(Vector2 centre, float radius, float strokeWidth, Colour colour) {
        setColour(colour);
        Colour faded = colour.clone();
        faded.intensity = 0;

        glBegin(GL_QUAD_STRIP);

            float cx = centre.getX();
            float cy = SCREEN_HEIGHT-centre.getY();
            glVertex2f(cx, cy+(radius-strokeWidth));
            setColour(faded);
            glVertex2f(cx, cy+radius);

            for (int i = 1; i < 360; i++) {
                setColour(faded);
                glVertex2d(cx+((radius-strokeWidth)*Math.sin(Math.toRadians(i))), cy+((radius-strokeWidth)*Math.cos(Math.toRadians(i))));
                setColour(colour);
                glVertex2d(cx+((radius)*Math.sin(Math.toRadians(i))), cy+((radius)*Math.cos(Math.toRadians(i))));
            }
            setColour(faded);
            glVertex2f(cx, cy+(radius-strokeWidth));
            setColour(colour);
            glVertex2f(cx, cy+radius);

        glEnd();
    }

    static void drawBlock(int x, int y, Block b) {
        Colour colour = b.getColour();
        if (b instanceof Food) {
            drawCircle((x+.5f)*BLOCK_SIZE, (y+.5f)*BLOCK_SIZE, BLOCK_SIZE/2, Colour.WHITE());
            drawAura(new Vector2((x+.5f)*BLOCK_SIZE, (y+.5f)*BLOCK_SIZE), BLOCK_SIZE/2, BLOCK_SIZE/2, colour);
        }
        else {
            invertedRectGlow(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE / 5, colour);
        }
    }



    static void drawPortal(int x, int y, Portal b, float rotation) {
        Colour other = b.getOtherColour();
        Colour colour = b.getColour();

        drawRect(x*BLOCK_SIZE+BLOCK_SIZE/4, y*BLOCK_SIZE+BLOCK_SIZE/4, rotation, BLOCK_SIZE/2, BLOCK_SIZE/2, other);
        drawRectGlow(x*BLOCK_SIZE+BLOCK_SIZE/4, y*BLOCK_SIZE+BLOCK_SIZE/4, rotation, BLOCK_SIZE/2, BLOCK_SIZE/2, other, BLOCK_SIZE/6);
        invertedRectGlow(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE / 5, colour);
    }

    static void drawSnake(SnakeBlock s) {
        int x = (int)s.getPos().getX();
        int y = (int)s.getPos().getY();
        Colour colour = s.getColour();

        if (s.isHead()) {
            drawCircle(x*BLOCK_SIZE+BLOCK_SIZE/2, y*BLOCK_SIZE+BLOCK_SIZE/2, BLOCK_SIZE/4, colour);
            drawAura(new Vector2(x*BLOCK_SIZE+BLOCK_SIZE/2, y*BLOCK_SIZE+BLOCK_SIZE/2), BLOCK_SIZE/4, BLOCK_SIZE/6, colour);
        }
        invertedRectGlow(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE / 5, colour);
    }

    /**
     * draws a glow inwards in a rectangle
     * @param xStart the left of the glow
     * @param yStart the top of the glow
     * @param rectWidth the width of the rectangle
     * @param rectHeight the height of the rectangle
     * @param strokeWidth the width of the line
     * @param colour the colour of the glow
     */
    static void invertedRectGlow(float xStart, float yStart, float rectWidth, float rectHeight, float strokeWidth, Colour colour) {
        yStart = SCREEN_HEIGHT-yStart-rectHeight;
        setColour(colour);
        Colour blank = colour.clone();
        blank.intensity = 0;
        glBegin(GL_QUAD_STRIP);

            setColour(colour);
            glVertex2f(xStart,yStart);
            setColour(blank);
            glVertex2f(xStart+strokeWidth, yStart+strokeWidth);
            setColour(colour);
            glVertex2f(xStart+rectWidth,yStart);
            setColour(blank);
            glVertex2f(xStart+rectWidth-strokeWidth, yStart+strokeWidth);
            setColour(colour);
            glVertex2f(xStart+rectWidth, yStart+rectHeight);
            setColour(blank);
            glVertex2f(xStart+rectWidth-strokeWidth, yStart+rectHeight-strokeWidth);
            setColour(colour);
            glVertex2f(xStart,yStart+rectHeight);
            setColour(blank);
            glVertex2f(xStart+strokeWidth, yStart+rectHeight-strokeWidth);
            setColour(colour);
            glVertex2f(xStart,yStart);
            setColour(blank);
            glVertex2f(xStart+strokeWidth, yStart+strokeWidth);

        glEnd();
    }

    /**
     * draws text in given position
     * @param tx the text renderer
     * @param s the string to draw
     * @param x left of the string
     * @param y the top of the string
     * @param colour the colour of the text
     * @param alignment the text align used
     */
    static void drawText(TextRenderer tx, String s, float x, float y, Colour colour, TextRenderer.Alignment alignment){
        glEnable(GL_TEXTURE_2D);
        setColour(colour);
        tx.drawText(s, x, y, alignment);
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
     */
    private static void drawCircle(float cx, float cy, float r, Colour colour) {
        float num_segments = r*2;
        setColour(colour);

        cy = SCREEN_HEIGHT-cy;

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

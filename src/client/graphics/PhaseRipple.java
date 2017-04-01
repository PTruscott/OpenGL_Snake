package client.graphics;

import static client.ClientSettings.RIPPLE_SPEED;
import static client.ClientSettings.SCREEN_HEIGHT;
import static client.ClientSettings.SCREEN_WIDTH;

/**
 * Created by peran on 3/31/17.
 * An animation to display the switching of phases
 */
public class PhaseRipple {
    private float startX;
    private float startY;
    private float speed;
    private float radius;
    private boolean alive;
    private int newPhase;
    private int oldPhase;


    public PhaseRipple(float startX, float startY, int oldPhase, int newPhase) {
        this.startX = startX;
        this.startY = startY;
        this.speed = RIPPLE_SPEED;
        this.newPhase = newPhase;
        this.oldPhase = oldPhase;
        radius = 10;
        alive = true;
    }

    float getStartX() {
        return startX;
    }

    float getStartY() {
        return startY;
    }

    public boolean isAlive() {
        return alive;
    }

    float getRadius() {
        return radius;
    }

    public void kill() {
        alive = false;
    }

    public void spread(float delta) {
        radius+=speed*delta;
        if (radius+startX > SCREEN_WIDTH && startX-radius < 0 && radius+startY > SCREEN_HEIGHT && startY-radius < 0) {
            kill();
        }
    }

    int getNewPhase() {
        return newPhase;
    }

    int getOldPhase() {
        return oldPhase;
    }
}

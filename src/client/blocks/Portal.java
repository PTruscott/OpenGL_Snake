package client.blocks;

import client.graphics.Colour;

import static client.ClientSettings.PHASE_COLOURS;

/**
 * Created by peran on 3/30/17.
 * Used to teleport people across the map
 */
public class Portal extends Block{
    private int otherPhase;
    private Colour otherColour;

    public Portal(int phase, int otherPhase) {
        collidable = false;
        colour = PHASE_COLOURS[phase].clone();
        otherColour = PHASE_COLOURS[otherPhase].clone();
        otherColour.intensity = 0.6f;
        this.otherPhase = otherPhase;
        this.phase = phase;
    }

    public int getOtherPhase() {
        return otherPhase;
    }

    public Colour getOtherColour() {
        return otherColour;
    }
}

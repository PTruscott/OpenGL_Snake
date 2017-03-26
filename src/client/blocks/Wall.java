package client.blocks;

import static client.ClientSettings.PHASE_COLOURS;

/**
 * Created by peran on 3/25/17.
 * A basic wall for snakes to crash into
 */
public class Wall extends Block {
    public Wall(int phase) {
        this.phase = phase;
        this.collidable = true;
        this.colour = PHASE_COLOURS[phase];
    }
}

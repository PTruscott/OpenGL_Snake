package client.blocks;

import static client.ClientSettings.FOOD_REWARD;
import static client.ClientSettings.PHASE_COLOURS;

/**
 * Created by peran on 3/25/17.
 * A basic wall for snakes to crash into
 */
public class Food extends Block {
    private int growth;

    public Food(int phase) {
        this.phase = phase;
        this.collidable = false;
        this.colour = PHASE_COLOURS[phase].clone();
        growth = FOOD_REWARD;
    }

    public int getGrowth() {
        return growth;
    }
}

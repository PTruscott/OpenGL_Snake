package client.graphics;

/**
 * Created by peran on 3/25/17.
 * Used to pass float colours around
 */
public class Colour {

    float red;
    float green;
    float blue;
    public float intensity;

    public Colour(float red, float blue, float green) {
        this(red, blue, green, 1);
    }

    public Colour(float red, float blue, float green, float intensity) {
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.intensity = intensity;
    }

    private Colour(Colour c) {
        this.red = c.red;
        this.blue = c.blue;
        this.green = c.green;
        this.intensity = c.intensity;
    }

    static Colour BLACK() {
        return new Colour(0, 0, 0, 1);
    }

    static Colour WHITE() {
        return new Colour(1, 1, 1, 1);
    }

    public Colour clone() {
        return new Colour(this);
    }
}

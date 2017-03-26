package client.graphics;

/**
 * Created by peran on 3/25/17.
 * Used to pass float colours around
 */
public class Colour {
    public float red;
    public float green;
    public float blue;
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
}

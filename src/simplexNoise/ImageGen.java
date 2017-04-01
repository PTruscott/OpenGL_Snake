package simplexNoise;

import java.util.Random;

import static client.ClientSettings.BLOCK_SIZE;
import static client.ClientSettings.SCREEN_HEIGHT;
import static client.ClientSettings.SCREEN_WIDTH;

public class ImageGen {
    public static void main(String args[]){
        Random rand = new Random();

        SimplexNoise simplexNoise=new SimplexNoise(100,0.1, rand.nextInt(10000));

        double xStart=0;
        double XEnd=500;
        double yStart=0;
        double yEnd=500;

        int xResolution=SCREEN_WIDTH/BLOCK_SIZE;
        int yResolution=SCREEN_HEIGHT/BLOCK_SIZE;

        double[][] result=new double[xResolution][yResolution];

        for(int i=0;i<xResolution;i++){
            for(int j=0;j<yResolution;j++){
                int x=(int)(xStart+i*((XEnd-xStart)/xResolution));
                int y=(int)(yStart+j*((yEnd-yStart)/yResolution));
                result[i][j]=0.5*(1+simplexNoise.getNoise(x,y));
            }
        }

        ImageWriter.greyWriteImage(result);
    }
}

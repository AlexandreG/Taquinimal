package fr.imac.taquinimal.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.Random;

import fr.imac.taquinimal.model.Animal;

/**
 * Created by AG on 23/06/2015.
 */
public class Utils {
    /**
     * Resize the given bitmap
     * @param bm
     * @param newHeight
     * @param newWidth
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;

    }

    public static Animal.AnimalType getRandomAnimalType(Random r){
        return Animal.AnimalType.getAnimalFromCode(r.nextInt(Animal.AnimalType.values().length-1)+1);
    }
}

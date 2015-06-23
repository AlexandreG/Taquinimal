package fr.imac.taquinimal.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import fr.imac.taquinimal.App;

/**
 * Created by AG on 23/06/2015.
 */
public class Animal {
    private AnimalType type;
    private AnimalState state;
    private Bitmap bp;

    private int mapX;
    private int mapY;
    private float x;
    private float y;
    private float speedX;
    private float speedY;

    public Animal(AnimalType type, Bitmap bp, int mapX, int mapY, float x, float y) {
        this.type = type;
        this.bp = bp;
        this.x = x;
        this.y = y;

        speedX = 0;
        speedY = 0;
    }

    /**
     * Draw itself
     * @param canvas
     * @param paint
     */
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bp, x - bp.getWidth() / 2, y - bp.getHeight() / 2, paint);
    }

    /**
     * Move itself
     */
    public void move(){
        //if(state == AnimalState.MOVING){
            x +=speedX;
            y +=speedY;
        //}
    }

    public void setSpeed(float speedX, float speedY){
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public enum AnimalType {
        CROCO,
        BEAR,
        CAT,
        OWL,
        SNAKE,
        MOUSE,
        ELEPHANT,
        FROG;
    }

    public enum AnimalState{
        IDLE,
        MOVING;
    }


}

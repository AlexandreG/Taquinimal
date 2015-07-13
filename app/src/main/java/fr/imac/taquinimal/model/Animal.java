package fr.imac.taquinimal.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import fr.imac.taquinimal.controller.GameActivity;
import fr.imac.taquinimal.utils.GameHelper;
import fr.imac.taquinimal.utils.Values;

/**
 * Created by AG on 23/06/2015.
 */
public class Animal {
    public final static float WALL_COLLIDE_PAD = Values.ANIMAL_SPEED / 2;

    private AnimalListener listener;

    private AnimalType type;
    private AnimalState state;
    private Bitmap bp;

    private int mapX;
    private int mapY;
    private float x;
    private float y;
    private float speedX;
    private float speedY;
    private float targetX; //the x pos of the destination of the movment
    private float targetY;

    public Animal(AnimalListener l, AnimalType type, Bitmap bp, int mapX, int mapY, float x, float y) {
        this.listener = l;
        this.type = type;
        this.bp = bp;
        this.x = x;
        this.y = y;
        this.mapX = mapX;
        this.mapY = mapY;
        this.speedX = 0;
        this.speedY = 0;

        //Log.d("a", "new animal : "+mapX+" "+mapY+" "+x+" "+y);
    }

    /**
     * Draw itself
     *
     * @param canvas
     * @param paint
     */
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bp, x - bp.getWidth() / 2, y - bp.getHeight() / 2, paint);
    }

    /**
     * Move itself
     */
    public void move(GameActivity.Swipe lastEvent) {
        if (lastEvent != null) {
            //collide
            switch (lastEvent) {
                case DOWN:
                    if (targetY < y) {
                        y = targetY;
                        x = targetX;
                        mapY = GameHelper.getInstance().getYMapFromPos(y);
                        listener.reachedNewPos(this);
                        state = AnimalState.IDLE;
                        listener.stopedMoving();
                        Log.w("a", "collide down " + mapX + " " + mapY);
                    }
                    break;
                case UP:
                    if (targetY > y) {
                        y = targetY;
                        x = targetX;
                        mapY = GameHelper.getInstance().getYMapFromPos(y);

                        listener.reachedNewPos(this);
                        state = AnimalState.IDLE;
                        listener.stopedMoving();
                        Log.w("a", "collide up " + mapX + " " + mapY);
                    }

                    break;
                case LEFT:
                    if (targetX > x) {
                        y = targetY;
                        x = targetX;
                        mapX = GameHelper.getInstance().getXMapFromPos(x);

                        listener.reachedNewPos(this);
                        state = AnimalState.IDLE;
                        listener.stopedMoving();
                        Log.w("a", "collide left " + mapX + " " + mapY);
                    }

                    break;
                case RIGHT:
                    if (targetX < x) {
                        y = targetY;
                        x = targetX;
                        mapX = GameHelper.getInstance().getXMapFromPos(x);

                        listener.reachedNewPos(this);
                        state = AnimalState.IDLE;
                        listener.stopedMoving();
                        Log.w("a", "collide right " + mapX + " " + mapY);
                    }

                    break;
            }
        }


        if (state == AnimalState.MOVING) {
            x += speedX;
            y += speedY;
        }
    }


    public void moveRight() {
        //no wall on the right
        if (mapX != Values.BOARD_SIZE - 1) {
            //if no idle animal
//            if(engine.getBoard().isMapEmpty(mapX+1, mapY)){
//
//            }

            targetX = GameHelper.getInstance().getXPosFromMap(Values.BOARD_SIZE - 1);
            targetY = y;
            listener.leavingPos(mapX, mapY);

            setSpeed(Values.ANIMAL_SPEED, 0);
            state = AnimalState.MOVING;
            listener.startMoving();
        }
    }

    public void moveLeft() {
        //no wall on the left
        if (mapX != 0) {

            targetX = GameHelper.getInstance().getXPosFromMap(0);
            targetY = y;
            listener.leavingPos(mapX, mapY);

            setSpeed(-Values.ANIMAL_SPEED, 0);
            state = AnimalState.MOVING;
            listener.startMoving();
        }
    }

    public void moveUp() {
        //no wall up
        if (mapY != 0) {

            targetY = GameHelper.getInstance().getXPosFromMap(0);
            targetX = x;
            listener.leavingPos(mapX, mapY);

            setSpeed(0, -Values.ANIMAL_SPEED);
            state = AnimalState.MOVING;
            listener.startMoving();
        }
    }

    public void moveDown() {
        //no wall down
        if (mapY != Values.BOARD_SIZE - 1) {

            targetY = GameHelper.getInstance().getYPosFromMap(Values.BOARD_SIZE - 1);
            targetX = x;
            listener.leavingPos(mapX, mapY);

            setSpeed(0, Values.ANIMAL_SPEED);
            state = AnimalState.MOVING;
            listener.startMoving();
        }
    }

    public void setSpeed(float speedX, float speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public int getMapX() {
        return mapX;
    }

    public int getMapY() {
        return mapY;
    }

    public void setState(AnimalState state) {
        this.state = state;
    }

    public enum AnimalType {
        CROCO,
        BEAR,
        CAT,
        OWL,
        SNAKE,
        MOUSE,
        ELEPHANT,
        FROG
    }

    public enum AnimalState {
        IDLE,
        MOVING
    }

    public interface AnimalListener {
        public void reachedNewPos(Animal a);

        public void leavingPos(int mapX, int mapY);

        public void stopedMoving();

        public void startMoving();
    }

}

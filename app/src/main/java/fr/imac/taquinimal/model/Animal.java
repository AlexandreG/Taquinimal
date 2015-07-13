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
        this.state = AnimalState.IDLE;
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
        if(bp != null){
            canvas.drawBitmap(bp, x - bp.getWidth() / 2, y - bp.getHeight() / 2, paint);
        }
    }

    /**
     * Move itself
     */
    public void move(GameActivity.Swipe lastEvent) {
        if (state == AnimalState.MOVING) {
            if (!isThereCollision(lastEvent)) {
                x += speedX;
                y += speedY;
            }
        }

    }

    /**
     * Test if the animal has reach its target
     *
     * @param lastEvent the last kind of event
     * @return true if collision, false else
     */
    private boolean isThereCollision(GameActivity.Swipe lastEvent) {
        if (lastEvent != null) {
            switch (lastEvent) {
                case DOWN:
                    if (targetY < y) {
                        y = targetY;
                        x = targetX;
                        state = AnimalState.IDLE;
                        listener.stoppedMoving();
                        Log.w("a", "collide down " + mapX + " " + mapY);
                        return true;
                    }
                    break;
                case UP:
                    if (targetY > y) {
                        y = targetY;
                        x = targetX;
                        state = AnimalState.IDLE;
                        listener.stoppedMoving();
                        Log.w("a", "collide up " + mapX + " " + mapY);
                        return true;
                    }

                    break;
                case LEFT:
                    if (targetX > x) {
                        y = targetY;
                        x = targetX;
                        state = AnimalState.IDLE;
                        listener.stoppedMoving();
                        Log.w("a", "collide left " + mapX + " " + mapY);
                        return true;
                    }

                    break;
                case RIGHT:
                    if (targetX < x) {
                        y = targetY;
                        x = targetX;
                        state = AnimalState.IDLE;
                        listener.stoppedMoving();
                        Log.w("a", "collide right " + mapX + " " + mapY);
                        return true;
                    }

                    break;
            }
        }
        return false;
    }

    public void moveRight(Board b) {
        for (int i = Values.BOARD_SIZE - 1; i > mapX; --i) {
            if (b.getBox(i, mapY) == -1) {
                //empty box => lets go!
                targetY = y;
                targetX = GameHelper.getInstance().getYPosFromMap(i);

                listener.leavingPos(mapX, mapY);
                mapX = GameHelper.getInstance().getYMapFromPos(targetX);
                listener.targetNewPos(mapX, mapY, this);

                setSpeed(Values.ANIMAL_SPEED, 0);
                state = AnimalState.MOVING;
                listener.startMoving();

                return;
            }
        }
    }

    public void moveLeft(Board b) {
        for (int i = 0; i < mapX; ++i) {
            if (b.getBox(i, mapY) == -1) {
                //empty box => lets go!
                targetY = y;
                targetX = GameHelper.getInstance().getYPosFromMap(i);

                listener.leavingPos(mapX, mapY);
                mapX = GameHelper.getInstance().getYMapFromPos(targetX);
                listener.targetNewPos(mapX, mapY, this);

                setSpeed(-Values.ANIMAL_SPEED, 0);
                state = AnimalState.MOVING;
                listener.startMoving();

                return;
            }
        }
    }

    public void moveUp(Board b) {
        for (int j = 0; j < mapY; ++j) {
            if (b.getBox(mapX, j) == -1) {
                //empty box => lets go!
                targetY = GameHelper.getInstance().getYPosFromMap(j);
                targetX = x;

                listener.leavingPos(mapX, mapY);
                mapY = GameHelper.getInstance().getYMapFromPos(targetY);
                listener.targetNewPos(mapX, mapY, this);

                setSpeed(0, -Values.ANIMAL_SPEED);
                state = AnimalState.MOVING;
                listener.startMoving();

                return;
            }
        }
    }

    public void moveDown(Board b) {
        for (int j = Values.BOARD_SIZE - 1; j > mapY; --j) {
            if (b.getBox(mapX, j) == -1) {
                //empty box => lets go!
                targetY = GameHelper.getInstance().getYPosFromMap(j);
                targetX = x;

                listener.leavingPos(mapX, mapY);
                mapY = GameHelper.getInstance().getYMapFromPos(targetY);
                listener.targetNewPos(mapX, mapY, this);

                setSpeed(0, Values.ANIMAL_SPEED);
                state = AnimalState.MOVING;
                listener.startMoving();

                return;
            }
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
        CROCO(1),
        BEAR(2),
        CAT(3),
        OWL(4),
        SNAKE(5),
        MOUSE(6),
        ELEPHANT(7),
        FROG(8);

        private int code;

        AnimalType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
        public static AnimalType getAnimalFromCode(int code){
            for(AnimalType e : AnimalType.values()){
                if(code == e.code) return e;
            }
            return null;
        }
    }

    public enum AnimalState {
        IDLE,
        MOVING
    }

    public interface AnimalListener {
        public void targetNewPos(int mapX, int mapY, Animal a);

        public void leavingPos(int mapX, int mapY);

        public void stoppedMoving();

        public void startMoving();
    }

}

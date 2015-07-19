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
        if (bp != null) {
            canvas.drawBitmap(bp, x - bp.getWidth() / 2, y - bp.getHeight() / 2, paint);
        }
    }

    /**
     * Move itself
     */
    public void move(GameActivity.Swipe lastEvent) {
        if (state == AnimalState.MOVING) {
            x += speedX;
            y += speedY;
            if (isThereCollision(lastEvent)) {
                stopMoving();
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
                        return true;
                    }
                    break;
                case UP:
                    if (targetY > y) {
                        return true;
                    }
                    break;
                case LEFT:
                    if (targetX > x) {
                        return true;
                    }
                    break;
                case RIGHT:
                    if (targetX < x) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * A collision occurred, we stop moving
     */
    private void stopMoving() {
        y = targetY;
        x = targetX;
        state = AnimalState.IDLE;
        listener.stoppedMoving();
        //Log.w("a", "collide right " + mapX + " " + mapY);
    }

    /**
     * An event moveRight occurred and is sent to the animal
     * Check if no collision, and the launch the movement
     *
     * @param b the board of the game
     */
    public void moveRight(Board b) {
        Animal a;
        for (int i = Values.BOARD_SIZE - 1; i > mapX; --i) {
            a = b.getBox(i, mapY);
            if (a == null) {
                //empty box => lets go
                proceedMove(GameHelper.getInstance().getXPosFromMap(i), y, Values.ANIMAL_SPEED, 0);
                return;
            } else {
                //if there is an eatable animal
                if (this.type.canIEatThis(a.type)) {
                    //if there is nothing between us and the animal to eat
                    if (i == mapX + 1 || b.getBox(i - 1, mapY) == null) {
                        proceedMove(GameHelper.getInstance().getXPosFromMap(i), y, Values.ANIMAL_SPEED, 0);
                        listener.willEatAnimal(a);
                        return;
                    }
                }
            }
        }
    }

    /**
     * An event moveLeft occurred and is sent to the animal
     * Check if no collision, and the launch the movement
     *
     * @param b the board of the game
     */
    public void moveLeft(Board b) {
        Animal a;
        for (int i = 0; i < mapX; ++i) {
            a = b.getBox(i, mapY);
            if (a == null) {
                //empty box => lets go
                proceedMove(GameHelper.getInstance().getYPosFromMap(i), y, -Values.ANIMAL_SPEED, 0);
                return;
            } else {
                //if there is an eatable animal
                if (this.type.canIEatThis(a.type)) {
                    //if there is nothing between us and the animal to eat
                    if (i == mapX - 1 || b.getBox(i + 1, mapY) == null) {
                        proceedMove(GameHelper.getInstance().getYPosFromMap(i), y, -Values.ANIMAL_SPEED, 0);
                        listener.willEatAnimal(a);
                        return;
                    }
                }
            }
        }
    }

    /**
     * An event moveUp occurred and is sent to the animal
     * Check if no collision, and the launch the movement
     *
     * @param b the board of the game
     */
    public void moveUp(Board b) {
        Animal a;
        for (int j = 0; j < mapY; ++j) {
            a = b.getBox(mapX, j);
            if (a == null) {
                //empty box => lets go
                proceedMove(x, GameHelper.getInstance().getYPosFromMap(j), 0, -Values.ANIMAL_SPEED);
                return;
            } else {
                //if there is an eatable animal
                if (this.type.canIEatThis(a.type)) {
                    //if there is nothing between us and the animal to eat
                    if (j == mapY - 1 || b.getBox(mapX, j + 1) == null) {
                        proceedMove(x, GameHelper.getInstance().getYPosFromMap(j), 0, -Values.ANIMAL_SPEED);
                        listener.willEatAnimal(a);
                        return;
                    }
                }
            }
        }
    }

    /**
     * An event moveDown occurred and is sent to the animal
     * Check if no collision, and the launch the movement
     *
     * @param b the board of the game
     */
    public void moveDown(Board b) {
        Animal a;
        for (int j = Values.BOARD_SIZE - 1; j > mapY; --j) {
            a = b.getBox(mapX, j);
            if (a == null) {
                //empty box => lets go
                proceedMove(x, GameHelper.getInstance().getYPosFromMap(j), 0, Values.ANIMAL_SPEED);
                return;
            } else {
                //if there is an eatable animal
                if (this.type.canIEatThis(a.type)) {
                    //if there is nothing between us and the animal to eat
                    if (j == mapY + 1 || b.getBox(mapX, j - 1) == null) {
                        proceedMove(x, GameHelper.getInstance().getYPosFromMap(j), 0, Values.ANIMAL_SPEED);
                        listener.willEatAnimal(a);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Launch the movement of the animal with the given
     *
     * @param tX the x pos of the target to reach
     * @param tY the y pos of the target to reach
     * @param sX the x speed to apply
     * @param sY the y speed to apply
     */
    private void proceedMove(float tX, float tY, float sX, float sY) {
        targetX = tX;
        targetY = tY;

        listener.leavingPos(mapX, mapY);
        mapX = GameHelper.getInstance().getXMapFromPos(targetX);
        mapY = GameHelper.getInstance().getYMapFromPos(targetY);
        listener.targetNewPos(mapX, mapY, this);

        setSpeed(sX, sY);
        state = AnimalState.MOVING;
        listener.startMoving();
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

    public enum AnimalState {
        IDLE,
        MOVING
    }

    public interface AnimalListener {
        public void targetNewPos(int mapX, int mapY, Animal a);

        public void leavingPos(int mapX, int mapY);

        public void stoppedMoving();

        public void startMoving();

        public void willEatAnimal(Animal a);
    }

}

package fr.imac.taquinimal.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import fr.imac.taquinimal.controller.GameEngine;
import fr.imac.taquinimal.utils.Values;

/**
 * Created by AG on 23/06/2015.
 */
public class Animal {
    private GameEngine engine;

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

    public Animal(GameEngine e, AnimalType type, Bitmap bp, int mapX, int mapY, float x, float y) {
        this.engine = e;
        this.type = type;
        this.bp = bp;
        this.x = x;
        this.y = y;
        this.mapX = mapX;
        this.mapY = mapY;
        this.speedX = 0;
        this.speedY = 0;
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
        if(engine.getLastEvent() != null){
            //collide
            switch (engine.getLastEvent()){
                case DOWN:
                    if(y > targetY+engine.getBoard().getBoxWidth()/2){
                        y = targetY;
                        x = targetX;
                        mapY = engine.getYMapFromPos(y);
                        engine.savePosOnBoard(this);
                        state = AnimalState.IDLE;
                        engine.setNbAnimalMovingMinus1();
                    }
                    break;
                case UP:
                    if(y < targetY+engine.getBoard().getBoxWidth()/2){
                        y = targetY;
                        x = targetX;
                        mapY = engine.getYMapFromPos(y);

                        engine.savePosOnBoard(this);
                        state = AnimalState.IDLE;
                        engine.setNbAnimalMovingMinus1();
                    }

                    break;
                case LEFT:
                    if(x < targetX-engine.getBoard().getBoxWidth()/2){
                        y = targetY;
                        x = targetX;
                        mapX = engine.getXMapFromPos(x);

                        engine.savePosOnBoard(this);
                        state = AnimalState.IDLE;
                        engine.setNbAnimalMovingMinus1();
                    }

                    break;
                case RIGHT:
                    if(x > targetX+engine.getBoard().getBoxWidth()/2){
                        y = targetY;
                        x = targetX;
                        mapX = engine.getXMapFromPos(x);

                        engine.savePosOnBoard(this);
                        state = AnimalState.IDLE;
                        engine.setNbAnimalMovingMinus1();
                    }

                    break;
            }
        }


        if(state == AnimalState.MOVING){
            x +=speedX;
            y +=speedY;
        }
    }


    public void moveRight(){
        //no wall on the right
        if(mapX != Values.BOARD_SIZE-1){
            //if no idle animal
//            if(engine.getBoard().isMapEmpty(mapX+1, mapY)){
//
//            }

            targetX = engine.getXPosFromMap(Values.BOARD_SIZE-1);
            targetY = y;
            engine.getBoard().setBox(mapX, mapY, -1);

            setSpeed(Values.ANIMAL_SPEED, 0);
            state = AnimalState.MOVING;
            engine.setNbAnimalMovingPlus1();
        }
    }

    public void moveLeft(){
        //no wall on the left
        if(mapY != 0){

            targetX = engine.getXPosFromMap(0);
            targetY = y;
            engine.getBoard().setBox(mapX, mapY, -1);

            setSpeed(-Values.ANIMAL_SPEED, 0);
            state = AnimalState.MOVING;
            engine.setNbAnimalMovingPlus1();
        }
    }

    public void moveUp(){
        //no wall up
        if(mapY != 0){

            targetY = engine.getXPosFromMap(0);
            targetX = x;
            engine.getBoard().setBox(mapX, mapY, -1);

            setSpeed(0, -Values.ANIMAL_SPEED);
            state = AnimalState.MOVING;
            engine.setNbAnimalMovingPlus1();
        }
    }

    public void moveDown(){
        //no wall down
        if(mapY != Values.BOARD_SIZE-1){

            targetY = engine.getXPosFromMap(Values.BOARD_SIZE-1);
            targetX = x;
            engine.getBoard().setBox(mapX, mapY, -1);

            setSpeed(0, Values.ANIMAL_SPEED);
            state = AnimalState.MOVING;
            engine.setNbAnimalMovingPlus1();
        }
    }

    public void setSpeed(float speedX, float speedY){
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
        FROG;
    }

    public enum AnimalState{
        IDLE,
        MOVING;
    }


}

package fr.imac.taquinimal.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import fr.imac.taquinimal.App;
import fr.imac.taquinimal.R;
import fr.imac.taquinimal.model.Animal;
import fr.imac.taquinimal.model.Board;
import fr.imac.taquinimal.utils.Utils;
import fr.imac.taquinimal.utils.Values;

/**
 * The engine update the view and the models
 * <p/>
 * Created by AG on 23/06/2015.
 */
public class GameEngine {
    private LinkedList<Animal> animalList;
    private HashMap<Animal.AnimalType, Bitmap> animalImageList;

    private Board board;

    private int nbAnimalMoving;
    private GameActivity.Swipe lastEvent;

    public void initGame() {
        animalList = new LinkedList<Animal>();
        animalImageList = new HashMap<Animal.AnimalType, Bitmap>();
        board = new Board();

        //init the bitmap with the righ size
        loadAnimalBitmaps();

        //add few animals
        animalList.add(new Animal(this, Animal.AnimalType.BEAR, animalImageList.get(Animal.AnimalType.BEAR), 0, 0, getXPosFromMap(0), getYPosFromMap(0)));
        board.setBox(animalList.get(0).getMapX(), animalList.get(0).getMapY(), 0);
        animalList.add(new Animal(this, Animal.AnimalType.CAT, animalImageList.get(Animal.AnimalType.CAT), 3, 4, getXPosFromMap(3), getYPosFromMap(4)));
        board.setBox(animalList.get(1).getMapX(), animalList.get(1).getMapY(), 1);
        animalList.add(new Animal(this, Animal.AnimalType.ELEPHANT, animalImageList.get(Animal.AnimalType.ELEPHANT), 3, 2, getXPosFromMap(3), getYPosFromMap(2)));
        board.setBox(animalList.get(2).getMapX(), animalList.get(2).getMapY(), 2);
    }

    /**
     * Load bitmaps from memory to java objects
     */
    private void loadAnimalBitmaps(){
        //load the bitmaps
        LinkedList<Bitmap> temp = new LinkedList<Bitmap>();
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.bear));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.cat));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.croco));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.elephant));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.frog));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.mouse));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.owl));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.snake));

        //resize the bitmaps
        animalImageList.put(Animal.AnimalType.BEAR, Utils.getResizedBitmap(temp.get(0), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(Animal.AnimalType.CAT, Utils.getResizedBitmap(temp.get(1), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(Animal.AnimalType.CROCO, Utils.getResizedBitmap(temp.get(2), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(Animal.AnimalType.ELEPHANT, Utils.getResizedBitmap(temp.get(3), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(Animal.AnimalType.FROG, Utils.getResizedBitmap(temp.get(4), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(Animal.AnimalType.MOUSE, Utils.getResizedBitmap(temp.get(5), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(Animal.AnimalType.OWL, Utils.getResizedBitmap(temp.get(6), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(Animal.AnimalType.SNAKE, Utils.getResizedBitmap(temp.get(7), (int) board.getBoxWidth(), (int) board.getBoxWidth()));


        //release memory
        for (Bitmap b : temp){
            b.recycle();
        }
    }

    /**
     * Update the game
     */
    public void update() {
        moveAll();
        drawAll();
    }

    /**
     * Move all animals and board
     */
    private void moveAll() {
        if (animalList !=null){
            for (Animal a : animalList){
                a.move();
            }
        }
    }

    /**
     * Draw all animals and board
     */
    private void drawAll() {
        App.getInstance().getGameActivity().updateView();
    }

    public LinkedList<Animal> getAnimalList() {
        return animalList;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Convert the position from the map to the coordinate in the screen
     * @param xMap
     * @return
     */
    public float getXPosFromMap(int xMap){
        return 0.5f*board.getBoxWidth()+xMap*board.getBoxWidth();
    }

    /**
     * Convert the position from the map to the coordinate in the screen
     * @param yMap
     * @return
     */
    public float getYPosFromMap(int yMap){
        return 0.5f*board.getBoxWidth()+yMap*board.getBoxWidth();
        //return App.getInstance().getScreenH()/2-0.5f*board.getWidth()+0.5f*board.getBoxWidth()+yMap*board.getBoxWidth();
    }

    /**
     * Convert the coordinate in the screen to the position in the map
     */
    public int getXMapFromPos(float x){
        for(int i=0 ; i<Values.BOARD_SIZE ; ++i){
            if(i*board.getWidth()< x && x < (i+1)*board.getWidth()){
                return i;
            }
        }
        return -1;
    }

    /**
     * Convert the coordinate in the screen to the position in the map
     */
    public int getYMapFromPos(float y){
        for(int i=0 ; i<Values.BOARD_SIZE ; ++i){
            if(i*board.getWidth()< y && y < (i+1)*board.getWidth()){
                return i;
            }
        }
        return -1;
    }

    public void onSwipe(GameActivity.Swipe s){
        lastEvent = s;
        if(nbAnimalMoving == 0){
            switch (s){
                case UP:
                    onSwipeUp();
                    break;
                case DOWN:
                    onSwipeDown();
                    break;
                case LEFT:
                    onSwipeLeft();
                    break;
                case RIGHT:
                    onSwipeRight();
                    break;
            }
        }
    }

    public void onSwipeUp(){
        for (Animal a : animalList){
            a.moveUp();
        }
    }

    public void onSwipeDown(){
        for (Animal a : animalList){
            a.moveDown();
        }
    }

    public void onSwipeLeft(){
        for (Animal a : animalList){
            a.moveLeft();
        }
    }

    public void onSwipeRight(){
        for (Animal a : animalList){
            a.moveRight();
        }
    }

    public GameActivity.Swipe getLastEvent() {
        return lastEvent;
    }

    public void savePosOnBoard(Animal a){
        board.setBox(a.getMapX(), a.getMapY(), animalList.indexOf(a));
    }

    public void setNbAnimalMoving(int nbAnimalMoving) {
        this.nbAnimalMoving = nbAnimalMoving;
    }

    public void setNbAnimalMovingPlus1() {
        ++nbAnimalMoving;
    }
    public void setNbAnimalMovingMinus1() {
        --nbAnimalMoving;
    }

    public int getNbAnimalMoving() {
        return nbAnimalMoving;
    }
}

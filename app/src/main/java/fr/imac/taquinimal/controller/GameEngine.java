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

    public GameEngine() {
        animalList = new LinkedList<Animal>();
        animalImageList = new HashMap<Animal.AnimalType, Bitmap>();
        board = new Board();

        initGame();
    }

    public void initGame() {
        //init the bitmap with the righ size
        loadAnimalBitmaps();

        //add few animals
        animalList.add(new Animal(Animal.AnimalType.BEAR, animalImageList.get(Animal.AnimalType.BEAR), 0, 0, getXPosFromMap(0), getYPosFromMap(0)));
        animalList.add(new Animal(Animal.AnimalType.CAT, animalImageList.get(Animal.AnimalType.CAT), 3, 4, getXPosFromMap(3), getYPosFromMap(4)));
        animalList.add(new Animal(Animal.AnimalType.ELEPHANT, animalImageList.get(Animal.AnimalType.ELEPHANT), 3, 2, getXPosFromMap(3), getYPosFromMap(2)));
    }

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
        for (Animal a : animalList){
            a.move();
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
        return board.getBoxMargin()+0.5f*board.getBoxWidth()+xMap*board.getBoxWidth();
    }

    /**
     * Convert the position from the map to the coordinate in the screen
     * @param yMap
     * @return
     */
    public float getYPosFromMap(int yMap){
        return App.getInstance().getScreenH()/2-0.5f*board.getWidth()+0.5f*board.getBoxWidth()+yMap*board.getBoxWidth();
    }

    public void onSwipeUp(){
        for (Animal a : animalList){
            a.setSpeed(0,-Values.ANIMAL_SPEED);
        }
    }

    public void onSwipeDown(){
        for (Animal a : animalList){
            a.setSpeed(0,Values.ANIMAL_SPEED);
        }
    }

    public void onSwipeLeft(){
        for (Animal a : animalList){
            a.setSpeed(-Values.ANIMAL_SPEED, 0);
        }
    }

    public void onSwipeRight(){
        for (Animal a : animalList){
            a.setSpeed(Values.ANIMAL_SPEED, 0);
        }
    }
}

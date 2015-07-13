package fr.imac.taquinimal.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import fr.imac.taquinimal.App;
import fr.imac.taquinimal.R;
import fr.imac.taquinimal.model.Animal;
import fr.imac.taquinimal.model.Board;
import fr.imac.taquinimal.utils.GameHelper;
import fr.imac.taquinimal.utils.Utils;
import fr.imac.taquinimal.utils.Values;

/**
 * The engine update the view and the models
 * <p/>
 * Created by AG on 23/06/2015.
 */
public class GameEngine implements Animal.AnimalListener {
    private LinkedList<Animal> animalList;
    private HashMap<Animal.AnimalType, Bitmap> animalImageList;

    private Board board;

    private int nbAnimalMoving;
    private GameActivity.Swipe lastEvent;

    //prevent OnMesure to init the game several times
    private boolean isGameInitiated = false;

    public void initGame() {
        if (!isGameInitiated) {
            animalList = new LinkedList<Animal>();
            animalImageList = new HashMap<Animal.AnimalType, Bitmap>();
            board = new Board();

            //init the bitmap with the right size
            loadAnimalBitmaps();

            //add few animals
            fillBoard(Values.NB_ANIMAL_AT_LAUNCH);

            isGameInitiated = true;
        }
    }

    /**
     * Load bitmaps from memory to java objects
     */
    private void loadAnimalBitmaps() {
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
        for (Bitmap b : temp) {
            b.recycle();
        }
    }

    /**
     * Fill the board with n random animal
     *
     * @param n number of animal to add
     */
    private void fillBoard(int n) {
        Random r = new Random();
        Animal a;
        Animal.AnimalType t;
        int[] mapPos;

        for (int i = 0; i < n; ++i) {
            t = Utils.getRandomAnimalType(r);
            mapPos = board.getAvailablePos(r);
            if (mapPos != null) {
                a = new Animal(this, t, animalImageList.get(t), mapPos[0], mapPos[1], GameHelper.getInstance().getXPosFromMap(mapPos[0]), GameHelper.getInstance().getYPosFromMap(mapPos[1]));
                animalList.add(a);
                board.setBox(mapPos[0], mapPos[1], animalList.indexOf(a));
            }
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
        if (animalList != null) {
            for (Animal a : animalList) {
                a.move(lastEvent);
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


    public void onSwipe(GameActivity.Swipe s) {
        Log.d("a", "event : " + s);

        lastEvent = s;
        if (nbAnimalMoving == 0) {
            switch (s) {
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

    public void onSwipeUp() {
        int id = -1;
        for (int j = 0; j <= Values.BOARD_SIZE - 1; ++j) {
            for (int i = 0; i <= Values.BOARD_SIZE - 1; ++i) {
                id = board.getBox(i, j);
                if (id != -1) {
                    animalList.get(id).moveUp(board);
                }
            }
        }
    }

    public void onSwipeDown() {
        int id = -1;
        for (int j = Values.BOARD_SIZE - 1; j >= 0; --j) {
            for (int i = 0; i <= Values.BOARD_SIZE - 1; ++i) {
                id = board.getBox(i, j);
                if (id != -1) {
                    animalList.get(id).moveDown(board);
                }
            }
        }
    }

    public void onSwipeLeft() {
        int id = -1;
        for (int i = 0; i <= Values.BOARD_SIZE - 1; ++i) {
            for (int j = 0; j <= Values.BOARD_SIZE - 1; ++j) {
                id = board.getBox(i, j);
                if (id != -1) {
                    animalList.get(id).moveLeft(board);
                }
            }
        }
    }

    public void onSwipeRight() {
        int id = -1;
        for (int i = Values.BOARD_SIZE - 1; i >= 0; --i) {
            for (int j = 0; j <= Values.BOARD_SIZE - 1; ++j) {
                id = board.getBox(i, j);
                if (id != -1) {
                    animalList.get(id).moveRight(board);
                }
            }
        }
    }

    public GameActivity.Swipe getLastEvent() {
        return lastEvent;
    }

    public void savePosOnBoard(int mapX, int mapY, Animal a) {
        board.setBox(mapX, mapY, animalList.indexOf(a));
    }

    public int getNbAnimalMoving() {
        return nbAnimalMoving;
    }

    public void setNbAnimalMoving(int nbAnimalMoving) {
        this.nbAnimalMoving = nbAnimalMoving;
    }

    @Override
    public void targetNewPos(int mapX, int mapY, Animal a) {
        savePosOnBoard(mapX, mapY, a);
    }

    @Override
    public void leavingPos(int mapX, int mapY) {
        board.setBox(mapX, mapY, -1);
    }

    @Override
    public void stoppedMoving() {
        --nbAnimalMoving;
    }

    @Override
    public void startMoving() {
        ++nbAnimalMoving;
    }


}

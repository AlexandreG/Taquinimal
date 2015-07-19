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
import fr.imac.taquinimal.model.AnimalType;
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
    private LinkedList<Animal> animalToRemove;
    private int nbAnimalToAdd;

    private HashMap<AnimalType, Bitmap> animalImageList;

    private Board board;

    private int nbAnimalMoving;
    private GameActivity.Swipe lastEvent;

    //prevent OnMesure to init the game several times
    private boolean isGameInitiated = false;
    private Random r;

    public void initGame() {
        if (!isGameInitiated) {
            r = new Random();
            animalList = new LinkedList<Animal>();
            animalToRemove = new LinkedList<Animal>();
            nbAnimalToAdd = 0;
            animalImageList = new HashMap<AnimalType, Bitmap>();
            board = new Board();

            //init the bitmap with the right size
            loadAnimalBitmaps();

            //add few animals
            addAnimal(Values.NB_ANIMAL_AT_LAUNCH);

            isGameInitiated = true;
        }
    }

    /**
     * Load bitmaps from memory to java objects
     */
    private void loadAnimalBitmaps() {
        //load the bitmaps
        LinkedList<Bitmap> temp = new LinkedList<Bitmap>();
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.croco));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.bear));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.cat));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.owl));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.snake));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.mouse));
        temp.add(BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.elephant));

        //resize the bitmaps
        animalImageList.put(AnimalType.CROCO, Utils.getResizedBitmap(temp.get(0), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(AnimalType.BEAR, Utils.getResizedBitmap(temp.get(1), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(AnimalType.CAT, Utils.getResizedBitmap(temp.get(2), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(AnimalType.OWL, Utils.getResizedBitmap(temp.get(3), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(AnimalType.SNAKE, Utils.getResizedBitmap(temp.get(4), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(AnimalType.MOUSE, Utils.getResizedBitmap(temp.get(5), (int) board.getBoxWidth(), (int) board.getBoxWidth()));
        animalImageList.put(AnimalType.ELEPHANT, Utils.getResizedBitmap(temp.get(6), (int) board.getBoxWidth(), (int) board.getBoxWidth()));

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
    private void addAnimal(int n) {
        Animal a;
        AnimalType type;
        int[] mapPos;

        for (int i = 0; i < n; ++i) {
            type = Utils.getRandomAnimalType(r);
            Log.d("a", "new animal : " + type);
            mapPos = board.getAvailablePos(r);
            if (mapPos != null) {
                a = new Animal(this, type, animalImageList.get(type), mapPos[0], mapPos[1], GameHelper.getInstance().getXPosFromMap(mapPos[0]), GameHelper.getInstance().getYPosFromMap(mapPos[1]));
                animalList.add(a);
                board.setBox(mapPos[0], mapPos[1], a);
            }
        }
        //todo: deal with the game over
    }

    /**
     * Update the game
     */
    public void update() {
        moveAll();
        drawAll();
        updateAnimalList();
    }

    /**
     * Add new animals and remove the killed ones
     */
    private void updateAnimalList() {
        if(animalList != null){
            synchronized (animalList) {
                if (nbAnimalToAdd != 0) {
                    addAnimal(nbAnimalToAdd);
                    nbAnimalToAdd = 0;

                    //if we have to had animals, that mean that we finished the animation,
                    //so we can remove the killed ones
                    for (Animal a : animalToRemove) {
                        animalList.remove(a);
                        Log.d("zdq", "removed 1");
                    }
                    animalToRemove.clear();
                }
            }
        }
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
        Animal a = null;
        for (int j = 0; j <= Values.BOARD_SIZE - 1; ++j) {
            for (int i = 0; i <= Values.BOARD_SIZE - 1; ++i) {
                a = board.getBox(i, j);
                if (a != null) {
                    a.moveUp(board);
                }
            }
        }
    }

    public void onSwipeDown() {
        Animal a = null;
        for (int j = Values.BOARD_SIZE - 1; j >= 0; --j) {
            for (int i = 0; i <= Values.BOARD_SIZE - 1; ++i) {
                a = board.getBox(i, j);
                if (a != null) {
                    a.moveDown(board);
                }
            }
        }
    }

    public void onSwipeLeft() {
        Animal a = null;
        for (int i = 0; i <= Values.BOARD_SIZE - 1; ++i) {
            for (int j = 0; j <= Values.BOARD_SIZE - 1; ++j) {
                a = board.getBox(i, j);
                if (a != null) {
                    a.moveLeft(board);
                }
            }
        }
    }

    public void onSwipeRight() {
        Animal a = null;
        for (int i = Values.BOARD_SIZE - 1; i >= 0; --i) {
            for (int j = 0; j <= Values.BOARD_SIZE - 1; ++j) {
                a = board.getBox(i, j);
                if (a != null) {
                    a.moveRight(board);
                }
            }
        }
    }

    public GameActivity.Swipe getLastEvent() {
        return lastEvent;
    }

    public void savePosOnBoard(int mapX, int mapY, Animal a) {
        board.setBox(mapX, mapY, a);
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
        board.setBox(mapX, mapY, null);
    }

    @Override
    public void stoppedMoving() {
        --nbAnimalMoving;
        if (nbAnimalMoving == 0) {
            ++nbAnimalToAdd;
        }
    }

    @Override
    public void startMoving() {
        ++nbAnimalMoving;
    }

    @Override
    public void willEatAnimal(Animal a) {
        animalToRemove.add(a);
    }


}

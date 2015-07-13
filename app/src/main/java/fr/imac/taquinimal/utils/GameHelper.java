package fr.imac.taquinimal.utils;

import java.util.Random;

import fr.imac.taquinimal.model.Animal;

/**
 * Created by AG on 13/07/2015.
 */
public class GameHelper {
    private static GameHelper sInstance;

    private int boardWidth;
    private int boxWidth;

    private GameHelper() {
    }


    public static synchronized GameHelper getInstance() {
        if (sInstance == null) {
            sInstance = new GameHelper();
        }
        return sInstance;
    }


    /**
     * Convert the position from the map to the coordinate in the screen
     *
     * @param xMap
     * @return
     */
    public float getXPosFromMap(int xMap) {
        return 0.5f * boxWidth + xMap * boxWidth;
    }

    /**
     * Convert the position from the map to the coordinate in the screen
     *
     * @param yMap
     * @return
     */
    public float getYPosFromMap(int yMap) {
        return 0.5f * boxWidth + yMap * boxWidth;
        //return App.getInstance().getScreenH()/2-0.5f*board.getWidth()+0.5f*boardWidth+yMap*boardWidth;
    }

    /**
     * Convert the coordinate in the screen to the position in the map
     */
    public int getXMapFromPos(float x) {
        //Log.d("a", "x " + x + " between 0 and " + boxWidth);
        for (int i = 0; i < Values.BOARD_SIZE; ++i) {
            if (i * boxWidth <= x && x <= (i + 1) * boxWidth) {
                //Log.d("a", "returned " + i);
                return i;
            }
        }
        return -1;
    }

    /**
     * Convert the coordinate in the screen to the position in the map
     */
    public int getYMapFromPos(float y) {
        for (int i = 0; i < Values.BOARD_SIZE; ++i) {
            if (i * boxWidth <= y && y <= (i + 1) * boxWidth) {
                return i;
            }
        }
        return -1;
    }

    /*------------------------------------------- Getters and Setters */
    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public void setBoxWidth(int boxWidth) {
        this.boxWidth = boxWidth;
    }
}

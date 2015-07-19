package fr.imac.taquinimal.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.LinkedList;
import java.util.Random;

import fr.imac.taquinimal.App;
import fr.imac.taquinimal.R;
import fr.imac.taquinimal.utils.GameHelper;
import fr.imac.taquinimal.utils.Utils;
import fr.imac.taquinimal.utils.Values;

/**
 * The board of the game
 * <p/>
 * Created by AG on 23/06/2015.
 */
public class Board {
    private final float width;
    private final int x;
    private final int y;
    private Animal[][] map;//the map of the game : -1 if empty, id of the animal in the list else
    private Bitmap bp;
    private float boxWidth;

    public Board() {
        map = new Animal[Values.BOARD_SIZE][Values.BOARD_SIZE];
        for (int i = 0; i < Values.BOARD_SIZE; ++i) {
            for (int j = 0; j < Values.BOARD_SIZE; ++j) {
                map[i][j] = null;
            }
        }

        width = GameHelper.getInstance().getBoardWidth();
        x = (int) width / 2;
        y = (int) width / 2;

        boxWidth = width / Values.BOARD_SIZE;

        Bitmap temp = BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.board_background);
        bp = Utils.getResizedBitmap(temp, (int) width, (int) width);
        temp.recycle();
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bp, x - bp.getWidth() / 2, y - bp.getHeight() / 2, paint);
    }

    public float getBoxWidth() {
        return boxWidth;
    }

    /**
     * indicate if the box at i,j is empty or not
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isMapEmpty(int i, int j) {
        if (map[i][j] == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Look at all positions and select one of the empty
     *
     * @param r the random to use
     * @return the position or null if we are full
     */
    public int[] getAvailablePos(Random r) {
        LinkedList<int[]> availablePos = new LinkedList<int[]>();
        //First we list all availables positions
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map.length; ++j) {
                if (map[i][j] == null) {
                    availablePos.add(new int[]{i, j});
                }
            }
        }
        if (availablePos.size() == 0) {
            return null;
        } else {
            return availablePos.get(r.nextInt(availablePos.size()));
        }
    }

    /**
     * Return the animal at the given pos or -1 if empty
     */
    public Animal getBox(int i, int j) {
        return map[i][j];
    }

    /**
     * Save in the map the given animal or -1 if empty
     */
    public void setBox(int i, int j, Animal id) {
        map[i][j] = id;
    }

    public float getWidth() {
        return width;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < map.length; ++i) {
            sb.append("\n");
            for (int j = 0; j < map.length; ++j) {
                sb.append(map[i][j]);
                sb.append(" ");

            }
        }
        return sb.toString();
    }
}

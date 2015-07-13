package fr.imac.taquinimal.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

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
    private int[][] map;//the map of the game : -1 if empty, id of the animal in the list else
    private Bitmap bp;
    private float boxWidth;

    public Board() {
        map = new int[Values.BOARD_SIZE][Values.BOARD_SIZE];
        for (int i = 0; i < Values.BOARD_SIZE; ++i) {
            for (int j = 0; j < Values.BOARD_SIZE; ++j) {
                map[i][j] = -1;
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
        if (map[i][j] == -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Save in the map the id of the given animal or -1 if empty
     */
    public void setBox(int i, int j, int id) {
        map[i][j] = id;
    }

    public float getWidth() {
        return width;
    }
}

package fr.imac.taquinimal.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import fr.imac.taquinimal.App;
import fr.imac.taquinimal.R;
import fr.imac.taquinimal.utils.Utils;
import fr.imac.taquinimal.utils.Values;

/** The board of the game
 *
 * Created by AG on 23/06/2015.
 */
public class Board {
    private int[][] map;
    private Bitmap bp;

    private final float width;
    private final int x;
    private final int y;

    private float boxWidth;
    private float boxMargin;

    public Board(){
        map = new int[Values.BOARD_SIZE][Values.BOARD_SIZE];
        for(int i=0; i<Values.BOARD_SIZE ; ++i){
            for(int j=0; j<Values.BOARD_SIZE ; ++j) {
                map[i][j] = 0;
            }
        }

        boxMargin = Values.BOARD_W_MARGIN*App.getInstance().getScreenW();
        width = App.getInstance().getScreenW()-2*boxMargin;
        x = App.getInstance().getScreenW()/2;
        y = App.getInstance().getScreenH()/2;

        boxWidth = width/Values.BOARD_SIZE;

        Bitmap temp = BitmapFactory.decodeResource(App.getInstance().getContext().getResources(), R.drawable.board_background);
        bp = Utils.getResizedBitmap(temp, (int)width, (int)width);
        temp.recycle();
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bp, x - bp.getWidth() / 2, y - bp.getHeight() / 2, paint);
    }

    public float getBoxWidth(){
        return boxWidth;
    }

    /**
     * indicate if the box at i,j is empty or not
     * @param i
     * @param j
     * @return
     */
    public boolean isMapEmpty(int i, int j){
        if(map[i][j] == 0){
            return true;
        }else {
            return false;
        }
    }

    public float getBoxMargin() {
        return boxMargin;
    }

    public float getWidth() {
        return width;
    }
}

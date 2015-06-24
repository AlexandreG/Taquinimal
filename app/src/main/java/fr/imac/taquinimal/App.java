package fr.imac.taquinimal;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import fr.imac.taquinimal.controller.GameActivity;
import fr.imac.taquinimal.utils.Values;

/**
 * Created by AG on 23/06/2015.
 */
public class App extends Application {
    private static App sInstance;
    private Context context;
    private GameActivity gameActivity;

    //the screen size
    private int screenW;
    private int screenH;

    private int boardWidth;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        sInstance = this;
        sInstance.initializeInstance();
    }

    private void initializeInstance() {
        context = getApplicationContext();

        initScreenSize();
    }

    private void initScreenSize() {
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            w.getDefaultDisplay().getSize(size);
            screenW = size.x;
            screenH = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            screenW = d.getWidth();
            screenH = d.getHeight();
        }
    }

    public void initGameActivity(GameActivity ga) {
        gameActivity = ga;
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized App getInstance() {
        return sInstance;
    }

    public Context getContext() {
        return context;
    }

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public int getScreenH(){
        return screenH;
    }

    public int getScreenW(){
        return screenW;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }
}


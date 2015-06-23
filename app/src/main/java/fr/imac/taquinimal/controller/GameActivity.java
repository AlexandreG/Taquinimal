package fr.imac.taquinimal.controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import fr.imac.taquinimal.App;
import fr.imac.taquinimal.utils.OnSwipeTouchListener;
import fr.imac.taquinimal.view.GameView;


public class GameActivity extends Activity {
    private GameView view;
    private GameThread gameThread;
    private GameEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        App.getInstance().initGameActivity(this);

        engine = new GameEngine();
        view = new GameView(getApplicationContext(), engine);
        setContentView(view);
        view.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
                engine.onSwipeUp();
            }

            public void onSwipeRight() {
                engine.onSwipeRight();
            }

            public void onSwipeLeft() {
                engine.onSwipeLeft();
            }

            public void onSwipeBottom() {
                engine.onSwipeDown();
            }
        });

        gameThread = new GameThread(engine);
        gameThread.setState(GameThread.RUNNING);
        gameThread.start();
    }

    //TODO: add listeners


    /**
     * Ask the view to refresh itself
     */
    public void updateView() {
        if (view != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.invalidate();
                }
            });
        }
    }

    public GameEngine getEngine() {
        return engine;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //If paused, restart the game
        if (gameThread.getState().equals(GameThread.PAUSED)) {
            gameThread = new GameThread(engine);
            gameThread.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //stop game
        gameThread.setState(GameThread.PAUSED);

        //TODO: save progress
    }
}

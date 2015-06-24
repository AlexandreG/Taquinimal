package fr.imac.taquinimal.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import fr.imac.taquinimal.App;
import fr.imac.taquinimal.R;
import fr.imac.taquinimal.utils.OnSwipeTouchListener;
import fr.imac.taquinimal.view.GameView;


public class GameActivity extends Activity {
    private GameView view;
    private GameThread gameThread;
    private GameEngine engine;

    private LinearLayout globalWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getInstance().initGameActivity(this);

        super.onCreate(savedInstanceState);

        //Full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        engine = new GameEngine();
        setContentView(R.layout.activity_game);

        view = (GameView) findViewById(R.id.gameview);

        globalWrapper = (LinearLayout) findViewById(R.id.global_wrapper);

        globalWrapper.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
                engine.onSwipe(Swipe.UP);
            }

            public void onSwipeRight() {
                engine.onSwipe(Swipe.RIGHT);
            }

            public void onSwipeLeft() {
                engine.onSwipe(Swipe.LEFT);
            }

            public void onSwipeBottom() {
                engine.onSwipe(Swipe.DOWN);
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

    public enum Swipe {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }
}

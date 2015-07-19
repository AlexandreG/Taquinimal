package fr.imac.taquinimal.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import fr.imac.taquinimal.App;
import fr.imac.taquinimal.R;
import fr.imac.taquinimal.view.OnSwipeTouchListener;
import fr.imac.taquinimal.view.GameView;


public class GameActivity extends Activity {
    private GameView view;
    private GameThread gameThread;
    private GameEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getInstance().initGameActivity(this);
        engine = new GameEngine();

        setFullScreen();
        setContentView(R.layout.activity_game);

        initViews();
        initListeners();
        startGame();
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initViews() {
        view = (GameView) findViewById(R.id.gameview);
    }

    private void initListeners() {
        findViewById(R.id.global_wrapper).setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
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
    }

    /**
     * Init and start gameThread
     */
    private void startGame() {
        gameThread = new GameThread(engine);
        gameThread.setState(GameThread.RUNNING);
        gameThread.start();
    }


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

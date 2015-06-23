package fr.imac.taquinimal.controller;

import android.util.Log;

import fr.imac.taquinimal.utils.Values;

/**
 * The purpose of this class is to remind the engine to update the game at each frame.
 * <p/>
 * Created by AG on 23/06/2015.
 */
public class GameThread extends Thread {
    public final static int RUNNING = 1;
    public final static int PAUSED = 2;//state when leave the app
    private static final int MAX_FRAME_SKIPS = 5;

    private long delay = 1000000000L / Values.FPS;
    private long beforeTime = 0;
    private long afterTime = 0;
    private long timeDiff = 0;
    private long sleepTime;
    private long overSleepTime = 0;
    private long excess = 0;

    private int state;
    private GameEngine engine;

    public GameThread(GameEngine engine) {
        int state = RUNNING;
        this.engine = engine;
    }


    @Override
    public void run() {
        while (state == RUNNING) {
            beforeTime = System.nanoTime();

            //update the game
            engine.update();

            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = ((delay) - timeDiff) - overSleepTime;

            if (sleepTime > 0) {
                try {
                    sleep(sleepTime / 1000000L);
                } catch (InterruptedException ex) {
                }
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            } else {
                excess -= sleepTime;
                overSleepTime = 0L;
            }

            int skips = 0;
            while ((excess > delay) && (skips < MAX_FRAME_SKIPS)) {
                excess -= delay;
                engine.update();
                skips++;
            }
        }
    }

    public void setState(int state) {
        this.state = state;
    }
}

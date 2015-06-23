package fr.imac.taquinimal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import fr.imac.taquinimal.controller.GameEngine;
import fr.imac.taquinimal.model.Animal;

/** The Android view of the game
 *
 * Created by AG on 23/06/2015.
 */
public class GameView extends View {
    private GameEngine engine;

    private Paint paint;

    public GameView(Context context, GameEngine engine) {
        super(context);
        init(engine);
    }

    private void init(GameEngine engine) {
        this.engine = engine;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(6);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        engine.getBoard().draw(canvas, paint);

        for (Animal a : engine.getAnimalList()) {
            a.draw(canvas, paint);
        }

    }
}

package fr.imac.taquinimal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import fr.imac.taquinimal.App;
import fr.imac.taquinimal.controller.GameActivity;
import fr.imac.taquinimal.controller.GameEngine;
import fr.imac.taquinimal.model.Animal;
import fr.imac.taquinimal.utils.GameHelper;
import fr.imac.taquinimal.utils.Values;

/**
 * The Android view of the game
 * <p/>
 * Created by AG on 23/06/2015.
 */
public class GameView extends View {
    private final double mScale = 1.0;

    private GameEngine engine;

    private Paint paint;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.engine = ((GameActivity) getContext()).getEngine();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(6);

//        //Resize view
//        int w = (int) (App.getInstance().getBoardWidth()*(1- Values.BOARD_W_MARGIN));
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
//        params.height = w;
//        this.setLayoutParams(params);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        engine.getBoard().draw(canvas, paint);

        for (Animal a : engine.getAnimalList()) {
            a.draw(canvas, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        GameHelper.getInstance().setBoardWidth(width);
        GameHelper.getInstance().setBoxWidth(width / Values.BOARD_SIZE);

        setMeasuredDimension(width, width);

        //now we have the size of the view, we can init the game
        App.getInstance().getGameActivity().getEngine().initGame();

    }

}

package edu.up.cs301.sorry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class GameBoardView extends View {
    private Paint gridPaint;
    private Paint textPaint;
    private Paint dotPaint;
    private int cellSize;
    private int boardSize;
    private Bitmap boardImage;
    private int margin;
    private RectF outlineRect;
    private int currentDotPosition;
    private int targetDotPosition;
    private float dotX, dotY;
    private float targetDotX, targetDotY;
    private long animationStartTime;
    private long animationDuration = 800; // Animation duration in milliseconds

    public GameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gridPaint = new Paint();
        gridPaint.setColor(Color.RED);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(2f);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        dotPaint = new Paint();
        dotPaint.setColor(Color.BLUE);
        dotPaint.setStyle(Paint.Style.FILL);

        boardImage = BitmapFactory.decodeResource(getResources(), R.drawable.sorryimage);
        margin = 25;
        outlineRect = new RectF();
        currentDotPosition = 1;
        targetDotPosition = 1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        boardSize = Math.min(w, h);
        cellSize = (boardSize - 2 * margin) / 15;
        updateOutlineRect();
        updateDotPosition();
        updateTargetDotPosition();
    }

    private void updateOutlineRect() {
        int outlineSize = boardSize - 2 * margin;
        int left = (boardSize - outlineSize) / 2;
        int top = (boardSize - outlineSize) / 2;
        int right = left + outlineSize;
        int bottom = top + outlineSize;
        outlineRect.set(left, top, right, bottom);
    }

    private void updateDotPosition() {
        int col = (currentDotPosition - 1) % 15;
        int row = (currentDotPosition - 1) / 15;
        dotX = margin + col * cellSize + cellSize / 2;
        dotY = margin + row * cellSize + cellSize / 2;
    }

    private void updateTargetDotPosition() {
        int col = (targetDotPosition - 1) % 15;
        int row = (targetDotPosition - 1) / 15;
        targetDotX = margin + col * cellSize + cellSize / 2;
        targetDotY = margin + row * cellSize + cellSize / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(boardImage, null, new RectF(0, 0, boardSize, boardSize), null);

        Bitmap outlineBitmap = Bitmap.createBitmap(boardSize, boardSize, Bitmap.Config.ARGB_8888);
        Canvas outlineCanvas = new Canvas(outlineBitmap);

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int index = i * 15 + j;
                int x = margin + j * cellSize;
                int y = margin + i * cellSize;
                outlineCanvas.drawRect(x, y, x + cellSize, y + cellSize, gridPaint);
                outlineCanvas.drawText(String.valueOf(index + 1), x + cellSize / 2, y + cellSize / 2, textPaint);
            }
        }

        canvas.drawBitmap(outlineBitmap, null, outlineRect, null);

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - animationStartTime;
        float t = Math.min(1f, (float) elapsedTime / animationDuration);

        float x = dotX + t * (targetDotX - dotX);
        float y = dotY + t * (targetDotY - dotY);

        int dotRadius = cellSize / 2;
        canvas.drawCircle(x, y, dotRadius, dotPaint);

        if (t < 1f) {
            invalidate();
        } else {
            currentDotPosition = targetDotPosition;
            dotX = targetDotX;
            dotY = targetDotY;
        }
    }

    public void moveDotTo(int position) {
        if (position >= 1 && position <= 225) {
            targetDotPosition = position;
            updateTargetDotPosition();
            animationStartTime = System.currentTimeMillis();
            invalidate();
        }
    }

    public void setAnimationDuration(long duration) {
        animationDuration = duration;
    }

    public void setMargin(int margin) {
        this.margin = margin;
        updateOutlineRect();
        updateDotPosition();
        updateTargetDotPosition();
        invalidate();
    }
}
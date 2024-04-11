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
    private int currentDotPosition = 1; // Start the dot at grid 1
    private int targetDotPosition = 1;
    private boolean isAnimating = false;
    private float dotX, dotY;

    public GameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void moveDotTo(int position) {
        if (position >= 1 && position <= 225) {
            targetDotPosition = position;
            isAnimating = true;
            invalidate(); // Request a redraw of the view
        }
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
        margin = 25; // Set the initial margin size (in pixels)
        outlineRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        boardSize = Math.min(w, h);
        cellSize = (boardSize - 2 * margin) / 15; // Adjust the cell size based on the margin
        updateOutlineRect();
    }

    private void updateOutlineRect() {
        int outlineSize = boardSize - 2 * margin;
        int left = (boardSize - outlineSize) / 2;
        int top = (boardSize - outlineSize) / 2;
        int right = left + outlineSize;
        int bottom = top + outlineSize;
        outlineRect.set(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the game board image
        canvas.drawBitmap(boardImage, null, new RectF(0, 0, boardSize, boardSize), null);

        // Create a separate canvas for the outline
        Bitmap outlineBitmap = Bitmap.createBitmap(boardSize, boardSize, Bitmap.Config.ARGB_8888);
        Canvas outlineCanvas = new Canvas(outlineBitmap);

        // Draw the grid and numbers on the outline canvas
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int index = i * 15 + j;
                int x = margin + j * cellSize;
                int y = margin + i * cellSize;
                outlineCanvas.drawRect(x, y, x + cellSize, y + cellSize, gridPaint);
                outlineCanvas.drawText(String.valueOf(index + 1), x + cellSize / 2, y + cellSize / 2, textPaint);
            }
        }

        // Draw the outline bitmap on the main canvas
        canvas.drawBitmap(outlineBitmap, null, outlineRect, null);

        // Calculate the position of the dot
        int dotRadius = cellSize / 2;
        if (isAnimating) {
            // Interpolate the position of the dot during animation
            float t = (float) (System.currentTimeMillis() % 500) / 500f;
            dotX = (1 - t) * getPositionX(currentDotPosition) + t * getPositionX(targetDotPosition);
            dotY = (1 - t) * getPositionY(currentDotPosition) + t * getPositionY(targetDotPosition);

            if (t >= 1) {
                currentDotPosition = targetDotPosition;
                isAnimating = false;
            }
        } else {
            dotX = getPositionX(currentDotPosition);
            dotY = getPositionY(currentDotPosition);
        }

        // Draw the dot
        canvas.drawCircle(dotX, dotY, dotRadius, dotPaint);

        // Redraw the view if animating
        if (isAnimating) {
            invalidate();
        }
    }

    private float getPositionX(int position) {
        int col = (position - 1) % 15;
        return margin + col * cellSize + cellSize / 2;
    }

    private float getPositionY(int position) {
        int row = (position - 1) / 15;
        return margin + row * cellSize + cellSize / 2;
    }

    public void setMargin(int margin) {
        this.margin = margin;
        updateOutlineRect();
        invalidate(); // Request a redraw of the view
    }
}
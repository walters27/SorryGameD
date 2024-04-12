package edu.up.cs301.sorry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class GameBoardView extends View {
    private Paint gridPaint;
    private Paint textPaint;
    private Paint highlightPaint;
    private int cellSize;
    private int boardSize;
    private Bitmap boardImage;
    private int margin;
    private RectF outlineRect;
    private SorryPawn currentPawn;
    private SorryPawn targetPawn;
    private float pawnX, pawnY;
    private float targetPawnX, targetPawnY;
    private long animationStartTime;
    private long animationDuration = 800;
    private List<Integer> validMovePositions;

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

        highlightPaint = new Paint();
        highlightPaint.setColor(Color.YELLOW);
        highlightPaint.setStyle(Paint.Style.FILL);
        highlightPaint.setAlpha(128);

        boardImage = BitmapFactory.decodeResource(getResources(), R.drawable.sorryimage);
        margin = 25;
        outlineRect = new RectF();
        currentPawn = new SorryPawn(Color.BLUE, R.drawable.blue_pawn);
        targetPawn = new SorryPawn(Color.BLUE, R.drawable.blue_pawn);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        boardSize = Math.min(w, h);
        cellSize = (boardSize - 2 * margin) / 15;
        updateOutlineRect();
        updatePawnPosition();
        updateTargetPawnPosition();
    }

    private void updateOutlineRect() {
        int outlineSize = boardSize - 2 * margin;
        int left = (boardSize - outlineSize) / 2;
        int top = (boardSize - outlineSize) / 2;
        int right = left + outlineSize;
        int bottom = top + outlineSize;
        outlineRect.set(left, top, right, bottom);
    }

    private void updatePawnPosition() {
        int col = (currentPawn.location - 1) % 15;
        int row = (currentPawn.location - 1) / 15;
        pawnX = margin + col * cellSize + cellSize / 2;
        pawnY = margin + row * cellSize + cellSize / 2;
    }

    private void updateTargetPawnPosition() {
        int col = (targetPawn.location - 1) % 15;
        int row = (targetPawn.location - 1) / 15;
        targetPawnX = margin + col * cellSize + cellSize / 2;
        targetPawnY = margin + row * cellSize + cellSize / 2;
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

                if (validMovePositions != null && validMovePositions.contains(index + 1)) {
                    outlineCanvas.drawRect(x, y, x + cellSize, y + cellSize, highlightPaint);
                }
            }
        }

        canvas.drawBitmap(outlineBitmap, null, outlineRect, null);

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - animationStartTime;
        float t = Math.min(1f, (float) elapsedTime / animationDuration);

        int col = (int) ((currentPawn.location - 1) % 15 + t * ((targetPawn.location - 1) % 15 - (currentPawn.location - 1) % 15));
        int row = (int) ((currentPawn.location - 1) / 15 + t * ((targetPawn.location - 1) / 15 - (currentPawn.location - 1) / 15));

        int x = margin + col * cellSize;
        int y = margin + row * cellSize;

        // Load the pawn image
        Drawable pawnDrawable = getResources().getDrawable(currentPawn.getImageResourceId());
        Bitmap pawnBitmap = ((BitmapDrawable) pawnDrawable).getBitmap();

        // Calculate the size of the pawn image to fit the cell
        int pawnSize = (int) (cellSize * 0.8); // Adjust the scaling factor as needed
        Bitmap resizedPawnBitmap = Bitmap.createScaledBitmap(pawnBitmap, pawnSize, pawnSize, true);

        // Calculate the position to center the pawn image within the cell
        int pawnX = x + (cellSize - pawnSize) / 2;
        int pawnY = y + (cellSize - pawnSize) / 2;

        // Draw the pawn image
        canvas.drawBitmap(resizedPawnBitmap, pawnX, pawnY, null);

        if (t < 1f) {
            invalidate();
        } else {
            currentPawn = new SorryPawn(targetPawn);
        }
    }

    public void movePawnTo(int position) {
        if (position >= 1 && position <= 225) {
            targetPawn.location = position;
            updateTargetPawnPosition();
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
        updatePawnPosition();
        updateTargetPawnPosition();
        invalidate();
    }

    public void highlightValidMoves(List<Integer> validPositions) {
        validMovePositions = validPositions;
        invalidate();
    }
}
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
        currentPawn.location = 1; // Start the pawn at the first box
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

    public void moveClockwise(int numSpaces) {
        int currentLocation = currentPawn.location;
        int newLocation = currentLocation;

        for (int i = 0; i < numSpaces; i++) {
            if (currentLocation == 2) {
                newLocation = 3;
            } else if (currentLocation == 3) {
                newLocation = 4;
            } else if (currentLocation == 4) {
                newLocation = 5;
            } else if (currentLocation == 5) {
                newLocation = 6;
            } else if (currentLocation == 6) {
                newLocation = 7;
            } else if (currentLocation == 7) {
                newLocation = 8;
            } else if (currentLocation == 8) {
                newLocation = 9;
            } else if (currentLocation == 9) {
                newLocation = 10;
            } else if (currentLocation == 10) {
                newLocation = 11;
            } else if (currentLocation == 11) {
                newLocation = 12;
            } else if (currentLocation == 12) {
                newLocation = 13;
            } else if (currentLocation == 13) {
                newLocation = 14;
            } else if (currentLocation == 14) {
                newLocation = 15;
            } else if (currentLocation == 15) {
                newLocation = 30;
            } else if (currentLocation == 30) {
                newLocation = 45;
            } else if (currentLocation == 45) {
                newLocation = 60;
            } else if (currentLocation == 60) {
                newLocation = 75;
            } else if (currentLocation == 75) {
                newLocation = 90;
            } else if (currentLocation == 90) {
                newLocation = 105;
            } else if (currentLocation == 105) {
                newLocation = 120;
            } else if (currentLocation == 120) {
                newLocation = 135;
            } else if (currentLocation == 135) {
                newLocation = 150;
            } else if (currentLocation == 150) {
                newLocation = 165;
            } else if (currentLocation == 165) {
                newLocation = 180;
            } else if (currentLocation == 180) {
                newLocation = 195;
            } else if (currentLocation == 195) {
                newLocation = 210;
            } else if (currentLocation == 210) {
                newLocation = 225;
            } else if (currentLocation == 225) {
                newLocation = 224;
            } else if (currentLocation == 224) {
                newLocation = 223;
            } else if (currentLocation == 223) {
                newLocation = 222;
            } else if (currentLocation == 222) {
                newLocation = 221;
            } else if (currentLocation == 221) {
                newLocation = 220;
            } else if (currentLocation == 220) {
                newLocation = 219;
            } else if (currentLocation == 219) {
                newLocation = 218;
            } else if (currentLocation == 218) {
                newLocation = 217;
            } else if (currentLocation == 217) {
                newLocation = 216;
            } else if (currentLocation == 216) {
                newLocation = 215;
            } else if (currentLocation == 215) {
                newLocation = 214;
            } else if (currentLocation == 214) {
                newLocation = 213;
            } else if (currentLocation == 213) {
                newLocation = 212;
            } else if (currentLocation == 212) {
                newLocation = 211;
            } else if (currentLocation == 211) {
                newLocation = 196;
            } else if (currentLocation == 196) {
                newLocation = 181;
            } else if (currentLocation == 181) {
                newLocation = 166;
            } else if (currentLocation == 166) {
                newLocation = 151;
            } else if (currentLocation == 151) {
                newLocation = 136;
            } else if (currentLocation == 136) {
                newLocation = 121;
            } else if (currentLocation == 121) {
                newLocation = 106;
            } else if (currentLocation == 106) {
                newLocation = 91;
            } else if (currentLocation == 91) {
                newLocation = 76;
            } else if (currentLocation == 76) {
                newLocation = 61;
            } else if (currentLocation == 61) {
                newLocation = 46;
            } else if (currentLocation == 46) {
                newLocation = 31;
            } else if (currentLocation == 31) {
                newLocation = 16;
            } else if (currentLocation == 16) {
                newLocation = 1;
            } else if (currentLocation == 1) {
                newLocation = 2;
            }

            currentLocation = newLocation;
        }

        movePawnTo(newLocation);
    }
}
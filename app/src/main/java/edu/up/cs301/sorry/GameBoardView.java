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

import java.util.ArrayList;
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
    public List<SorryPawn> pawns;
    public SorryPawn currentPawn;
    public SorryPawn targetPawn;
    public boolean youWon = false;
    public boolean youLost = false;
    private long animationStartTime;
    private static final long animationDuration = 500; // Animation duration in milliseconds

    public GameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Paint grid
        gridPaint = new Paint();
        gridPaint.setColor(Color.RED);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(2f);

        // Text for grid
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Highlight grid that pawn can move to
        highlightPaint = new Paint();
        highlightPaint.setColor(Color.YELLOW);
        highlightPaint.setStyle(Paint.Style.FILL);
        highlightPaint.setAlpha(128);

        // Load board image
        boardImage = BitmapFactory.decodeResource(getResources(), R.drawable.sorryimage);
        margin = 50;
        outlineRect = new RectF();
        pawns = new ArrayList<>();

        // Initialize pawns in starting position
        initializePawns();
    }

    private void initializePawns() {
        int[][] locations = {
                {75},  // Blue
                {5},   // Red
                {221}, // Yellow
                {151}  // Green
        };
        int[] colors = {Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN};
        int[] drawableIds = {R.drawable.blue_pawn, R.drawable.red_pawn, R.drawable.yellow_pawn, R.drawable.green_pawn};

        for (int colorIndex = 0; colorIndex < colors.length; colorIndex++) {
            for (int location : locations[colorIndex]) {
                SorryPawn pawn = new SorryPawn(colors[colorIndex], drawableIds[colorIndex]);
                pawn.location = location;
                pawns.add(pawn);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        boardSize = Math.min(w, h);
        cellSize = (boardSize - 2 * margin) / 15;
        updateOutlineRect();
    }

    private void updateOutlineRect() {
        int outlineSize = boardSize - 2 * margin;
        outlineRect.set(margin, margin, margin + outlineSize, margin + outlineSize);
    }

    private float interpolate(float start, float end, float t) {
        return start + (end - start) * t;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(boardImage, null, new RectF(0, 0, boardSize, boardSize), null);

        // Draw grid and numbers
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int index = i * 15 + j;
                float x = margin + j * cellSize;
                float y = margin + i * cellSize;
                canvas.drawRect(x, y, x + cellSize, y + cellSize, gridPaint);
                canvas.drawText(String.valueOf(index + 1), x + cellSize / 2, y + cellSize / 2, textPaint);
            }
        }

        // Draw pawns
        for (SorryPawn pawn : pawns) {
            int pawnLocation = pawn.location;
            int pawnRow = (pawnLocation - 1) / 15;
            int pawnCol = (pawnLocation - 1) % 15;

            // Highlight the current pawn's location
            if (pawn == currentPawn) {
                float highlightX = margin + pawnCol * cellSize;
                float highlightY = margin + pawnRow * cellSize;
                canvas.drawRect(highlightX, highlightY, highlightX + cellSize, highlightY + cellSize, highlightPaint);
            }

            // Animate the pawn movement
            if (pawn == currentPawn && targetPawn != null) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - animationStartTime;
                float t = Math.min(1f, (float) elapsedTime / animationDuration);

                float interpolatedCol = interpolate(pawnCol, (targetPawn.location - 1) % 15, t);
                float interpolatedRow = interpolate(pawnRow, (targetPawn.location - 1) / 15, t);
                pawnRow = (int) interpolatedRow;
                pawnCol = (int) interpolatedCol;
            }

            // Draw the pawn
            int x = margin + pawnCol * cellSize;
            int y = margin + pawnRow * cellSize;

            Drawable pawnDrawable = getResources().getDrawable(pawn.getImageResourceId());
            Bitmap pawnBitmap = ((BitmapDrawable) pawnDrawable).getBitmap();

            int pawnSize = (int) (cellSize * 0.8);
            Bitmap resizedPawnBitmap = Bitmap.createScaledBitmap(pawnBitmap, pawnSize, pawnSize, true);

            int pawnX = x + (cellSize - pawnSize) / 2;
            int pawnY = y + (cellSize - pawnSize) / 2;

            canvas.drawBitmap(resizedPawnBitmap, pawnX, pawnY, null);
        }

        // Check if animation is still in progress
        if (currentPawn != null && targetPawn != null) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - animationStartTime;
            float t = Math.min(1f, (float) elapsedTime / animationDuration);

            if (t < 1f) {
                invalidate();
            } else {
                currentPawn.location = targetPawn.location;
                targetPawn = null;
            }
        }
    }

    public void movePawnTo(int position) {
        if (position >= 1 && position <= 225) {
            targetPawn = new SorryPawn(currentPawn);
            targetPawn.location = position;
            animationStartTime = System.currentTimeMillis();
            invalidate();
        }
    }

    public void setMargin(int margin) {
        this.margin = margin;
        updateOutlineRect();
        invalidate();
    }

    public void moveClockwise(int numSpaces) {
        if (currentPawn != null) {
            int currentLocation = currentPawn.location;
            int newLocation = currentLocation;

            for (int i = 0; i < numSpaces; i++) {
                if (currentLocation == 2) {
                    newLocation = 3;
                    if (currentPawn.color == Color.RED) {
                        youLost = true;
                    }
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
                    if (currentPawn.color == Color.BLUE) {
                        youWon = true;
                    }
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
                    if (currentPawn.color == Color.YELLOW) {
                        youLost = true;
                    }
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
                    if (currentPawn.color == Color.GREEN) {
                        youLost = true;
                    }
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
            movePawnTo(currentLocation);
        }
    }
}

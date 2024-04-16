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
    private int currentPlayerIndex = 0;
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
                {58, 73, 88, 74},     // Blue
                {20, 34, 35, 36},     // Red
                {191, 192, 206, 190}, // Yellow
                {138, 153, 168, 152}  // Green
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

            // Check if the pawn is in its start box
            if ((currentPawn.color == Color.BLUE && isInStartBox(currentLocation, Color.BLUE)) ||
                    (currentPawn.color == Color.RED && isInStartBox(currentLocation, Color.RED)) ||
                    (currentPawn.color == Color.YELLOW && isInStartBox(currentLocation, Color.YELLOW)) ||
                    (currentPawn.color == Color.GREEN && isInStartBox(currentLocation, Color.GREEN))) {
                if (numSpaces == 1 || numSpaces == 2) {
                    // Move the pawn to its start position
                    newLocation = getStartPosition(currentPawn.color);
                    currentLocation = newLocation;
                } else {
                    // Pawn cannot move from the start box with the given number of spaces
                    return;
                }
            }

            // Move the pawn clockwise
            for (int i = 0; i < numSpaces; i++) {
                if (currentLocation == 2) {
                    newLocation = 3;
                    if (currentPawn.color == Color.RED) {
                        newLocation = 18; // Move to the first safe zone for Red
                        break;
                    }
                } else if (currentLocation >= 3 && currentLocation <= 6) {
                    newLocation = currentLocation + 1; // Continue on the Red slide
                } else if (currentLocation >= 10 && currentLocation <= 13) {
                    newLocation = currentLocation + 1; // Continue on the Red slide
                } else if (currentLocation == 15) {
                    newLocation = 30;
                } else if (currentLocation == 30) {
                    newLocation = 45;
                    if (currentPawn.color == Color.BLUE) {
                        newLocation = 44; // Move to the first safe zone for Blue
                        break;
                    }
                } else if (currentLocation == 45 || currentLocation == 60 || currentLocation == 75 || currentLocation == 90) {
                    newLocation = currentLocation + 15; // Continue on the Blue slide
                } else if (currentLocation == 150 || currentLocation == 165 || currentLocation == 180 || currentLocation == 195) {
                    newLocation = currentLocation + 15; // Continue on the Blue slide
                } else if (currentLocation == 225) {
                    newLocation = 224;
                } else if (currentLocation == 224) {
                    newLocation = 223;
                    if (currentPawn.color == Color.YELLOW) {
                        newLocation = 208; // Move to the first safe zone for Yellow
                        break;
                    }
                } else if (currentLocation >= 220 && currentLocation <= 223) {
                    newLocation = currentLocation - 1; // Continue on the Yellow slide
                } else if (currentLocation >= 213 && currentLocation <= 216) {
                    newLocation = currentLocation - 1; // Continue on the Yellow slide
                } else if (currentLocation == 211) {
                    newLocation = 196;
                } else if (currentLocation == 196) {
                    newLocation = 181;
                    if (currentPawn.color == Color.GREEN) {
                        newLocation = 182; // Move to the first safe zone for Green
                        break;
                    }
                } else if (currentLocation == 181 || currentLocation == 166 || currentLocation == 151 || currentLocation == 136) {
                    newLocation = currentLocation - 15; // Continue on the Green slide
                } else if (currentLocation == 76 || currentLocation == 61 || currentLocation == 46 || currentLocation == 31) {
                    newLocation = currentLocation - 15; // Continue on the Green slide
                } else {
                    // Move to the next position on the board
                    if (currentLocation >= 1 && currentLocation <= 15) {
                        newLocation = (currentLocation % 15) + 1;
                    } else if (currentLocation >= 16 && currentLocation <= 30) {
                        newLocation = currentLocation + 15;
                    } else if (currentLocation >= 211 && currentLocation <= 225) {
                        newLocation = (currentLocation % 15) - 1;
                        if (newLocation == 0) {
                            newLocation = 15;
                        }
                    } else if (currentLocation >= 196 && currentLocation <= 210) {
                        newLocation = currentLocation - 15;
                    }
                }

                // Check if the pawn has reached its home
                if (isHome(newLocation, currentPawn.color)) {
                    currentPawn.location = newLocation;
                    break;
                }

                currentLocation = newLocation;
            }

            movePawnTo(currentLocation);
        }
    }

    private boolean isInStartBox(int location, int color) {
        int[][] startBoxes = {
                {58, 73, 88, 74},     // Blue
                {20, 34, 35, 36},     // Red
                {191, 192, 206, 190}, // Yellow
                {138, 153, 168, 152}  // Green
        };

        int colorIndex = getColorIndex(color);
        for (int startLocation : startBoxes[colorIndex]) {
            if (location == startLocation) {
                return true;
            }
        }
        return false;
    }

    private int getStartPosition(int color) {
        int[] startPositions = {75, 5, 221, 151};
        int colorIndex = getColorIndex(color);
        return startPositions[colorIndex];
    }

    private boolean isHome(int location, int color) {
        int[][] homePositions = {
                {107, 108, 109, 123}, // Red
                {23, 38, 53, 37},     // Blue
                {118, 119, 103, 104}, // Yellow
                {173, 188, 203, 189}  // Green
        };

        int colorIndex = getColorIndex(color);
        for (int homeLocation : homePositions[colorIndex]) {
            if (location == homeLocation) {
                return true;
            }
        }
        return false;
    }

    private int getColorIndex(int color) {
        switch (color) {
            case Color.BLUE:
                return 0;
            case Color.RED:
                return 1;
            case Color.YELLOW:
                return 2;
            case Color.GREEN:
                return 3;
            default:
                return -1;
        }
    }

    public void setCurrentPlayer(int playerIndex) {
        currentPlayerIndex = playerIndex;
        currentPawn = pawns.get(playerIndex * 4); // Assumes 4 pawns per player
    }

    public void selectPawn(int pawnIndex) {
        currentPawn = pawns.get(currentPlayerIndex * 4 + pawnIndex);
    }}

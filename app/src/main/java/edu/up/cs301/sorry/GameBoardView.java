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
import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.up.cs301.sorry.R;
import edu.up.cs301.sorry.SorryPawn;

public class GameBoardView extends View {
    private Paint gridPaint;
    private Paint textPaint;
    private Paint highlightPaint;
    public int cellSize;
    private int boardSize;
    private Bitmap boardImage;
    private int margin;
    private RectF outlineRect;
    public ArrayList<SorryPawn> pawns;
    public SorryPawn currentPawn;
    public SorryPawn targetPawn;
    public boolean youWon = false;
    public boolean youLost = false;
    private long animationStartTime;
    private static final long animationDuration = 500; // Animation duration in milliseconds
    private int currentPlayerIndex = 0;
    private Map<Integer, Integer> mainPathMap;
    private Map<String, TeamConfiguration> teams;

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

       initializePawns();
    }


    private void initializeTeam() {
        for (Map.Entry<String, TeamConfiguration> entry : teams.entrySet()) {
            String color = entry.getKey();
            TeamConfiguration teamConfig = entry.getValue();

            for (int location : teamConfig.getStartBox()) {
                SorryPawn pawn = new SorryPawn(getColorFromString(color), getDrawableIdFromColor(color));
                pawn.location = location;
                pawns.add(pawn);
            }
        }
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

    private int getColorFromString(String colorString) {
        switch (colorString) {
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "yellow":
                return Color.YELLOW;
            case "green":
                return Color.GREEN;
            default:
                return Color.BLACK;
        }
    }

    private int getDrawableIdFromColor(String colorString) {
        switch (colorString) {
            case "red":
                return R.drawable.red_pawn;
            case "blue":
                return R.drawable.blue_pawn;
            case "yellow":
                return R.drawable.yellow_pawn;
            case "green":
                return R.drawable.green_pawn;
            default:
                return 0;
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
    }

    public void movePawnTo(int position) {
        if (position >= 1 && position <= 225) {
            currentPawn.location = position;
            invalidate();
        }
    }
}

class TeamConfiguration {
    private int[] startBox;
    private int startPos;
    private int safeEntry;
    private int[] safeZone;
    private int[] home;

    public TeamConfiguration(int[] startBox, int startPos, int safeEntry, int[] safeZone, int[] home) {
        this.startBox = startBox;
        this.startPos = startPos;
        this.safeEntry = safeEntry;
        this.safeZone = safeZone;
        this.home = home;
    }

    public int[] getStartBox() {
        return startBox;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getSafeEntry() {
        return safeEntry;
    }

    public int[] getSafeZone() {
        return safeZone;
    }

    public int[] getHome() {
        return home;
    }
}
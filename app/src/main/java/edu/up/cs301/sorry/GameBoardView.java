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

        // Initialize main path mapping
        mainPathMap = new HashMap<>();
        mainPathMap.put(2, 3);
        mainPathMap.put(3, 4);
        mainPathMap.put(4, 5);
        mainPathMap.put(5, 6);
        mainPathMap.put(6, 7);
        mainPathMap.put(7, 8);
        mainPathMap.put(8, 9);
        mainPathMap.put(9, 10);
        mainPathMap.put(10, 11);
        mainPathMap.put(11, 12);
        mainPathMap.put(12, 13);
        mainPathMap.put(13, 14);
        mainPathMap.put(14, 15);
        mainPathMap.put(15, 30);
        mainPathMap.put(30, 45);
        mainPathMap.put(45, 60);
        mainPathMap.put(60, 75);
        mainPathMap.put(75, 90);
        mainPathMap.put(90, 105);
        mainPathMap.put(105, 120);
        mainPathMap.put(120, 135);
        mainPathMap.put(135, 150);
        mainPathMap.put(150, 165);
        mainPathMap.put(165, 180);
        mainPathMap.put(180, 195);
        mainPathMap.put(195, 210);
        mainPathMap.put(210, 225);
        mainPathMap.put(225, 224);
        mainPathMap.put(224, 223);
        mainPathMap.put(223, 222);
        mainPathMap.put(222, 221);
        mainPathMap.put(221, 220);
        mainPathMap.put(220, 219);
        mainPathMap.put(219, 218);
        mainPathMap.put(218, 217);
        mainPathMap.put(217, 216);
        mainPathMap.put(216, 215);
        mainPathMap.put(215, 214);
        mainPathMap.put(214, 213);
        mainPathMap.put(213, 212);
        mainPathMap.put(212, 211);
        mainPathMap.put(211, 196);
        mainPathMap.put(196, 181);
        mainPathMap.put(181, 166);
        mainPathMap.put(166, 151);
        mainPathMap.put(151, 136);
        mainPathMap.put(136, 121);
        mainPathMap.put(121, 106);
        mainPathMap.put(106, 91);
        mainPathMap.put(91, 76);
        mainPathMap.put(76, 61);
        mainPathMap.put(61, 46);
        mainPathMap.put(46, 31);
        mainPathMap.put(31, 16);
        mainPathMap.put(16, 1);
        mainPathMap.put(1, 2);

        // Initialize team configurations
        teams = new HashMap<>();
        teams.put("red", new TeamConfiguration(
                new int[]{20, 34, 35, 36}, 5, 3,
                new int[]{18, 48, 63, 78, 93}, new int[]{107, 108, 109, 123}));
        teams.put("blue", new TeamConfiguration(
                new int[]{58, 73, 88, 74}, 75, 45,
                new int[]{44, 42, 41, 40, 39}, new int[]{23, 38, 53, 37}));
        teams.put("yellow", new TeamConfiguration(
                new int[]{190, 191, 192, 206}, 221, 223,
                new int[]{208, 178, 163, 148, 133}, new int[]{118, 119, 103, 104}));
        teams.put("green", new TeamConfiguration(
                new int[]{138, 153, 168, 152}, 151, 181,
                new int[]{182, 184, 185, 186, 187}, new int[]{173, 188, 203, 189}));

        // Initialize pawns in starting position
        initializePawns();
    }

    private void initializePawns() {
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

            Log.d("SorryGame", "Drawing pawn at location: " + pawnLocation);
            Log.d("SorryGame", "Pawn row: " + pawnRow + ", Pawn col: " + pawnCol);

            // Highlight the current pawn's location
            if (pawn == currentPawn) {
                float highlightX = margin + pawnCol * cellSize;
                float highlightY = margin + pawnRow * cellSize;
                canvas.drawRect(highlightX, highlightY, highlightX + cellSize, highlightY + cellSize, highlightPaint);
            }

            // Draw the pawn
            int x = margin + pawnCol * cellSize;
            int y = margin + pawnRow * cellSize;

            Log.d("SorryGame", "Pawn position - x: " + x + ", y: " + y);

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

    public void moveClockwise(int numSpaces) {
        int newLocation = currentPawn.location;
        String currentTeamColor = getTeamColorFromPawn(currentPawn);
        TeamConfiguration currentTeamConfig = teams.get(currentTeamColor);

        Log.d("SorryGame", "Current pawn location: " + currentPawn.location);
        Log.d("SorryGame", "Current team color: " + currentTeamColor);

        for (int i = 0; i < numSpaces; i++) {
            if (currentPawn.isInStart) {
                // Move from start box to start position
                newLocation = currentTeamConfig.getStartPos();
                currentPawn.isInStart = false;
                Log.d("SorryGame", "Moved from start box to start position: " + newLocation);
            } else if (mainPathMap.containsKey(newLocation)) {
                // Move along the main path
                newLocation = mainPathMap.get(newLocation);
                Log.d("SorryGame", "Moved along main path to: " + newLocation);
            } else if (newLocation == currentTeamConfig.getSafeEntry()) {
                // Enter the safe zone
                newLocation = currentTeamConfig.getSafeZone()[0];
                Log.d("SorryGame", "Entered safe zone at: " + newLocation);
            } else {
                int[] safeZone = currentTeamConfig.getSafeZone();
                int safeZoneIndex = Arrays.asList(safeZone).indexOf(newLocation);

                if (safeZoneIndex != -1) {
                    // Move within the safe zone
                    if (safeZoneIndex < safeZone.length - 1) {
                        newLocation = safeZone[safeZoneIndex + 1];
                        Log.d("SorryGame", "Moved within safe zone to: " + newLocation);
                    } else {
                        // Move to a random spot in the home position
                        int[] home = currentTeamConfig.getHome();
                        int randomHomeIndex = new Random().nextInt(home.length);
                        newLocation = home[randomHomeIndex];
                        currentPawn.isHome = true;
                        Log.d("SorryGame", "Moved to home position: " + newLocation);
                    }
                }
            }
        }
        Log.d("SorryGame", "Final pawn location: " + newLocation);
        movePawnTo(newLocation);
    }



    private String getTeamColorFromPawn(SorryPawn pawn) {
        int pawnColor = pawn.color;
        if (pawnColor == Color.RED) {
            return "red";
        } else if (pawnColor == Color.BLUE) {
            return "blue";
        } else if (pawnColor == Color.YELLOW) {
            return "yellow";
        } else if (pawnColor == Color.GREEN) {
            return "green";
        }
        return "";
    }

    public void setCurrentPlayer(int playerIndex) {
        currentPlayerIndex = playerIndex;
        currentPawn = pawns.get(playerIndex * 4); // Assumes 4 pawns per player
    }

    public void selectPawn(int pawnIndex) {
        currentPawn = pawns.get(currentPlayerIndex * 4 + pawnIndex);
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
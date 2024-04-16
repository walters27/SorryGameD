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
    private List<SorryPawn> pawns;

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
        pawns = new ArrayList<>();

        initializePawns();
    }

    private void initializePawns() {
        int[][] locations = {
                {58, 73, 88, 72},  // Blue
                {20, 34, 35, 36},   // Red
                {176, 191, 206, 192}, // Yellow
                {138, 153, 168, 152} // Green
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
            int col = (pawn.location - 1) % 15;
            int row = (pawn.location - 1) / 15;
            float pawnX = margin + col * cellSize + cellSize / 2;
            float pawnY = margin + row * cellSize + cellSize / 2;

            Drawable pawnDrawable = getResources().getDrawable(pawn.getImageResourceId());
            Bitmap pawnBitmap = ((BitmapDrawable) pawnDrawable).getBitmap();
            int pawnSize = (int) (cellSize * 0.8);
            Bitmap resizedPawnBitmap = Bitmap.createScaledBitmap(pawnBitmap, pawnSize, pawnSize, true);
            canvas.drawBitmap(resizedPawnBitmap, pawnX - pawnSize / 2, pawnY - pawnSize / 2, null);
        }
    }

    public void movePawnTo(int pawnIndex, int newPosition) {
        if (newPosition >= 1 && newPosition <= 225) {
            SorryPawn pawn = pawns.get(pawnIndex);
            pawn.location = newPosition;
            invalidate();
        }
    }

    public void setMargin(int margin) {
        this.margin = margin;
        updateOutlineRect();
        invalidate();
    }
}
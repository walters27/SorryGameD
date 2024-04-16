package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.infoMessage.GameState;

/**
 * This contains the state for the Sorry game.
 *
 * @author Steven R. Vegdahl, Quince Pham, Kira Kunitake, Annalise Walters, Corwin Carr
 * @version July 2013
 */
public class SorryState extends GameState {
	// to satisfy Serializable interface
	private static final long serialVersionUID = 7737393762469851826L;

	private int playerTurn;
	private int[] pawnHomeCount;
	private int[] pawnStartCount;
	private SorryPawn[] pawns;
	private int cardNumber;
	private int playerId;
	private boolean cardDrawn;
	private int movesToWin;
	private int currentPawnIndex;

	// constructor to initialize game state
	public SorryState() {
		super();
		playerTurn = 1;
		pawnStartCount = new int[]{4, 4, 4, 4};
		pawnHomeCount = new int[]{0, 0, 0, 0};
		pawns = new SorryPawn[16];
		// initialize pawns with colors and image
		for (int i = 0; i < 16; i++) {
			int color = i / 4;
			int imageResourceId = getImageResourceId(color);
			pawns[i] = new SorryPawn(color, imageResourceId);
		}
		cardNumber = 1;
		cardDrawn = false;
		movesToWin = 61;
		currentPawnIndex = -1;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int s) {
		playerId = s;
	}

	public boolean getCardDrawn() {
		return cardDrawn;
	}

	public void setCardDrawn(boolean state) {
		cardDrawn = state;
	}

	public int getPawnHomeCount(int color) {
		return pawnHomeCount[color];
	}

	public void setPawnHomeCount(int color, int num) {
		pawnHomeCount[color] = num;
	}

	public int getPawnStartCount(int color) {
		return pawnStartCount[color];
	}

	public void setPawnStartCount(int color, int num) {
		pawnStartCount[color] = num;
	}

	public SorryPawn getPawn(int i) {
		return pawns[i];
	}

	public int getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getCurrentPawnIndex() {
		return currentPawnIndex;
	}

	public void setCurrentPawnIndex(int index) {
		currentPawnIndex = index;
	}

	public SorryState(SorryState orig) {
		super();
		if (orig != null) {
			this.playerTurn = orig.playerTurn;
			this.pawnStartCount = orig.pawnStartCount.clone();
			this.pawnHomeCount = orig.pawnHomeCount.clone();
			this.pawns = new SorryPawn[16];
			for (int i = 0; i < 16; i++) {
				this.pawns[i] = new SorryPawn(orig.pawns[i]);
			}
			this.cardNumber = orig.cardNumber;
			this.cardDrawn = orig.cardDrawn;
			this.playerId = orig.playerId;
			this.movesToWin = orig.movesToWin;
			this.currentPawnIndex = orig.currentPawnIndex;
		}
	}

	// matches pawn color to a corresponding pawn image
	private int getImageResourceId(int color) {
		switch (color) {
			case 0:
				return R.drawable.blue_pawn;
			case 1:
				return R.drawable.red_pawn;
			case 2:
				return R.drawable.green_pawn;
			case 3:
				return R.drawable.yellow_pawn;
			default:
				return 0;
		}
	}

	// gets the color of the pawn based on color index
	private String getColorName(int color) {
		switch (color) {
			case 0:
				return "Blue";
			case 1:
				return "Red";
			case 2:
				return "Green";
			case 3:
				return "Yellow";
			default:
				return "";
		}
	}
}
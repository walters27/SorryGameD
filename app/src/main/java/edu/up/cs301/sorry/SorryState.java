package edu.up.cs301.sorry;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
	private ArrayList <SorryPawn> pawns;
	private int cardNumber;  //this currently face up card
	private int playerId;
	private boolean cardDrawn;
	private int movesToWin;
	private int currentPawnIndex;
	public SorryPawn currentPawn;
	private Map<Integer, Integer> mainPathMap;
	private Map<String, TeamConfiguration> teams;
	private int currentPlayerIndex = 0;

	private Random rand = new Random();


	// constructor to initialize game state
	public SorryState() {
		super();
		playerTurn = 1;
		pawnStartCount = new int[]{4, 4, 4, 4};
		pawnHomeCount = new int[]{0, 0, 0, 0};
		pawns = new ArrayList<SorryPawn>();

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

		cardNumber = 1;
		cardDrawn = false;
		movesToWin = 61;
		currentPawnIndex = 0;

		teams  = new HashMap<>();
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
		return pawns.get(i);
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
			this.pawns = new ArrayList<SorryPawn>();
			for (int i = 0; i < 16; i++) {
				this.pawns.add(new SorryPawn(orig.pawns.get(i)));
			}
			this.cardNumber = orig.cardNumber;
			this.cardDrawn = orig.cardDrawn;
			this.playerId = orig.playerId;
			this.movesToWin = orig.movesToWin;
			this.currentPawnIndex = orig.currentPawnIndex;
			this.teams = orig.teams;
			this.mainPathMap = orig.mainPathMap;
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

	/** Handler for the SorryDrawCard action.
	 *
	 * Caveat: This code assumes it is the associated player's turn
	 */

	public void moveClockwise(int numSpaces) {
		if(teams == null){
			Log.e("SorryGame","Teams map is null");
			return;
		}
		int newLocation = currentPawn.location;
		String currentTeamColor = getTeamColorFromPawn(currentPawn);
		TeamConfiguration currentTeamConfig = teams.get(currentTeamColor);

		Log.d("SorryGame", "Current pawn location: " + currentPawn.location);
		Log.d("SorryGame", "Current team color: " + currentTeamColor);

		boolean enteredSafeZone = false;

		for (int i = 0; i < numSpaces; i++) {
			if (currentPawn.isInStart) {
				// Move from start box to start position
				newLocation = currentTeamConfig.getStartPos();
				currentPawn.isInStart = false;
				Log.d("SorryGame", "Moved from start box to start position: " + newLocation);
			} else if (mainPathMap.containsKey(newLocation) && !enteredSafeZone) {
				// Move along the main path
				int nextLocation = mainPathMap.get(newLocation);
				if (nextLocation == currentTeamConfig.getSafeEntry()) {
					// Pawn will pass over the safe entry, switch to safe zone path
					newLocation = currentTeamConfig.getSafeZone()[0];
					enteredSafeZone = true;
					Log.d("SorryGame", "Entered safe zone at: " + newLocation);
				} else {
					newLocation = nextLocation;
					Log.d("SorryGame", "Moved along main path to: " + newLocation);
				}
			} else {
				int[] safeZone = currentTeamConfig.getSafeZone();
				int safeZoneIndex = Arrays.asList(safeZone).indexOf(newLocation);

				if (safeZoneIndex != -1) {
					// Move within the safe zone
					if (safeZoneIndex < safeZone.length - 1) {
						newLocation = safeZone[safeZoneIndex + 1];
						Log.d("SorryGame", "Moved within safe zone to: " + newLocation);
					} else {
						// Move to a random unoccupied spot in the home position
						int[] home = currentTeamConfig.getHome();
						List<Integer> unoccupiedHomeSpots = new ArrayList<>();
						for (int homeSpot : home) {
							boolean isOccupied = false;
							for (SorryPawn pawn : pawns) {
								if (pawn.location == homeSpot) {
									isOccupied = true;
									break;
								}
							}
							if (!isOccupied) {
								unoccupiedHomeSpots.add(homeSpot);
							}
						}
						if (!unoccupiedHomeSpots.isEmpty()) {
							int randomIndex = new Random().nextInt(unoccupiedHomeSpots.size());
							newLocation = unoccupiedHomeSpots.get(randomIndex);
							currentPawn.isHome = true;
							Log.d("SorryGame", "Moved to unoccupied home position: " + newLocation);
						} else {
							Log.d("SorryGame", "All home positions are occupied. Pawn stays in the safe zone.");
						}
						break; // Stop moving further, as the pawn has reached the end of the safe zone
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
	public void movePawnTo(int position) {
		if (position >= 1 && position <= 225) {
			currentPawn.location = position;
			//GameBoardView.invalidate();
		}
	}

	public void selectPawn(int pawnIndex) {
		currentPawn = pawns.get(currentPlayerIndex * 4 + pawnIndex);
	}

	public void setCurrentPlayer(int playerIndex) {
		currentPlayerIndex = playerIndex;
		currentPawn = pawns.get(playerIndex * 4); // Assumes 4 pawns per player
	}
	public void drawCard(SorryDrawCard sdc) {
		this.cardNumber = rand.nextInt(11) + 1;
	}
}


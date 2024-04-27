package edu.up.cs301.sorry;

import android.graphics.Color;
import android.util.Log;

import java.io.Serializable;
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
public class SorryState extends GameState implements Serializable {
	// to satisfy Serializable interface
	private static final long serialVersionUID = 7737393762469851826L;
	private int playerTurn;
	private int[] pawnHomeCount;
	private int[] pawnStartCount;
	private ArrayList <SorryPawn> pawns;
	private int cardNumber;
	private int playerId;
	private boolean cardDrawn;
	private int movesToWin;
	private int currentPawnIndex;
	public SorryPawn currentPawn;
	public SorryPawn targetPawn;
	private Map<Integer, Integer> mainPathMap;
	private Map<String, TeamConfiguration> teams;
	private int currentPlayerIndex = 0;
	private Random rand = new Random();
	public Map<Integer, Integer> getMainPathMap() {
		return mainPathMap;
	}

	// constructor to initialize game state
	public SorryState() {
		super();
		playerTurn = 1;
		pawnStartCount = new int[]{4, 4, 4, 4};
		pawnHomeCount = new int[]{0, 0, 0, 0};
		pawns = new ArrayList<SorryPawn>();

		//start location for each pawn
		int[][] locations = {
				{58, 73, 88, 74},     // Blue
				{20, 34, 35, 36},     // Red
				{191, 192, 206, 190}, // Yellow
				{138, 153, 168, 152}  // Green
		};
		int[] colors = {Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN};
		int[] drawableIds = {R.drawable.blue_pawn, R.drawable.red_pawn, R.drawable.yellow_pawn, R.drawable.green_pawn};

		//create a pawn for each team and add it to the lsit
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

		//team configurations with start and home locations
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

		//main path map of the board
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

		// Red Safe Zone Path
		mainPathMap.put(18, 33);
		mainPathMap.put(33, 48);
		mainPathMap.put(48, 63);
		mainPathMap.put(63, 78);
		mainPathMap.put(78, 93);
		mainPathMap.put(93, 108);

		// Blue Safe Zone Path
		mainPathMap.put(44, 43);
		mainPathMap.put(43, 42);
		mainPathMap.put(42, 41);
		mainPathMap.put(41, 40);
		mainPathMap.put(40, 39);
		mainPathMap.put(39, 38);

		// Yellow Safe Zone Path
		mainPathMap.put(182, 183);
		mainPathMap.put(183, 184);
		mainPathMap.put(184, 185);
		mainPathMap.put(185, 186);
		mainPathMap.put(186, 187);
		mainPathMap.put(187, 188);


		// Green Safe Zone Path
		mainPathMap.put(208,193);
		mainPathMap.put(193,178);
		mainPathMap.put(178,163);
		mainPathMap.put(163,148);
		mainPathMap.put(148,133);
		mainPathMap.put(133, 118);

	}

	public int getPlayerId() {
		return playerId;
	}

	public ArrayList<SorryPawn> getPawns() {
		return pawns;
	}

	public void setCardDrawn(boolean state) {
		cardDrawn = state;
	}

	public int getPawnHomeCount(int color) {
		return pawnHomeCount[color];
	}

	public int getCardNumber() {
		return cardNumber;
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
			this.currentPawn = orig.currentPawn;
			this.teams = orig.teams;
			this.mainPathMap = orig.mainPathMap;
		}
	}

	/** Handler for the SorryDrawCard action.
	 *
	 * Caveat: This code assumes it is the associated player's turn
	 */

	public void moveClockwise(int numSpaces) {
		if (!cardDrawn) {return;}
		SorryPawn statepawn = null;
		//check if current player is allowed to move this pawn
		if(this.getPlayerId() != getTeamIdFromPawn(currentPawn)){
			return;
		}
		if(teams == null){
			return;
		}
		if (currentPawn != null){
			//Loop through pawn to find current pawn's index
			//Sorry card exception
			if (cardNumber == 13) {
				if (sendToStart(targetPawn) != -1)
				{
					int temploc = targetPawn.location;
					for (SorryPawn s : pawns) {
						if (s.location == temploc) {targetPawn = s;}
						if (s.location == currentPawn.location) {statepawn = s;}
					}
					targetPawn.location = sendToStart(targetPawn);
					statepawn.location = temploc;

					cardDrawn = false;
					this.playerId = ((this.playerId+1)%4);
					return;
				}
				else {return;}
			//Sorry card exception over
			}
			for (int i = 0; i < 16; i++)
			{
				if (pawns.get(i).location == currentPawn.location) {statepawn = pawns.get(i);}
			}

			int currLocation = currentPawn.location;
			String currentTeamColor = getTeamColorFromPawn(currentPawn);
			TeamConfiguration currentTeamConfig = teams.get(currentTeamColor);
			boolean enteredSafeZone = false;
			boolean instart = false;

			//Move the pawn
			for (int i = 0; i < numSpaces; i++) {
				//if pawn is in start
				if (currentPawn.isInStart) {
					if (cardNumber != 1 && cardNumber != 2) {return;}
					// Move from start box to start position
					currLocation = currentTeamConfig.getStartPos();
					currentPawn.isInStart = false;
					statepawn.isInStart = false;
					instart = true;
					//if pawn has not entered safe zone
				} else if (mainPathMap.containsKey(currLocation) && !enteredSafeZone) {
					// Move along the main path
					int nextLocation = mainPathMap.get(currLocation);
					//if pawn has entered safe zone
					if (nextLocation == currentTeamConfig.getSafeEntry()) {
						// Pawn will pass over the safe entry, switch to safe zone path
						currLocation = currentTeamConfig.getSafeZone()[0];
						currentPawn.location = currLocation;
						enteredSafeZone = true;
					} else {
						currLocation = nextLocation;
					}
				} else {
					//get safe zone for current team
					int[] safeZone = currentTeamConfig.getSafeZone();

					/**
					 * External Citation
					 * Date: 4/25/24
					 * Problem: safeZoneIndex remained -1 when iterating
					 * through the ArrayList so it was not entering the if
					 * statement so the pawns could not move in the safeZone.
					 * Resource: Professor Nux
					 * Solution: Changed ArrayList to Array to properly update
					 * safeZoneIndex.
					 */

					int safeZoneIndex = -1;
					for(int j = 0; j < safeZone.length; j++){
						//find index of current location in the safe zone
						if(safeZone[j] == currLocation){
							safeZoneIndex = j;
							break;
						}
					}
					if (safeZoneIndex != -1) {
						// Move within the safe zone
						if (safeZoneIndex < safeZone.length - 1) {
							//move to next spot in the safe zone
							currLocation = safeZone[safeZoneIndex + 1];
						}
					}
				}
			}
			//red home location
			if (currLocation == 108) {
				//mark pawn as home
				currentPawn.isHome = true;
				//list of red home positions
				ArrayList<Integer> location = new ArrayList<>();
				location.add(107);
				location.add(108);
				location.add(109);
				location.add(123);
				//remove location from ArrayList if it is occupied
			for (SorryPawn s : pawns) {
				if (location.contains(s.location))
				{
					location.remove(location.indexOf(s.location));
				}
			}
			//move pawn to first available location
			 if (location.size() > 0) {currLocation =location.get(0);}
			}
			//blue home location
			if (currLocation == 38) {
				//mark pawn as home
				currentPawn.isHome = true;
				//list of blue home positions
				ArrayList<Integer> location = new ArrayList<>();
				location.add(23);
				location.add(53);
				location.add(37);
				location.add(38);
				//remove location from ArrayList if it is occupied
				for (SorryPawn s : pawns) {
					if (location.contains(s.location))
					{
						location.remove(location.indexOf(s.location));
					}

			}
			if (location.size() > 0)	{currLocation =location.get(0);}}
			//green home location
			if (currLocation == 188) {
				//list of green home positions
				ArrayList<Integer> location = new ArrayList<>();
				location.add(188);
				location.add(189);
				location.add(173);
				location.add(174);
				//remove location from ArrayList if it is occupied
				for (SorryPawn s : pawns) {
					if (location.contains(s.location))
					{
						location.remove(location.indexOf(s.location));
					}} if (location.size() > 0) {currLocation =location.get(0);}
			}
			//yellow home location
			if (currLocation == 118) {
				//list of yellow home positions
				ArrayList<Integer> location = new ArrayList<>();
				location.add(118);
				location.add(119);
				location.add(117);
				location.add(103);
				//remove location from ArrayList if it is occupied
				for (SorryPawn s : pawns) {
					if (location.contains(s.location))
					{
						location.remove(location.indexOf(s.location));
					}} if (location.size() > 0) currLocation =location.get(0);
			}
			//move pawn to new location

			//movePawnTo(currLocation);

			//update state pawn's location if it exists
			boolean cluttered = false;
			for (SorryPawn s : pawns) {if (s.location == currLocation) {cluttered = true; break;}}
			if (statepawn != null) {
				if ((!cluttered) || currentPawn.isHome){
				statepawn.location = currLocation;
			}	else {Log.d("", "someone was cluttered"); statepawn.isInStart = instart; return;}
			}
			//switch to next player's turn
			cardDrawn = false;



			//SLIDER TIME
			ArrayList<Integer> sliders = new ArrayList<>();
			//slider locations
			sliders.add(2); sliders.add(10); sliders.add(30); sliders.add(150);
			sliders.add(216); sliders.add(224); sliders.add(76); sliders.add(196);
			ArrayList<Integer> byebye = new ArrayList<>();
			//determine slide based on current location of pawn
			switch(statepawn.location) {
				case 2: byebye.add(3); byebye.add(4); byebye.add(5);
				case 10: byebye.add(11); byebye.add(12); byebye.add(13);
				case 30: byebye.add(45); byebye.add(60); byebye.add(75);
				case 150: byebye.add(165); byebye.add(180); byebye.add(195);
				case 216: byebye.add(215); byebye.add(214); byebye.add(213);
				case 224: byebye.add(223); byebye.add(222); byebye.add(221);
				case 76: byebye.add(31); byebye.add(46); byebye.add(61);
				case 196: byebye.add(151); byebye.add(166); byebye.add(181);
				default: break;
			}
			if (sliders.contains(statepawn.location)) {
				for (SorryPawn s : pawns) {
					//if location of pawn is in byebye, send it back to start
					if (byebye.contains(s.location)) {s.location = sendToStart(s);}
				}
				//move pawn forward (slide)
				for (int i = 0; i < 3; i++) {
					int nextLocation = mainPathMap.get(statepawn.location);
					statepawn.location = nextLocation;
				}
			}
			this.playerId = ((this.playerId+1)%4);
		}
	}

	/**
	 *returns team corresponding to the color of the pawn
	 */
	private String getTeamColorFromPawn(SorryPawn pawn) {
		int pawnColor = pawn.color;
		//determine team based on pawn's color
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

	/**
	 * get team id of corresponding pawn
	 */
	public int getTeamIdFromPawn(SorryPawn pawn) {
		if(pawn != null){
			//get color of pawn
			int pawnColor = pawn.color;
			//determine id based on pawn's color
			if (pawnColor == Color.RED) {
				return  0;
			} else if (pawnColor == Color.BLUE) {
				return 1;
			} else if (pawnColor == Color.YELLOW) {
				return 2;
			} else if (pawnColor == Color.GREEN) {
				return 3;
			}
		}
		return -1;
	}
	public void drawCard(SorryDrawCard sdc) {
		if (cardDrawn) {return;}
		//array of card numbers
		int[] validCardNumbers = {1, 2, 3, 4, 5, 8, 10, 11, 12, 13};
		//generate random index to set as card
		int randomIndex = rand.nextInt(validCardNumbers.length);
		this.cardNumber = validCardNumbers[randomIndex];
		cardDrawn = true;
	}

	/**
	 * retrieve pawn belonging to specific player
	 */
	public SorryPawn[] getPlayerPawns(int playerId){
		ArrayList <SorryPawn> playerPawns = new ArrayList<>();
		//go through all pawns in game state
		for (SorryPawn pawn:pawns){
			int pawnTeamId = getTeamIdFromPawn(pawn);
			//if pawn belongs to player add to list
			if(pawnTeamId==playerId){
				playerPawns.add(pawn);
			}
		}
		//convert array list to array
		return playerPawns.toArray(new SorryPawn[playerPawns.size()]);
	}

	public void moveNextTurn() {
		//changes players turn
		this.playerId = ((this.playerId+1)%4);
		//reset card
		cardDrawn = false;
	}

	/**
	 * sorry card. sends pawn back to home if selected by enemy.
	 */
	public int sendToStart(SorryPawn p) {
		if (p == null) {return -1;}
		//get team id of pawn
		int team = getTeamIdFromPawn(p);
		ArrayList<Integer> startSpaces = new ArrayList<>();
		//populate start spaces based on pawn's team
		switch(team)
		{
			case 1: startSpaces.add(58); startSpaces.add(73); startSpaces.add(88); startSpaces.add(74);
				break;
			case 2: startSpaces.add(206); startSpaces.add(190); startSpaces.add(191); startSpaces.add(192);
				break;
			case 3: startSpaces.add(138); startSpaces.add(168); startSpaces.add(152); startSpaces.add(153);
				break;
			case 0: startSpaces.add(20); startSpaces.add(35); startSpaces.add(34); startSpaces.add(36);
				break;
			default:
				break;
		}
		//if pawn is not in start or home
		if (!p.isInStart && !p.isHome)
		{
			// still need to check safe spaces
			TeamConfiguration pawnteam = teams.get(getTeamColorFromPawn(p));
			int[] exceptions = pawnteam.getSafeZone();
			for (int i = 0; i < exceptions.length; i++)
			{
				if (p.location == exceptions[i]) {return -1;}
			}
			//remove pawns occupying start spaces
			for (SorryPawn pa : pawns)
			{
				if (startSpaces.contains(pa.location))
				{startSpaces.remove(startSpaces.indexOf(pa.location));}
			}
			if (startSpaces.size() > 0) {
			return startSpaces.get(0);}
		}
		return -1;
	}
}



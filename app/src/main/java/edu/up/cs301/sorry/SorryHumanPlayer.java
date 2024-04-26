package edu.up.cs301.sorry;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

import edu.up.cs301.GameFramework.actionMessage.MyNameIsAction;
import edu.up.cs301.GameFramework.infoMessage.GameState;
import edu.up.cs301.GameFramework.players.GameHumanPlayer;
import edu.up.cs301.GameFramework.GameMainActivity;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.infoMessage.GameInfo;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SorryHumanPlayer extends GameHumanPlayer implements OnClickListener, View.OnTouchListener {
	private SorryState state;

	public MediaPlayer mediaPlayer;
	// EXTERNAL CITATION
	// Tramanh Best - Boolean Baddies
	// Problem: Needed Music
	// Solution: take inspiration from the boolean baddies
	private GameMainActivity myActivity;
	private ImageView imageViewCard;
	private Button buttonMoveClockwise;
	private GameBoardView gameBoardView;
	private Handler handler = new Handler();
	private SorryPawn selectedPawn;

	public SorryHumanPlayer(String name) {
		super(name);
	}

	public View getTopView() {
		return myActivity.findViewById(R.id.sorry_gui_layout);
	}

	protected void updateDisplay() {

		updateCurrentCard();

		gameBoardView.pawns = state.getPawns();
		gameBoardView.invalidate();

		//TODO: update message box (winner?)
	}

	/** Displays the currently face up card for the user */
	private void updateCurrentCard() {
		// generates/draws a random card number
		int cardNum = state.getCardNumber();
		int drawFace = 0;

		// displays card depending on what number card was drawn
		switch (cardNum) {
			case 1:
				drawFace = R.drawable.sorrycardone;
				//state.setCardNumber(1);
				handleOneCard();
				break;
			case 2:
				drawFace = R.drawable.sorrycardtwo;
				//state.setCardNumber(2);
				handleTwoCard();
				break;
			case 3:
				drawFace = R.drawable.sorrycardthree;
				handleThreeCard();
				//state.setCardNumber(3);
				break;
			case 4:
				drawFace = R.drawable.sorrycardfour;
				handleFourCard();
				//state.setCardNumber(4);
				break;
			case 5:
				drawFace = R.drawable.sorrycardfive;
				handleFiveCard();
				//state.setCardNumber(5);
				break;
			case 6:
				drawFace = R.drawable.sorrycardseven;
				handleSevenCard();
				//state.setCardNumber(7);
				break;
			case 7:
				drawFace = R.drawable.sorrycardeight;
				handleEightCard();
				//state.setCardNumber(8);
				break;
			case 8:
				drawFace = R.drawable.sorrycardten;
				handleTenCard();
				//state.setCardNumber(10);
				break;
			case 9:
				drawFace = R.drawable.sorrycardeleven;
				handleElevenCard();
				//state.setCardNumber(11);
				break;
			case 10:
				drawFace = R.drawable.sorrycardtwelve;
				handleTwelveCard();
				//state.setCardNumber(12);
				break;
			case 11:
				drawFace = R.drawable.sorrycardsorry;
				handleSorryCard();
				//state.setCardNumber(13);
				break;
		}

		imageViewCard.setImageResource(drawFace);
		state.setCardDrawn(true);
	}

	// gets text box for displaying messages
	public TextView getTextBox() {
		return myActivity.findViewById(R.id.textViewMessages);
	}

	// sends a message to the text box
	public void sendTextMessage(TextView t, String m) {
		t.setText(""); // Clear the previous message
		t.append(m); // Add the new message
	}

	public void onClick(View button) {
		if (game == null) return;
			if (button.getId() == R.id.buttonDrawCards) {
				drawCard();
			} else if (button.getId() == R.id.buttonMoveClockwise) {
				state.setCardDrawn(false);
				MoveForwardAction move = new MoveForwardAction(this,selectedPawn);
				game.sendAction(move);
			}
	}

	private int getDistanceToHome(SorryPawn pawn) {
		int distance = 0;
		int currentLocation = pawn.location;

		// Calculate distance based on pawn color and current location
		if (pawn.color == RED) {
			if (currentLocation >= 1 && currentLocation <= 107) {
				distance = 107 - currentLocation;
			}
		} else if (pawn.color == BLUE) {
			if (currentLocation >= 16 && currentLocation <= 23) {
				distance = 23 - currentLocation;
			} else if (currentLocation >= 1 && currentLocation <= 15) {
				distance = (15 - currentLocation) + 8;
			}
		} else if (pawn.color == YELLOW) {
			if (currentLocation >= 211 && currentLocation <= 118) {
				distance = 118 - currentLocation;
			} else if (currentLocation >= 196 && currentLocation <= 210) {
				distance = (210 - currentLocation) + 104;
			}
		} else if (pawn.color == GREEN) {
			if (currentLocation >= 166 && currentLocation <= 173) {
				distance = 173 - currentLocation;
			} else if (currentLocation >= 151 && currentLocation <= 165) {
				distance = (165 - currentLocation) + 8;
			}
		}

		return distance;
	}

	public void drawCard() {
		SorryDrawCard act = new SorryDrawCard(this);
		game.sendAction(act);
	}

	// displays message to text box depending on which card was drawn
	private void handleOneCard() {
		sendTextMessage(getTextBox(), "Player " + state.getPlayerId() + " drew a 1 card. Move a pawn from Start or move a pawn one space forward.");
	}

	private void handleTwoCard() {
		sendTextMessage(getTextBox(), "Player " + state.getPlayerId() + " drew a 2 card. Move a pawn two spaces forward or move a pawn from Start and draw again.");
	}

	private void handleThreeCard() {
		sendTextMessage(getTextBox(), "Player " + state.getPlayerId() + " drew a 3 card. Move a pawn three spaces forward.");
	}

	private void handleFourCard() {
		sendTextMessage(getTextBox(), "Player " + state.getPlayerId() + " drew a 4 card. Move a pawn four spaces backward.");
	}

	private void handleFiveCard() {
		sendTextMessage(getTextBox(), "Player " + state.getPlayerId() + " drew a 5 card. Move a pawn five spaces forward.");
	}

	private void handleSevenCard() {
		sendTextMessage(getTextBox(), "Player " + state.getPlayerId() + " drew a 7 card. Move one pawn seven spaces forward or split the seven spaces between two pawns.");
	}

	private void handleEightCard() {
		sendTextMessage(getTextBox(), "Player " + state.getPlayerId() + " drew an 8 card. Move a pawn eight spaces forward.");
	}

	private void handleTenCard() {
		sendTextMessage(getTextBox(), "Player " + state.getPlayerId() + " drew a 10 card. Move a pawn ten spaces forward or move a pawn one space backward.");
	}

	private void handleElevenCard() {
		sendTextMessage(getTextBox(), "Player " + state.getPlayerId() + " drew an 11 card. Move eleven spaces forward or switch places with an opponent.");
	}

	private void handleTwelveCard() {
		sendTextMessage(getTextBox(), "Player " + state.getPlayerId() + " drew a 12 card. Move a pawn twelve spaces forward.");
	}

	private void handleSorryCard() {
		sendTextMessage(getTextBox(), "Player " + state.getPlayerId() + " drew a Sorry card. Take one pawn from Start and move it directly to a square occupied by any opponent's pawn, sending that pawn back to its own Start.");
	}

	// receives game info and updates SorryState
	@Override
	public void receiveInfo(GameInfo info) {
		if (!(info instanceof SorryState)) return;
		this.state = (SorryState) info;
		updateDisplay();
		gameBoardView.invalidate();
	}

	public void setAsGui(GameMainActivity activity) {
		this.myActivity = activity;

		activity.setContentView(R.layout.sorry_xml_multi_line);

		// initialize GUI elements
		imageViewCard = activity.findViewById(R.id.imageViewCard);
		Button buttonDrawCards = activity.findViewById(R.id.buttonDrawCards);
		buttonMoveClockwise = activity.findViewById(R.id.buttonMoveClockwise);
		gameBoardView = activity.findViewById(R.id.gameBoardView);

		// register click listeners
		buttonDrawCards.setOnClickListener(this);
		buttonMoveClockwise.setOnClickListener(this);
		gameBoardView.setOnTouchListener(this);

		//add music
		mediaPlayer = MediaPlayer.create(myActivity, R.raw.sorry);
		mediaPlayer.start();
		mediaPlayer.setLooping(true);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v instanceof GameBoardView)
		{
			SorryPawn selected = null;
			List<SorryPawn> sp = state.getPawns();
			// code citation DJhon Stackoverflow
			// get x and y location of click
			int transx = (int) event.getX();
			int transy = (int) event.getY();
			int ydiff =0;
			int xdiff =0;
			for (int i = 0; i < 16; i++)
			{
				int pawnrow = (sp.get(i).location - 1) / 15;
				int pawncol = (sp.get(i).location - 1) % 15;
				//get pawn location and compare with transx, transy
				//      margin         row/col number              compare           center on box
				ydiff = (50 + (pawnrow * gameBoardView.cellSize)) - transy + gameBoardView.cellSize / 2;
				xdiff = (50 + (pawncol * gameBoardView.cellSize)) - transx + gameBoardView.cellSize / 2;
				//if the distance between touch and pawn position < 25, pawn is selected pawn
				if (Math.sqrt((double)(xdiff * xdiff + ydiff * ydiff)) < 25)
				{selected = sp.get(i);}
			}
			if (selected != null)
			{
				StateChangeCurrentPawn sta = new StateChangeCurrentPawn(this, selected);
				game.sendAction(sta);
				//state.targetPawn = selected;
				selectedPawn = selected;
				sendTextMessage(getTextBox(), "selected a " + selected.color + " color pawn" + " x:" + transx + " y:" + transy);
			}
			return true;
		}
		return false;
	}

	public void setBoardView(GameBoardView gb)
	{
		this.gameBoardView = gb;
	}
}
package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.players.GameHumanPlayer;
import edu.up.cs301.GameFramework.GameMainActivity;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.infoMessage.GameInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SorryHumanPlayer extends GameHumanPlayer implements OnClickListener {
	private SorryState state;
	private GameMainActivity myActivity;
	private ImageView imageViewCard;
	private EditText editTextGridBox;
	private EditText editTextNumSpaces;
	private Button buttonMoveDot;
	private Button buttonMoveClockwise;
	private GameBoardView gameBoardView;

	public SorryHumanPlayer(String name) {
		super(name);
	}

	public View getTopView() {
		return myActivity.findViewById(R.id.sorry_gui_layout);
	}

	protected void updateDisplay() {
	}

	//gets text box for displaying messages
	public TextView getTextBox() {
		return myActivity.findViewById(R.id.textViewMessages);
	}

	//sends a message to the text box
	public void sendTextMessage(TextView t, String m) {
		t.setText(""); // Clear the previous message
		t.append(m); // Add the new message
	}

	public void onClick(View button) {
		if (game == null) return;
		if (state.getCardDrawn() == true) return;

		if (button.getId() == R.id.buttonDrawCards) {
			//generates/draws a random card number
			Random rand = new Random();
			int cardNum = rand.nextInt(11) + 1;
			int drawFace = 0;

			//displays card depending on what number card was drawn
			switch (cardNum) {
				case 1:
					drawFace = R.drawable.sorrycardone;
					state.setCardNumber(1);
					handleOneCard();
					break;
				case 2:
					drawFace = R.drawable.sorrycardtwo;
					state.setCardNumber(2);
					handleTwoCard();
					break;
				case 3:
					drawFace = R.drawable.sorrycardthree;
					handleThreeCard();
					state.setCardNumber(3);
					break;
				case 4:
					drawFace = R.drawable.sorrycardfour;
					handleFourCard();
					state.setCardNumber(4);
					break;
				case 5:
					drawFace = R.drawable.sorrycardfive;
					handleFiveCard();
					state.setCardNumber(5);
					break;
				case 6:
					drawFace = R.drawable.sorrycardseven;
					handleSevenCard();
					state.setCardNumber(6);
					break;
				case 7:
					drawFace = R.drawable.sorrycardeight;
					handleEightCard();
					state.setCardNumber(7);
					break;
				case 8:
					drawFace = R.drawable.sorrycardten;
					handleTenCard();
					state.setCardNumber(8);
					break;
				case 9:
					drawFace = R.drawable.sorrycardeleven;
					handleElevenCard();
					state.setCardNumber(9);
					break;
				case 10:
					drawFace = R.drawable.sorrycardtwelve;
					handleTwelveCard();
					state.setCardNumber(10);
					break;
				case 11:
					drawFace = R.drawable.sorrycardsorry;
					handleSorryCard();
					state.setCardNumber(11);
					break;
			}

			imageViewCard.setImageResource(drawFace);

			List<Integer> validMovePositions = getValidMovePositions(cardNum);

			//highlights valid boxes to move to based on card drawn
			gameBoardView.highlightValidMoves(validMovePositions);

			state.setCardDrawn(true);


		} else if (button.getId() == R.id.buttonMoveDot) {
			String gridBoxNumber = editTextGridBox.getText().toString();
			if (!gridBoxNumber.isEmpty()) {
				int position = Integer.parseInt(gridBoxNumber);
				gameBoardView.movePawnTo(position);
				editTextGridBox.setText("");

				gameBoardView.highlightValidMoves(Collections.emptyList());
				state.setCardDrawn(false);
			}
		} else if (button.getId() == R.id.buttonMoveClockwise) {
			String numSpacesString = editTextNumSpaces.getText().toString();
			if (!numSpacesString.isEmpty()) {
				int numSpaces = Integer.parseInt(numSpacesString);
				gameBoardView.moveClockwise(state.getCardNumber());
				editTextNumSpaces.setText("");
				state.setCardDrawn(false);
				// changed buttonMoveClockwise to move based on the card drawn
			}
		}
	}

	//displays message to text box depending on which card was drawn
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

	private List<Integer> getValidMovePositions(int cardNum) {
		return Collections.emptyList();
	}

	//receives game info and updates SorryState
	@Override
	public void receiveInfo(GameInfo info) {
		if (!(info instanceof SorryState)) return;
		this.state = (SorryState) info;
		updateDisplay();
	}

	public void setAsGui(GameMainActivity activity) {
		this.myActivity = activity;

		activity.setContentView(R.layout.sorry_xml_multi_line);

		//initialize GUI elements
		imageViewCard = activity.findViewById(R.id.imageViewCard);
		Button buttonDrawCards = activity.findViewById(R.id.buttonDrawCards);
		editTextGridBox = activity.findViewById(R.id.editTextGridBox);
		editTextNumSpaces = activity.findViewById(R.id.editTextNumSpaces);
		buttonMoveDot = activity.findViewById(R.id.buttonMoveDot);
		buttonMoveClockwise = activity.findViewById(R.id.buttonMoveClockwise);
		gameBoardView = activity.findViewById(R.id.gameBoardView);

		//register click listeners
		buttonDrawCards.setOnClickListener(this);
		buttonMoveDot.setOnClickListener(this);
		buttonMoveClockwise.setOnClickListener(this);
	}
}
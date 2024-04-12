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
	private Button buttonMoveDot;
	private GameBoardView gameBoardView;

	public SorryHumanPlayer(String name) {
		super(name);
	}

	public View getTopView() {
		return myActivity.findViewById(R.id.sorry_gui_layout);
	}

	protected void updateDisplay() {
	}

	public TextView getTextBox() {
		return myActivity.findViewById(R.id.textViewMessages);
	}

	public void sendTextMessage(TextView t, String m) {
		t.setText(""); // Clear the previous message
		t.append(m); // Add the new message
	}

	public void onClick(View button) {
		if (game == null) return;

		if (button.getId() == R.id.buttonDrawCards) {
			Random rand = new Random();
			int cardNum = rand.nextInt(11) + 1;
			int drawFace = 0;

			switch (cardNum) {
				case 1:
					drawFace = R.drawable.sorrycardone;
					handleOneCard();
					break;
				case 2:
					drawFace = R.drawable.sorrycardtwo;
					handleTwoCard();
					break;
				case 3:
					drawFace = R.drawable.sorrycardthree;
					handleThreeCard();
					break;
				case 4:
					drawFace = R.drawable.sorrycardfour;
					handleFourCard();
					break;
				case 5:
					drawFace = R.drawable.sorrycardfive;
					handleFiveCard();
					break;
				case 6:
					drawFace = R.drawable.sorrycardseven;
					handleSevenCard();
					break;
				case 7:
					drawFace = R.drawable.sorrycardeight;
					handleEightCard();
					break;
				case 8:
					drawFace = R.drawable.sorrycardten;
					handleTenCard();
					break;
				case 9:
					drawFace = R.drawable.sorrycardeleven;
					handleElevenCard();
					break;
				case 10:
					drawFace = R.drawable.sorrycardtwelve;
					handleTwelveCard();
					break;
				case 11:
					drawFace = R.drawable.sorrycardsorry;
					handleSorryCard();
					break;
			}

			imageViewCard.setImageResource(drawFace);

			List<Integer> validMovePositions = getValidMovePositions(cardNum);

			gameBoardView.highlightValidMoves(validMovePositions);
		} else if (button.getId() == R.id.buttonMoveDot) {
			String gridBoxNumber = editTextGridBox.getText().toString();
			if (!gridBoxNumber.isEmpty()) {
				int position = Integer.parseInt(gridBoxNumber);
				gameBoardView.movePawnTo(position);
				editTextGridBox.setText("");

				gameBoardView.highlightValidMoves(Collections.emptyList());
			}
		}
	}

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

	@Override
	public void receiveInfo(GameInfo info) {
		if (!(info instanceof SorryState)) return;
		this.state = (SorryState) info;
		updateDisplay();
	}

	public void setAsGui(GameMainActivity activity) {
		this.myActivity = activity;
		activity.setContentView(R.layout.sorry_xml_multi_line);

		imageViewCard = activity.findViewById(R.id.imageViewCard);
		Button buttonDrawCards = activity.findViewById(R.id.buttonDrawCards);
		editTextGridBox = activity.findViewById(R.id.editTextGridBox);
		buttonMoveDot = activity.findViewById(R.id.buttonMoveDot);
		gameBoardView = activity.findViewById(R.id.gameBoardView);

		buttonDrawCards.setOnClickListener(this);
		buttonMoveDot.setOnClickListener(this);
	}
}
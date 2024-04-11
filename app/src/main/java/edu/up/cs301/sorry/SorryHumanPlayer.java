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

import java.util.Random;

import edu.up.cs301.sorry.SorryState;

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
		// Set the text in the appropriate widget
		// counterValueTextView.setText("" + state.getCounter());
	}

	public void onClick(View button) {
		if (game == null) return;

		if (button.getId() == R.id.buttonDrawCards) {
			// Generate random card
			Random rand = new Random();
			int cardNum = rand.nextInt(11) + 1;
			int drawFace = 0;
			if (cardNum == 1) {
				drawFace = R.drawable.sorrycardone;
			} else if (cardNum == 2) {
				drawFace = R.drawable.sorrycardtwo;
			} else if (cardNum == 3) {
				drawFace = R.drawable.sorrycardthree;
			} else if (cardNum == 4) {
				drawFace = R.drawable.sorrycardfour;
			} else if (cardNum == 5) {
				drawFace = R.drawable.sorrycardfive;
			} else if (cardNum == 6) {
				drawFace = R.drawable.sorrycardseven;
			} else if (cardNum == 7) {
				drawFace = R.drawable.sorrycardeight;
			} else if (cardNum == 8) {
				drawFace = R.drawable.sorrycardten;
			} else if (cardNum == 9) {
				drawFace = R.drawable.sorrycardeleven;
			} else if (cardNum == 10) {
				drawFace = R.drawable.sorrycardtwelve;
			} else if (cardNum == 11) {
				drawFace = R.drawable.sorrycardsorry;
			}
			// Set ImageView to new card drawn
			imageViewCard.setImageResource(drawFace);
		} else if (button.getId() == R.id.buttonMoveDot) {
			String gridBoxNumber = editTextGridBox.getText().toString();
			if (!gridBoxNumber.isEmpty()) {
				int position = Integer.parseInt(gridBoxNumber);
				gameBoardView.moveDotTo(position);
			}
		}
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

		// Initialize widgets
		imageViewCard = activity.findViewById(R.id.imageViewCard);
		Button buttonDrawCards = activity.findViewById(R.id.buttonDrawCards);
		editTextGridBox = activity.findViewById(R.id.editTextGridBox);
		buttonMoveDot = activity.findViewById(R.id.buttonMoveDot);
		gameBoardView = activity.findViewById(R.id.gameBoardView);

		// Listeners for buttons
		buttonDrawCards.setOnClickListener(this);
		buttonMoveDot.setOnClickListener(this);
	}
}
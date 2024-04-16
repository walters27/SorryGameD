package edu.up.cs301.sorry;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

import edu.up.cs301.GameFramework.players.GameHumanPlayer;
import edu.up.cs301.GameFramework.GameMainActivity;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.infoMessage.GameInfo;

import android.os.Handler;
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
	private Handler handler = new Handler();

	public SorryHumanPlayer(String name) {
		super(name);
	}

	public View getTopView() {
		return myActivity.findViewById(R.id.sorry_gui_layout);
	}

	protected void updateDisplay() {
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

	public void onClick(View button) { if (game == null) return;
		if (button.getId() == R.id.buttonDrawCards) {
			drawCard();
		} else if (button.getId() == R.id.buttonMoveClockwise) {
			// Real player's turn
			gameBoardView.setCurrentPlayer(0);
			int bestPawnIndex = getBestPawnIndex(0);
			gameBoardView.selectPawn(bestPawnIndex);
			gameBoardView.moveClockwise(state.getCardNumber());
			state.setCardDrawn(false);
			if (gameBoardView.youWon) {
				sendTextMessage(getTextBox(), "You win");
				return;
			}

			// Automated player 1's turn
			new Handler().postDelayed(new Runnable() {
				public void run() {
					gameBoardView.setCurrentPlayer(1);
					int bestPawnIndex = getBestPawnIndex(1);
					gameBoardView.selectPawn(bestPawnIndex);
					drawCard();
					gameBoardView.moveClockwise(state.getCardNumber());
					if (gameBoardView.youLost) {
						sendTextMessage(getTextBox(), "You lost");
						return;
					}

					// Automated player 2's turn
					new Handler().postDelayed(new Runnable() {
						public void run() {
							gameBoardView.setCurrentPlayer(2);
							int bestPawnIndex = getBestPawnIndex(2);
							gameBoardView.selectPawn(bestPawnIndex);
							drawCard();
							gameBoardView.moveClockwise(state.getCardNumber());
							if (gameBoardView.youLost) {
								sendTextMessage(getTextBox(), "You lost");
								return;
							}

							// Automated player 3's turn
							new Handler().postDelayed(new Runnable() {
								public void run() {
									gameBoardView.setCurrentPlayer(3);
									int bestPawnIndex = getBestPawnIndex(3);
									gameBoardView.selectPawn(bestPawnIndex);
									drawCard();
									gameBoardView.moveClockwise(state.getCardNumber());
									if (gameBoardView.youLost) {
										sendTextMessage(getTextBox(), "You lost");
										return;
									}

									// Real player's turn again
									new Handler().postDelayed(new Runnable() {
										public void run() {
											gameBoardView.setCurrentPlayer(0);
											int bestPawnIndex = getBestPawnIndex(0);
											gameBoardView.selectPawn(bestPawnIndex);
										}
									}, 200);
								}
							}, 200);
						}
					}, 200);
				}
			}, 200);
		}
	}
	private int getBestPawnIndex(int playerIndex) {
		int bestPawnIndex = 0;
		int maxDistance = -1;
		for (int i = 0; i < 4; i++) {
			SorryPawn pawn = gameBoardView.pawns.get(playerIndex * 4 + i);
			int distance = getDistanceToHome(pawn);

			if (distance > maxDistance) {
				maxDistance = distance;
				bestPawnIndex = i;
			}
		}

		return bestPawnIndex;
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

		return distance;}









	public void drawCard(){
		// generates/draws a random card number
		Random rand = new Random();
		int cardNum = rand.nextInt(11) + 1;
		int drawFace = 0;

		// displays card depending on what number card was drawn
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
				state.setCardNumber(7);
				break;
			case 7:
				drawFace = R.drawable.sorrycardeight;
				handleEightCard();
				state.setCardNumber(8);
				break;
			case 8:
				drawFace = R.drawable.sorrycardten;
				handleTenCard();
				state.setCardNumber(10);
				break;
			case 9:
				drawFace = R.drawable.sorrycardeleven;
				handleElevenCard();
				state.setCardNumber(11);
				break;
			case 10:
				drawFace = R.drawable.sorrycardtwelve;
				handleTwelveCard();
				state.setCardNumber(12);
				break;
			case 11:
				drawFace = R.drawable.sorrycardsorry;
				handleSorryCard();
				state.setCardNumber(13);
				break;
		}

		imageViewCard.setImageResource(drawFace);
		state.setCardDrawn(true);
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

	private List<Integer> getValidMovePositions(int cardNum) {
		// TODO: Implement the logic to determine valid move positions based on the card number
		return Collections.emptyList();
	}

	// receives game info and updates SorryState
	@Override
	public void receiveInfo(GameInfo info) {
		if (!(info instanceof SorryState)) return;
		this.state = (SorryState) info;
		updateDisplay();
	}

	public void setAsGui(GameMainActivity activity) {
		this.myActivity = activity;

		activity.setContentView(R.layout.sorry_xml_multi_line);

		// initialize GUI elements
		imageViewCard = activity.findViewById(R.id.imageViewCard);
		Button buttonDrawCards = activity.findViewById(R.id.buttonDrawCards);
		//editTextGridBox = activity.findViewById(R.id.editTextGridBox);
		//editTextNumSpaces = activity.findViewById(R.id.editTextNumSpaces);
		//buttonMoveDot = activity.findViewById(R.id.buttonMoveDot);
		buttonMoveClockwise = activity.findViewById(R.id.buttonMoveClockwise);
		gameBoardView = activity.findViewById(R.id.gameBoardView);

		// register click listeners
		buttonDrawCards.setOnClickListener(this);
		//buttonMoveDot.setOnClickListener(this);
		buttonMoveClockwise.setOnClickListener(this);
	}
}
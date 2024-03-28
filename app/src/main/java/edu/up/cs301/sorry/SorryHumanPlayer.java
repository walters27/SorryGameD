package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.players.GameHumanPlayer;
import edu.up.cs301.GameFramework.GameMainActivity;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.infoMessage.GameInfo;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import edu.up.cs301.sorry.SorryState;


/**
 * A GUI of a counter-player. The GUI displays the current value of the counter,
 * and allows the human player to press the '+' and '-' buttons in order to
 * send moves to the game.
 * 
 * Just for fun, the GUI is implemented so that if the player presses either button
 * when the counter-value is zero, the screen flashes briefly, with the flash-color
 * being dependent on whether the player is player 0 or player 1.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll, Quince Pham, Corwin Carr, Kira Kunitake, Annalise Walters
 * @version July 2013
 */
public class SorryHumanPlayer extends GameHumanPlayer implements OnClickListener {

	/* instance variables */
	
	// The TextView the displays the current counter value
	private TextView testResultTextView;
	
	// the most recent game state, as given to us by the CounterLocalGame
	private SorryState state;
	
	// the android activity that we are running
	private GameMainActivity myActivity;
	
	/**
	 * constructor
	 * @param name
	 * 		the player's name
	 */
	public SorryHumanPlayer(String name) {
		super(name);
	}

	/**
	 * Returns the GUI's top view object
	 * 
	 * @return
	 * 		the top object in the GUI's view heirarchy
	 */
	public View getTopView() {

		return myActivity.findViewById(R.id.sorry_gui_layout);
	}
	
	/**
	 * sets the counter value in the text view
	 */
	protected void updateDisplay() {
		// set the text in the appropriate widget
		//counterValueTextView.setText("" + state.getCounter());
	}

	/**
	 * this method gets called when the user clicks the '+' or '-' button. It
	 * creates a new CounterMoveAction to return to the parent activity.
	 * 
	 * @param button
	 * 		the button that was clicked
	 */
	public void onClick(View button) {
		// if we are not yet connected to a game, ignore
		if (game == null) return;


		// Clear any text currently displayed in the multi-line EditText
		testResultTextView.setText("");


		// Create a new instance of the game state class using the default constructor
		SorryState firstInstance = new SorryState();


		// Create a deep copy of firstInstance
		SorryState firstCopy = new SorryState(firstInstance);


		// Simulate an entire game with firstInstance
		simulateGame(firstInstance);


		// Create another new instance of the game state for comparison
		SorryState secondInstance = new SorryState();


		// Create a deep copy of secondInstance
		SorryState secondCopy = new SorryState(secondInstance);


		// Call toString() on both copies to prepare for comparison
		String firstCopyString = firstCopy.toString();
		String secondCopyString = secondCopy.toString();


		// Verify if the two strings are identical
		boolean areIdentical = firstCopyString.equals(secondCopyString);


		// Append the verification result to the EditText
		appendToGameLog("Verification: " + (areIdentical ? "The two game states are identical." : "The two game states are not identical."));


		// Print both strings for visual inspection
		appendToGameLog("First game state: " + firstCopyString);
		appendToGameLog("Second game state: " + secondCopyString);
	}


	private void simulateGame(SorryState state) {
		// Game starts: all pawns are at start
		appendToGameLog("Game starts. All pawns are at their start positions.");

		while (true) {
			// Interaction 1
			state.setPlayerId(1);
			state.setCardDrawn(true);
			state.setBluePawnHomeCount(state.getBluePawnHomeCount() - 1);
			state.setBluePawnHomeCount(state.getBluePawnHomeCount() + 1);
			appendToGameLog("Player 1 draws a card and moves a blue pawn from start.");

			// Interaction 2
			state.setPlayerId(2);
			state.setCardDrawn(true);
			state.setRedPawnHomeCount(state.getRedPawnHomeCount() - 1);
			state.setRedPawnHomeCount(state.getRedPawnHomeCount() + 1);
			appendToGameLog("Player 2 draws a card and moves a red pawn towards home.");

			// Interaction 3
			state.setPlayerId(3);
			state.setCardDrawn(true);
			state.setGreenPawnHomeCount(state.getGreenPawnHomeCount() - 1);
			state.setGreenPawnHomeCount(state.getGreenPawnHomeCount() + 1);
			appendToGameLog("Player 3 draws a card and moves a green pawn towards home.");

			// Interaction 4
			state.setPlayerId(4);
			state.setCardDrawn(true);
			state.setYellowPawnHomeCount(state.getYellowPawnHomeCount() - 1);
			state.setYellowPawnHomeCount(state.getYellowPawnHomeCount() + 1);
			appendToGameLog("Player 4 draws a card and moves a yellow pawn towards home.");

			// Interaction 5: Slide
			state.setPlayerId(1);
			state.setCardDrawn(true);
			state.setRedPawnHomeCount(state.getRedPawnHomeCount() - 1); // Assume red pawn is in the way
			state.setRedPawnHomeCount(state.getRedPawnHomeCount() + 1); // Move red pawn back to start
			state.setBluePawnHomeCount(state.getBluePawnHomeCount() + 1); // Move blue pawn forward
			appendToGameLog("Player 1 draws a card, slides another player's pawn back, and moves their blue pawn forward.");

			// Interaction 6: Switch
			state.setPlayerId(2);
			state.setCardDrawn(true);
			int tempRedCount = state.getRedPawnHomeCount();
			state.setRedPawnHomeCount(state.getBluePawnHomeCount());
			state.setBluePawnHomeCount(tempRedCount);
			appendToGameLog("Player 2 draws a card and switches the positions of their red pawn and player 1's blue pawn.");

			// Interaction 7: Sorry
			state.setPlayerId(3);
			state.setCardDrawn(true);
			state.setYellowPawnHomeCount(state.getYellowPawnHomeCount() - 1); // Assume yellow pawn is in the way
			state.setYellowPawnHomeCount(state.getYellowPawnHomeCount() + 1); // Move yellow pawn back to start
			state.setGreenPawnHomeCount(state.getGreenPawnHomeCount() + 1); // Move green pawn forward
			appendToGameLog("Player 3 draws a card, apologizes (Sorry), sends another player's pawn back, and moves their green pawn forward.");

			// Check for a winner
			if (state.getBluePawnHomeCount() == 4) {
				appendToGameLog("Player 1 wins the game!");
				break;
			} else if (state.getRedPawnHomeCount() == 4) {
				appendToGameLog("Player 2 wins the game!");
				break;
			} else if (state.getGreenPawnHomeCount() == 4) {
				appendToGameLog("Player 3 wins the game!");
				break;
			} else if (state.getYellowPawnHomeCount() == 4) {
				appendToGameLog("Player 4 wins the game!");
				break;
			}
		}

		// Game over
		appendToGameLog("Game over.");
	}




	private void appendToGameLog(String message) {
		// Appends a message to the game log in the multi-line EditText
		String currentText = testResultTextView.getText().toString();
		String newText = currentText.isEmpty() ? message : currentText + "\n" + message;
		testResultTextView.setText(newText);
	}
//onClick



	/**
	 * callback method when we get a message (e.g., from the game)
	 * 
	 * @param info
	 * 		the message
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		// ignore the message if it's not a CounterState message
		if (!(info instanceof SorryState)) return;
		
		// update our state; then update the display
		this.state = (SorryState)info;
		updateDisplay();
	}
	
	/**
	 * callback method--our game has been chosen/rechosen to be the GUI,
	 * called from the GUI thread
	 * 
	 * @param activity
	 * 		the activity under which we are running
	 */
	public void setAsGui(GameMainActivity activity) {
		
		// remember the activity
		this.myActivity = activity;
		
	    // Load the layout resource for our GUI
		activity.setContentView(R.layout.sorry_xml_multi_line);

		// Refrence EditText to test text view
		testResultTextView = myActivity.findViewById(R.id.editTextTextMultiLine2);
		myActivity.findViewById(R.id.testButton).setOnClickListener(this);


	}

}// class CounterHumanPlayer


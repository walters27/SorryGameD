package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.players.GameHumanPlayer;
import edu.up.cs301.GameFramework.GameMainActivity;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.infoMessage.GameInfo;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.Random;

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

	private ImageView imageViewCard = null;
	
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

		// Create a new instance of the game state class using the default constructor
		SorryState firstInstance = new SorryState();

		//Generate random card
		Random rand = new Random();
		int cardNum = rand.nextInt(11)+1;
		int drawFace = 0;
		if(cardNum == 1){
			drawFace = R.drawable.sorrycardone;
		}
		else if(cardNum ==2){
			drawFace = R.drawable.sorrycardtwo;
		}
		else if(cardNum ==3){
			drawFace = R.drawable.sorrycardthree;
		}
		else if(cardNum ==4){
			drawFace = R.drawable.sorrycardfour;
		}
		else if(cardNum ==5){
			drawFace = R.drawable.sorrycardfive;
		}
		else if(cardNum ==6){
			drawFace = R.drawable.sorrycardseven;
		}
		else if(cardNum ==7){
			drawFace = R.drawable.sorrycardeight;
		}
		else if(cardNum ==8){
			drawFace = R.drawable.sorrycardten;
		}
		else if(cardNum ==9){
			drawFace = R.drawable.sorrycardeleven;
		}
		else if(cardNum ==10){
			drawFace = R.drawable.sorrycardtwelve;
		}
		else if(cardNum ==11){
			drawFace = R.drawable.sorrycardsorry;
		}
		//set ImageView to new card drawn
		imageViewCard.setImageResource(drawFace);
	}


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

		//initialize widgets
		this.imageViewCard = (ImageView) activity.findViewById(R.id.imageViewCard);
		Button buttonDrawCards = (Button)activity.findViewById(R.id.buttonDrawCards);

		//Listener for button
		buttonDrawCards.setOnClickListener(this);
	}

}// class CounterHumanPlayer


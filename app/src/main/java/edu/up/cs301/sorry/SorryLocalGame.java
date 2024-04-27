package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.infoMessage.GameState;
import edu.up.cs301.GameFramework.players.GamePlayer;
import edu.up.cs301.GameFramework.LocalGame;
import edu.up.cs301.GameFramework.actionMessage.GameAction;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Random;

/**
 * A class that represents the state of a game. In our counter game, the only
 * relevant piece of information is the value of the game's counter. The
 * CounterState object is therefore very simple.
 *
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll, Annalise Walters, Kira Kunitake, Corwin Carr, Quince Pham.
 * @version July 2013
 */
public class SorryLocalGame extends LocalGame {
	// the game's state
	private SorryState gameState;
	private Activity myActivity;


	/**
	 * can this player move
	 *
	 * @return true, because all player are always allowed to move at all times,
	 * as this is a fully asynchronous game
	 */
	@Override
	protected boolean canMove(int playerIdx) {
		return true;
	}

	/**
	 *checks if game is over by seeing if any player has all 4 pawns in home
	 * and sends a message to the message box if they do
	 */
	@Override
	protected String checkIfGameOver() {
		if (gameState.getPawnHomeCount(0) == 4) {
			handleGameOver("Player 0 has won!");
			return "Player 0 has won!";
		}
		else if (gameState.getPawnHomeCount(1) == 4) {
			handleGameOver("Player 1 has won!");
			return "Player 1 has won!";
		}
		else if (gameState.getPawnHomeCount(2) == 4) {
			handleGameOver("Player 2 has won!");
			return "Player 2 has won!";
		}
		else if (gameState.getPawnHomeCount(3) == 4) {
			handleGameOver("Player 3 has won!");
			return "Player 3 has won!";
		}
		else{
			return null;
		}
	}
	private void handleGameOver(String message){
		//get reference to text box and set game over message
		TextView textViewMessages = myActivity.findViewById((R.id.textViewMessages));
		textViewMessages.setText(message);
	}

	/**
	 * This ctor should be called when a new counter game is started
	 */
	public SorryLocalGame(GameState state) {
		// initialize the game state, with the counter value starting at 0
		if (state != null) {
			this.state = state;
		} else {
			this.state = new SorryState();
		}
		gameState = new SorryState();
	}

	/**
	 * The only type of GameAction that should be sent is CounterMoveAction
	 */
	@Override
	protected boolean makeMove(GameAction action) {
		//Reject  moves if it's  not your turn
		if (!this.players[this.gameState.getPlayerId()].equals(action.getPlayer())) {
			return false;
		}

		if (action instanceof SorryDrawCard) {
			//draw card action
			SorryDrawCard sdc = (SorryDrawCard) action;
			this.gameState.drawCard(sdc);
			return true;
		} else if (action instanceof MoveForwardAction) {
			//move forward action
			gameState.moveClockwise(gameState.getCardNumber());

			return true;
		} else if (action instanceof StateChangeCurrentPawn) {
			//change current pawn action
			if ( gameState.getTeamIdFromPawn(((StateChangeCurrentPawn)action).getPawn()) != gameState.getPlayerId())
			{
				gameState.targetPawn = ((StateChangeCurrentPawn) action).getPawn();
			}
			else {
			gameState.currentPawn = ((StateChangeCurrentPawn) action).getPawn(); }
			return true;
		} else if (action instanceof SkipTurnAction) {
			//skip turn action
			gameState.moveNextTurn();
			return true;}

		else {
			// denote that this was an illegal move
			return false;
		}
	}//makeMove

	/**
	 * send the updated state to a given player
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// this is a perfect-information game, so we'll make a
		// complete copy of the state to send to the player
		p.sendInfo(new SorryState(this.gameState));

	}//sendUpdatedSate

}

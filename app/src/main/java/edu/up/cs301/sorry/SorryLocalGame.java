package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.infoMessage.GameState;
import edu.up.cs301.GameFramework.players.GamePlayer;
import edu.up.cs301.GameFramework.LocalGame;
import edu.up.cs301.GameFramework.actionMessage.GameAction;

import android.graphics.Color;
import android.util.Log;

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

	public static final int TARGET_MAGNITUDE = 10;

	// the game's state
	private SorryState gameState;
	private int playerId;


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

	@Override
	protected String checkIfGameOver() {
		return null;
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
		Log.d("turn", "action sender:" + action.getPlayer() + " current player:" + this.players[this.gameState.getPlayerId()]);
		Log.d("", "gamestate player id: "+gameState.getPlayerId());
		if (action instanceof SorryDrawCard) {
			SorryDrawCard sdc = (SorryDrawCard) action;
			this.gameState.drawCard(sdc);
			return true;
		} else if (action instanceof MoveForwardAction) {
			//SorryMoveAction sma = (SorryMoveAction) action;
			gameState.moveClockwise(gameState.getCardNumber());

			return true;
		} else if (action instanceof StateChangeCurrentPawn) {
			//StateChangeCurrentPawn sta = (StateChangeCurrentPawn) action;
			gameState.currentPawn = ((StateChangeCurrentPawn) action).getPawn();
			return true;
		} else {
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

package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.infoMessage.GameState;
import edu.up.cs301.GameFramework.players.GamePlayer;
import edu.up.cs301.GameFramework.LocalGame;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import android.util.Log;

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

	// When a counter game is played, any number of players. The first player
	// is trying to get the counter value to TARGET_MAGNITUDE; the second player,
	// if present, is trying to get the counter to -TARGET_MAGNITUDE. The
	// remaining players are neither winners nor losers, but can interfere by
	// modifying the counter.
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
		Log.i("action", action.getClass().toString());

		if (action instanceof SorryMoveAction) {

			// cast so that we Java knows it's a CounterMoveAction
			SorryMoveAction cma = (SorryMoveAction) action;

			// denote that this was a legal/successful move
			return true;
		} else {
			// denote that this was an illegal move
			return false;
		}
	}//makeMove

	// helper method to update player turn after a move
	protected boolean movePiece(GameAction action) {

		if (action instanceof SorryMoveAction) {

			SorryMoveAction cma = (SorryMoveAction) action;

			if (gameState.getPlayerId() == 0) {

				gameState.setPlayerId(1);
			} else if (gameState.getPlayerId() == 1) {

				gameState.setPlayerId(2);
			} else if (gameState.getPlayerId() == 2) {

				gameState.setPlayerId(3);
			} else if (gameState.getPlayerId() == 3) {

				gameState.setPlayerId(0);
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * send the updated state to a given player
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// this is a perfect-information game, so we'll make a
		// complete copy of the state to send to the player
		p.sendInfo(new SorryState(this.gameState));

	}//sendUpdatedSate

/*	@Override
	protected String checkIfGameOver() {
		for (int i = 0; i < 4; i++) {
			if(gameState.getPawnHomeCount(i) == 1){
				return "Player " + (i+1) + " wins!";
			}
		}
		return null;
	}*/

}

// class CounterLocalGame

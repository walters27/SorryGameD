package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.infoMessage.GameState;
import edu.up.cs301.GameFramework.players.GamePlayer;
import edu.up.cs301.GameFramework.LocalGame;
import edu.up.cs301.GameFramework.actionMessage.GameAction;
import android.util.Log;

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
		}
		else {
			this.state = new SorryState();
		}
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

			// Update the counter values based upon the action
			//int result = gameState.getCounter() + (cma.isPlus() ? 1 : -1);
			//gameState.setCounter(result);

			// denote that this was a legal/successful move
			return true;
		} else {
			// denote that this was an illegal move
			return false;
		}
	}//makeMove
	// helper method to update player turn after a move
	protected boolean movePiece(GameAction action){

		if (action instanceof SorryMoveAction)
		{

			SorryMoveAction cma = (SorryMoveAction) action;

			if(gameState.getPlayerId() == 0){

				gameState.setPlayerId(1);
			}

			else if(gameState.getPlayerId() == 1){

				gameState.setPlayerId(2);
			}
			else if(gameState.getPlayerId() == 2){

				gameState.setPlayerId(3);
			}
			else if(gameState.getPlayerId() == 3){

				gameState.setPlayerId(4);
			}
			else if(gameState.getPlayerId() == 4){

				gameState.setPlayerId(0);
			}
			return true;
		}
		else {
			return false;
		}
	}

	protected boolean drawCard (GameAction action){
		if (gameState.getCardDrawn() == true){
			return true;
		}
		else {
			return false;
		}
	}

	protected boolean isGameOver(GameAction action){
		if (gameState.getBluePawnHomeCount() == 4 ||
				gameState.getRedPawnHomeCount() == 4||
				gameState.getYellowPawnHomeCount() == 4 ||
				gameState.getGreenPawnHomeCount() == 4){
			return true;
		}
		else{
			return false;
		}
	}
	/*public boolean pawnIsHome(GameAction action, int i){
		if (gameState.pawn[i].getLocation() >= 61)
			return true;
		else{
			return false;
		}
	}*/
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

	/**
	 * Check if the game is over. It is over, return a string that tells
	 * who the winner(s), if any, are. If the game is not over, return null;
	 * 
	 * @return
	 * 		a message that tells who has won the game, or null if the
	 * 		game is not over
	 */
	/*@Override*/
/*	protected String checkIfGameOver() {*/

		// get the value of the counter
		/*int counterVal = this.gameState.getCounter();
		
		if (counterVal >= TARGET_MAGNITUDE) {
			// counter has reached target magnitude, so return message that
			// player 0 has won.
			return playerNames[0]+" has won.";
		}
		else if (counterVal <= -TARGET_MAGNITUDE) {
			// counter has reached negative of target magnitude; if there
			// is a second player, return message that this player has won,
			// otherwise that the first player has lost
			if (playerNames.length >= 2) {
				return playerNames[1]+" has won.";
			}
			else {
				return playerNames[0]+" has lost.";
			}
		}else {
			// game is still between the two limit: return null, as the game
			// is not yet over
			return null;
		}*/



// class CounterLocalGame

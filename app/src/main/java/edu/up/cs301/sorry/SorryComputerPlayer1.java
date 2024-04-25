package edu.up.cs301.sorry;

import java.util.Random;

import edu.up.cs301.GameFramework.players.GameComputerPlayer;
import edu.up.cs301.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.GameFramework.utilities.Tickable;


/**
 * Dumb computer player
 *
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll, Annalise Walters, Kira Kunitake, Quince Pham, Corwin Carr
 * @version September 2013
 */
public class SorryComputerPlayer1 extends GameComputerPlayer {

	/**
	 * Constructor for objects of class CounterComputerPlayer1
	 *
	 * @param name the player's name
	 */
	public SorryComputerPlayer1(String name) {
		// invoke superclass constructor
		super(name);
	}

	//TODO: Nux says it's better to put variables like this in the game state
	//Example:  gameState.phase =  DRAW_PHASE | SELECT_PAWN_PHASE | MOVE_OR_SELECT_PHASE
	private boolean needToDraw = true;

	/**
	 * callback method--game's state has changed
	 *
	 * @param info the information (presumably containing the game's state)
	 */
	@Override
	protected void receiveInfo(GameInfo info) {
		//cast info to SorryState
		SorryState gameState = (SorryState)info;
		//check if it's the current players turn
		if(info instanceof SorryState &&
				gameState.getPlayerId() == this.playerNum){

			//Do I need to draw a card
			if (needToDraw) {
				SorryDrawCard sdc = new SorryDrawCard(this);
				game.sendAction(sdc);
				needToDraw = false;
			}
			else {  //move
				//TODO: choose which pawn to move
				SorryPawn pawn = null;


				//create a MoveForward action
				MoveForwardAction forward = new MoveForwardAction(this, pawn);
				//send move forward to the game
				game.sendAction(forward);
				needToDraw = true;
			}
		}
	}
}


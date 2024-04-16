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
			//generate a random card
			Random rand = new Random();
			int cardNum = rand.nextInt(11) + 1;
			//create a MoveForward action
			MoveForwardAction forward = new MoveForwardAction (this);
			//send move forward to the game
			game.sendAction(forward);
		}
	}
}


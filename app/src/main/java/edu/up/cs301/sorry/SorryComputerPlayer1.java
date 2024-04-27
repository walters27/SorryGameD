package edu.up.cs301.sorry;

import android.util.Log;

import java.util.Random;

import edu.up.cs301.GameFramework.infoMessage.IllegalMoveInfo;
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
	public SorryComputerPlayer1(String name) {
		// invoke superclass constructor
		super(name);
	}

	private boolean needToDraw = true;

	/**
	 * callback method--game's state has changed
	 *
	 * @param info the information (presumably containing the game's state)
	 */
	@Override
	protected void receiveInfo(GameInfo info) {
		//cast info to SorryState
		if (info instanceof IllegalMoveInfo) {
			//do nothing if it's an illegal move
			return;}

		SorryState gameState = (SorryState)info;

		//check if it's the current players turn
		if(info instanceof SorryState && gameState.getPlayerId() == this.playerNum){
			try {Thread.sleep(500);} catch (Exception e) {}
			if (needToDraw) {
				//If you need to draw a card, create draw card action
				SorryDrawCard sdc = new SorryDrawCard(this);
				//set draw card to false after drawing a card
				needToDraw = false;
				//send card action to the game
				game.sendAction(sdc);
			}
			else {  //move
				//get available pawns for the player
				SorryPawn[] movePawn = gameState.getPlayerPawns(playerNum);

				Random rand = new Random();
				//randomly choose a pawn to move
				SorryPawn pawn = movePawn[rand.nextInt(movePawn.length)];

				//create change pawn action
				StateChangeCurrentPawn sta = new StateChangeCurrentPawn(this, pawn);
				//send action to the game
				game.sendAction(sta);

				//create a MoveForward action
				MoveForwardAction forward = new MoveForwardAction(this, pawn);
				//send move forward to the game
				game.sendAction(forward);

				SkipTurnAction ska = new SkipTurnAction(this, null);
				game.sendAction(ska);

				//set needToDraw to true to draw again
				needToDraw = true;
			}
		}
	}
}


package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.GameMainActivity;
import edu.up.cs301.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.GameFramework.infoMessage.IllegalMoveInfo;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;


/**
*Smart computer player
 *
* @author Steven R. Vegdahl
* @author Andrew M. Nuxoll, Corwin Carr, Quince Pham, Kira Kunitake, Annalise Walters
* @version September 2013
*/
public class SorryComputerPlayer2 extends SorryComputerPlayer1 {
	public SorryComputerPlayer2(String name) {
		super(name);
	}
	private boolean needToDraw = true;

	@Override
	protected void receiveInfo(GameInfo info) {
		//cast info to SorryState
		if (info instanceof IllegalMoveInfo) {
			//do nothing if it's an illegal move
			return;}

		SorryState gameState = (SorryState)info;

		//check if it's the current players turn
		if(info instanceof SorryState && gameState.getPlayerId() == this.playerNum){
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
				SorryPawn[] allPawns = gameState.getPlayerPawns(playerNum);

				for(int i = 0; i < allPawns.length; i++){
					//move pawn if it is not home
					if (!allPawns[i].isHome) {
						//create change pawn action
						StateChangeCurrentPawn sta = new StateChangeCurrentPawn(this, allPawns[i]);
						//send action to the game
						game.sendAction(sta);
						//create a MoveForward action
						MoveForwardAction forward = new MoveForwardAction(this, allPawns[i]);
						//send move forward to the game
						game.sendAction(forward);
						//set needToDraw to true to draw again
						needToDraw = true;

					}
				}


			}
		}
	}
}

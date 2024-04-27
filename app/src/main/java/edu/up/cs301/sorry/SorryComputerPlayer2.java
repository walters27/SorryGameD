package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.GameMainActivity;
import edu.up.cs301.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.GameFramework.infoMessage.IllegalMoveInfo;
import edu.up.cs301.GameFramework.players.GameComputerPlayer;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

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

		// check if it's the current players turn
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
				ArrayList<SorryPawn> movePawn = new ArrayList<>();
				for (int i = 0; i < gameState.getPlayerPawns(playerNum).length; i++)
				{movePawn.add(gameState.getPlayerPawns(playerNum)[i]);}
				SorryPawn pawn = null;
				//try to start a pawn if possible
				for (SorryPawn s : movePawn)
				{
					if (s.isHome) {continue;}
					if (gameState.getCardNumber() == 1 || gameState.getCardNumber() == 2 || gameState.getCardNumber() == 13)
					{
						if (s.isInStart) {pawn = s;}
					}
				}
				// try to bring a single pawn to safety
				if (pawn == null) {
				for (SorryPawn s : movePawn)
				{
					if (s.isHome || s.isInStart) {continue;}
					pawn = s;
				} }
				//create change pawn action
				StateChangeCurrentPawn sta = new StateChangeCurrentPawn(this, pawn);
				//send action to the game
				game.sendAction(sta);

				//create a MoveForward action
				MoveForwardAction forward = new MoveForwardAction(this, pawn);
				//send move forward to the game
				game.sendAction(forward);

				//set needToDraw to true to draw again
				needToDraw = true;

				SkipTurnAction ska = new SkipTurnAction(this, null);
				game.sendAction(ska);

			}
		}
	}
}
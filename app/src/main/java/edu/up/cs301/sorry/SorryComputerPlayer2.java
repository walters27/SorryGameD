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
import java.util.Map;
import java.util.Random;

public class SorryComputerPlayer2 extends SorryComputerPlayer1 {
	public SorryComputerPlayer2(String name) {
		super(name);
	}

	private boolean needToDraw = true;
	private SorryPawn currentPawn = null;

	@Override
	protected void receiveInfo(GameInfo info) {
		if (info instanceof IllegalMoveInfo) {
			return;
		}

		SorryState gameState = (SorryState) info;

		if (info instanceof SorryState && gameState.getPlayerId() == this.playerNum) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}

			if (needToDraw) {
				SorryDrawCard sdc = new SorryDrawCard(this);
				needToDraw = false;
				game.sendAction(sdc);
			} else {
				SorryPawn[] movePawns = gameState.getPlayerPawns(playerNum);

				if (currentPawn == null || currentPawn.isHome) {
					currentPawn = null;
					int maxDistanceFromStart = -1;

					// Find the pawn with the maximum distance from the start position
					for (SorryPawn pawn : movePawns) {
						if (!pawn.isHome) {
							int distanceFromStart = pawn.location;
							if (distanceFromStart > maxDistanceFromStart) {
								currentPawn = pawn;
								maxDistanceFromStart = distanceFromStart;
							}
						}
					}

					// If no pawn is found outside the home square, select a random pawn
					if (currentPawn == null) {
						Random rand = new Random();
						currentPawn = movePawns[rand.nextInt(movePawns.length)];
					}
				}

				StateChangeCurrentPawn sta = new StateChangeCurrentPawn(this, currentPawn);
				game.sendAction(sta);

				MoveForwardAction forward = new MoveForwardAction(this, currentPawn);
				game.sendAction(forward);

				SkipTurnAction ska = new SkipTurnAction(this, null);
				game.sendAction(ska);

				needToDraw = true;
			}
		}
	}
}
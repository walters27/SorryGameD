package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

public class StateChangeCurrentPawn extends GameAction {
    public SorryPawn pawn;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public StateChangeCurrentPawn(GamePlayer player, SorryPawn p) {
        super(player);
        this.pawn = p;
    }

    public SorryPawn getPawn() {
        return pawn;
    }
}

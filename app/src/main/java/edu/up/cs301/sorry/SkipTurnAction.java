package edu.up.cs301.sorry;

import java.io.Serializable;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

public class SkipTurnAction extends GameAction implements Serializable {

    final static private long serialVersionUID = 7737654376238098346L;

    public SorryPawn pawn;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public SkipTurnAction(GamePlayer player, SorryPawn p) {
        super(player);
        this.pawn = p;
    }

    public SorryPawn getPawn() {
        return pawn;
    }
}

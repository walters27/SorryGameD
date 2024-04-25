package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

public class MoveForwardAction extends GameAction {

    public SorryPawn pawn;
    private static final long serialVersionUID = 28062013L;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public MoveForwardAction(GamePlayer player, SorryPawn pawn) {
        super(player);
        this.pawn = pawn;
    }
}

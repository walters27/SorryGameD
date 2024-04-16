package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

public class MoveForwardAction extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public MoveForwardAction(GamePlayer player) {
        super(player);
    }
}

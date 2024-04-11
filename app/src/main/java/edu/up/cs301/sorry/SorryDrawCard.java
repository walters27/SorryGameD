package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

public class SorryDrawCard extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public SorryDrawCard(GamePlayer player) {
        super(player);
    }
}

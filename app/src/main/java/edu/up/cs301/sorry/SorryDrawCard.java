package edu.up.cs301.sorry;

import java.io.Serializable;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

public class SorryDrawCard extends GameAction implements Serializable {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    final private static long serialVersionUID = 192847198274L;
    public SorryDrawCard(GamePlayer player) {
        super(player);
    }
}

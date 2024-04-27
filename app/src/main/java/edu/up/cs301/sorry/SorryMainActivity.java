package edu.up.cs301.sorry;

import android.content.res.Configuration;

import java.util.ArrayList;

import edu.up.cs301.GameFramework.GameMainActivity;
import edu.up.cs301.GameFramework.infoMessage.GameState;
import edu.up.cs301.GameFramework.players.GamePlayer;
import edu.up.cs301.GameFramework.LocalGame;
import edu.up.cs301.GameFramework.gameConfiguration.*;

/**
 * this is the primary activity for Counter game
 * 
 * @author Andrew M. Nuxoll, Kira Kunitake, Annalise Walters, Crorwin Carr, Quince Pham
 * @author Steven R. Vegdahl
 * @version April 2024
 *
 * Features: Custom Soundtrack, Custom image icon, Complex Gestures (swipe up to draw card),
 * How to Play screen, orientation handling, network play, sliders, Sorry! Special card.
 *
 * Bug: Sometimes the game crashes before the game is over.
 *
 *Button Actions: Start game by drawing a card. If you do not draw a 1 or 2 card
 * to start, skip turn. If a pawn is in the space you are about to move
 * to, skip turn. To play the sorry card click the pawn you want to switch
 * positions with and click move.
 *
 */
public class SorryMainActivity extends GameMainActivity {
	
	// the port number that this game will use when playing over the network
	private static final int PORT_NUMBER = 2234;

	/**
	 * Create the default configuration for this game:
	 * - one human player vs. one computer player
	 * - minimum of 1 player, maximum of 2
	 * - one kind of computer player and one kind of human player available
	 * 
	 * @return
	 * 		the new configuration object, representing the default configuration
	 */
	@Override
	public GameConfig createDefaultConfig() {
		
		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();
		
		// a human player player type (player type 0)
		playerTypes.add(new GamePlayerType("Local Human Player") {
			public GamePlayer createPlayer(String name) {
				return new SorryHumanPlayer(name);
			}});
		
		// computer player type (player type 1)
		playerTypes.add(new GamePlayerType("Computer Player") {
			public GamePlayer createPlayer(String name) {
				return new SorryComputerPlayer1(name);
			}});
		
		// computer player type (player type 2)
		playerTypes.add(new GamePlayerType("Smart Computer Player") {
			public GamePlayer createPlayer(String name) {
				return new SorryComputerPlayer2(name);
			}});

		// Create a game configuration class for Sorry!:
		// - player types as given above
		// - 4 players
		// - name of game is "Sorry!"
		// - port number as defined above
		GameConfig defaultConfig = new GameConfig(playerTypes, 4, 4, "Sorry!",
				PORT_NUMBER);

		// Add the default players to the configuration
		defaultConfig.addPlayer("Human", 0); // player 1: a human player
		defaultConfig.addPlayer("Computer", 1); // player 2: a computer player
		defaultConfig.addPlayer("Computer", 2); // player 3: a computer player
		defaultConfig.addPlayer("Computer", 3); // player 4: a computer player
		
		// Set the default remote-player setup:
		// - player name: "Remote Player"
		// - IP code: (empty string)
		// - default player type: human player
		defaultConfig.setRemoteData("Remote Player", "", 0);
		
		// return the configuration
		return defaultConfig;
	}//createDefaultConfig

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.sorry_xml_multi_line);
	}

	/**
	 * create a local game
	 * 
	 * @return
	 * 		the local game, a counter game
	 */
	@Override
	public LocalGame createLocalGame(GameState state) {

		return new SorryLocalGame(state);
	}



}

package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.players.GamePlayer;
import edu.up.cs301.GameFramework.actionMessage.GameAction;

/**
 * A SorryMoveAction is an action that represents a move in the game of Sorry.
 * It includes the player making the move, the pawn being moved, the card drawn,
 * and the target position of the move.
 *
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll, Corwin Carr, Annalise Walters, Kira Kunitake, Quince Pham
 * @version September 2012
 */
public class SorryMoveAction extends GameAction {
	// to satisfy the serializable interface
	private static final long serialVersionUID = 28062013L;

	// the pawn being moved
	private SorryPawn pawn;

	// the card drawn for the move
	private int card;

	// the target position of the move
	private int targetPosition;

	/**
	 * Constructor for the SorryMoveAction class.
	 *
	 * @param player the player making the move
	 * @param pawn the pawn being moved
	 * @param card the card drawn for the move
	 * @param targetPosition the target position of the move
	 */
	public SorryMoveAction(GamePlayer player, SorryPawn pawn, int card, int targetPosition) {
		super(player);
		this.pawn = pawn;
		this.card = card;
		this.targetPosition = targetPosition;
	}

	//moves the pawn from the start position ot the first position on the board
	public void moveFromStart() {
		pawn.isInStart = false;
		pawn.location = 1;
	}

	//moves the pawn forward the specified number of spaces
	public void moveForward(int spaces) {
		pawn.location += spaces;
	}

	//moves the pawn backward by the specified number of spaces
	public void moveBackward(int spaces) {
		pawn.location -= spaces;
	}

	//moves pawn to home position
	public void moveToHome() {
		pawn.isHome = true;
		pawn.location = 0;
	}

	//swaps the position of the pawn with another pawn
	public void swapWithPawn(SorryPawn otherPawn) {
		int tempLocation = pawn.location;
		pawn.location = otherPawn.location;
		otherPawn.location = tempLocation;
	}

	//moves pawn to specified target position
	public void moveToPosition() {
		pawn.location = targetPosition;
	}

	//executes move action based on the card drawn
	public void execute() {
		switch (card) {
			case 1:
				moveFromStart();
				break;
			case 2:
				moveForward(2);
				break;
			case 3:
				moveForward(3);
				break;
			case 4:
				moveBackward(4);
				break;
			case 5:
				moveForward(5);
				break;
			case 7:
				moveForward(7);
				break;
			case 8:
				moveForward(8);
				break;
			case 10:
				moveForward(10);
				break;
			case 11:
				moveForward(11);
				break;
			case 12:
				moveForward(12);
				break;
			case 0:
				moveToHome();
				break;
			// TODO: Add more cases for other card values and their corresponding actions
			default:
				break;
		}
	}
}
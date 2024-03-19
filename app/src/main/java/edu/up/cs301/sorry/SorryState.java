package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.infoMessage.GameState;


/**
 * This contains the state for the Counter game. The state consist of simply
 * the value of the counter.
 * 
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class SorryState extends GameState {

	// to satisfy Serializable interface
	private static final long serialVersionUID = 7737393762469851826L;

	private int playerTurn;

	private int pawnHomeCount;

	private int pawnStartCount;

	private SorryPawn pawn;

	private int cardNumber;


	public SorryState(SorryState gameState) {
		super();

		playerTurn = 1;

		pawnStartCount = 4;

		pawnHomeCount = 0;

		cardNumber = 0;

	}


	@Override
	public String toString(){

		 return "Player turn = " + playerTurn + " Pawn Start Count = " + pawnStartCount + " Pawn Home Count = " + pawnHomeCount + " Card Number = " + cardNumber;
}






}

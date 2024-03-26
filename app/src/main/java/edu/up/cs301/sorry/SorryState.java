package edu.up.cs301.sorry;

import edu.up.cs301.GameFramework.infoMessage.GameState;


/**
 * This contains the state for the Counter game. The state consist of simply
 * the value of the counter.
 * 
 * @author Steven R. Vegdahl, Quince Pham, Kira Kunitake, Annalise Walters, Corwin Carr
 * @version July 2013
 */
public class SorryState extends GameState {

	// to satisfy Serializable interface
	private static final long serialVersionUID = 7737393762469851826L;

	private int playerTurn;
	private int bluePawnHomeCount;
	private int redPawnHomeCount;
	private int greenPawnHomeCount;
	private int yellowPawnHomeCount;
	private int bluePawnStartCount;
	private int redPawnStartCount;
	private int greenPawnStartCount;
	private int yellowPawnStartCount;
	SorryPawn [] pawn = new SorryPawn[16];
	private int cardNumber;
	private int playerId;
	private boolean cardDrawn;
	private int movesToWin;



	public SorryState() {
		super();

		playerTurn = 1;

		bluePawnStartCount = 4;
		redPawnStartCount = 4;
		yellowPawnStartCount = 4;
		greenPawnStartCount = 4;

		bluePawnHomeCount = 0;
		redPawnHomeCount = 0;
		yellowPawnHomeCount = 0;
		greenPawnHomeCount = 0;

		for(int i = 0; i < 16; i++){
			pawn[i] = new SorryPawn();
		}

		cardNumber = 0;

		cardDrawn = false;
		movesToWin = 61;
	}

	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int s)
	{
		playerId = s;
	}
	public boolean getCardDrawn(){
		return cardDrawn;
	}
	public void setCardDrawn(boolean state){
		cardDrawn = state;
	}
	public int getBluePawnHomeCount(){
		return bluePawnHomeCount;
	}
	public void setBluePawnHomeCount(int num){
		bluePawnHomeCount = num;
	}
	public int getRedPawnHomeCount(){
		return redPawnHomeCount;
	}
	public void setRedPawnHomeCount(int num){
		redPawnHomeCount = num;
	}
	public int getYellowPawnHomeCount(){
		return yellowPawnHomeCount;
	}
	public void setYellowPawnHomeCount(int num){
		yellowPawnHomeCount = num;
	}
	public int getGreenPawnHomeCount(){
		return greenPawnHomeCount;
	}
	public void setGreenPawnHomeCount(int num){
		greenPawnHomeCount = num;
	}
	public SorryPawn getPawn(int i){
		return pawn[i];
	}

	public SorryState(SorryState orig) {
		super();
		if (orig != null) {
			this.playerTurn = orig.playerTurn;
			this.bluePawnStartCount = orig.bluePawnStartCount;
			this.redPawnStartCount = orig.redPawnStartCount;
			this.yellowPawnStartCount = orig.yellowPawnStartCount;
			this.greenPawnStartCount = orig.greenPawnStartCount;
			this.bluePawnHomeCount = orig.bluePawnHomeCount;
			this.redPawnHomeCount = orig.redPawnHomeCount;
			this.yellowPawnHomeCount = orig.yellowPawnHomeCount;
			this.greenPawnHomeCount = orig.greenPawnHomeCount;
			this.cardNumber = orig.cardNumber;
			this.cardDrawn = orig.cardDrawn;
			for (int i = 0; i < 16; i++){
				this.pawn[i] = new SorryPawn();
			}
		}
	}



	@Override
	public String toString(){

		 return "Player turn = " + playerTurn +
				 " Blue Pawn Start Count = " + bluePawnStartCount +
				 " Blue Pawn Home Count = " + bluePawnHomeCount +
				 " Red Pawn Start Count = " + redPawnStartCount +
				 " Red Pawn Home Count = " + redPawnHomeCount +
				 " Green Pawn Start Count = " + greenPawnStartCount +
				 " Green Pawn Home Count = " + greenPawnHomeCount +
				 " Yellow Pawn Start Count = " + yellowPawnStartCount +
				 " Yellow Pawn Home Count = " + yellowPawnHomeCount +
				 " Card Number = " + cardNumber;
}

}

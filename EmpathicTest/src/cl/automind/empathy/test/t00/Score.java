package cl.automind.empathy.test.t00;

public class Score {
	private final int turn;
	private final int value;
	public Score(int turn, int value){
		this.turn = turn;
		this.value = value;
	}
	public int getTurn() {
		return turn;
	}
	public int getValue() {
		return value;
	}
}

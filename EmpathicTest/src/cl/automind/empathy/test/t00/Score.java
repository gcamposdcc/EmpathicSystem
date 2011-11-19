package cl.automind.empathy.test.t00;

import cl.automind.empathy.data.sql.Column;

public class Score {
	@Column	private final int turn;
	@Column private final int value;
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

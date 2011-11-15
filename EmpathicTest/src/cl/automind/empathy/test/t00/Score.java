package cl.automind.empathy.test.t00;

import cl.automind.empathy.data.sql.Column;
import cl.automind.empathy.data.sql.Id;

public class Score {
	@Id private int id = 0;
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

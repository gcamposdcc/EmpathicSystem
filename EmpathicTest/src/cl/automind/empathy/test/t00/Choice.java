package cl.automind.empathy.test.t00;

public class Choice {
	private int playerId;
	private int turn;
	private char value;
	public Choice(int playerId, int turn, char value){
		setPlayerId(playerId);
		setTurn(turn);
		setValue(value);
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public int getTurn() {
		return turn;
	}
	public void setValue(char value) {
		this.value = value;
	}
	public char getValue() {
		return value;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public int getPlayerId() {
		return playerId;
	}
}

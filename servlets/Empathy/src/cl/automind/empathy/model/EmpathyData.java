package cl.automind.empathy.model;

public class EmpathyData {
	private int id;
	private int idSource;
	private int idType;
	private boolean evaluated;
	private boolean answered;
	private boolean liked;
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setIdSource(int idSource) {
		this.idSource = idSource;
	}
	public int getIdSource() {
		return idSource;
	}
	public void setIdType(int idType) {
		this.idType = idType;
	}
	public int getIdType() {
		return idType;
	}
	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}
	public boolean isEvaluated() {
		return evaluated;
	}
	public void setAnswered(boolean answered) {
		this.answered = answered;
	}
	public boolean isAnswered() {
		return answered;
	}
	public void setLiked(boolean liked) {
		this.liked = liked;
	}
	public boolean isLiked() {
		return liked;
	}
	
	
}

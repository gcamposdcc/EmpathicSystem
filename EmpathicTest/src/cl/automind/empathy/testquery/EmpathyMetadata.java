package cl.automind.empathy.testquery;

import java.sql.Date;

import cl.automind.empathy.data.sql.Column;
import cl.automind.empathy.data.sql.Id;

public class EmpathyMetadata {
	@Id
	private int id;
	@Column(name = "created_at")
	private Date createdAt;
	@Column
	private int idsource;
	@Column
	private int idtype;
	@Column
	private boolean evaluated;
	@Column
	private boolean answered;
	@Column
	private boolean liked;

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setIdsource(int idsource) {
		this.idsource = idsource;
	}
	public int getIdsource() {
		return idsource;
	}
	public void setIdtype(int idtype) {
		this.idtype = idtype;
	}
	public int getIdtype() {
		return idtype;
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
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
}
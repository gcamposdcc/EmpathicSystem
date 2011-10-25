package cl.automind.empathy.data;

public interface IIndicator {
	public boolean canEvaluate();
	public boolean needsReevaluation();
	public boolean evaluate();
	public Object getValue();
}

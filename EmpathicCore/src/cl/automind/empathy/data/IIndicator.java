package cl.automind.empathy.data;

public interface IIndicator {
	boolean canEvaluate();
	boolean needsReevaluation();
	boolean evaluate();
	Object getValue();
}

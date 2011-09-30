package cl.automind.empathy.rule;

import cl.automind.empathy.feedback.AbstractMessage;
import cl.automind.empathy.feedback.EmptyMessage;

public final class EmptyRule extends AbstractRule {
	public final static AbstractRule instance = new EmptyRule();
	private EmptyRule(){ super(); }
	@Override
	public double evaluateImpl() {
		return 0;
	}

	@Override
	public boolean canEvaluate(Object... params) {
		return false;
	}
	@Override
	public AbstractMessage getMessage() {
		return new EmptyMessage();
	}

}

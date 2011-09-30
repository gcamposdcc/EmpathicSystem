package cl.automind.empathy.rule;

import java.util.Map;

import cl.automind.empathy.feedback.AbstractMessage;

public interface IRule {

	public static final int NON_EXECUTABLE = -1;

	// <metadata-fields-getters>
	public abstract String getName();

	public abstract String[] getStrategies();

	public abstract boolean hasStrategy(String strategyName);

	// </metadata-fields-getters>
	/**
	 * Determinates the priority of this rule.
	 * @return
	 */
	public abstract double evaluateImpl();

	public abstract double evaluate(Object... params);

	public abstract boolean canEvaluate(Object... params);

	public abstract boolean isSelectable();

	public abstract AbstractMessage getMessage();

	public abstract void setMessage(AbstractMessage message);

	public abstract Map<String, Object> getValuesMap();

	public abstract void clearValues();

	public abstract void putValue(String key, Object value);

	public abstract void removeValue(String key);

	public abstract void setDataMediator(DataRuleMediator dataMediator);

	public abstract DataRuleMediator getDataMediator();

	public abstract double getValue();

}
package cl.automind.empathy.rule;

import java.util.Map;

import cl.automind.empathy.feedback.AbstractMessage;

public interface IRule {

	int NON_EXECUTABLE = -1;

	// <metadata-fields-getters>
	String getName();

	String[] getStrategies();

	boolean hasStrategy(String strategyName);

	// </metadata-fields-getters>
	/**
	 * Determinates the priority of this rule.
	 * @return
	 */
	double evaluateImpl(Object...params);

	double evaluate(Object... params);

	boolean canEvaluate(Object... params);

	boolean isSelectable();

	AbstractMessage getMessage();

	void setMessage(AbstractMessage message);

	Map<String, Object> getValuesMap();

	void clearValues();

	void putValue(String key, Object value);

	void removeValue(String key);

	void setDataMediator(DataRuleMediator dataMediator);

	DataRuleMediator getDataMediator();

	double getValue();

	void setParams(Object...params);

	Object[] getParams();

}
package cl.automind.empathy.rule;

import java.util.Map;

import cl.automind.empathy.feedback.AbstractMessage;

public interface IRule {

	int NON_EXECUTABLE = -1;

	// <metadata-fields-getters>
	String getName();

	String[] getStrategies();

	boolean hasStrategy(String strategyName);

	double evaluate(Object... params);

	boolean canEvaluate(Object... params);

	boolean isSelectable();

	AbstractMessage getMessage();

	void setMessage(AbstractMessage message);

	Map<String, Object> getMessageValuesMap();

	void clearMessageValues();

	void putMessageValue(String key, Object value);

	void removeMessageValue(String key);

	void setDataMediator(DataRuleMediator dataMediator);

	DataRuleMediator getDataMediator();

	double getLastEvaluationValue();

	void setParams(Object...params);

	Object[] getParams();

}
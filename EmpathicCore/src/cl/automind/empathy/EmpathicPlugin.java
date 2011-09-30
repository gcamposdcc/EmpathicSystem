package cl.automind.empathy;

import java.util.Collection;

import cl.automind.empathy.data.AbstractDataManager;
import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.feedback.AbstractEmotion;
import cl.automind.empathy.rule.AbstractRule;
import cl.automind.empathy.rule.AbstractRuleManager;
import cl.automind.empathy.ui.IUiManager;

public abstract class EmpathicPlugin {
	public abstract AbstractRuleManager getRuleManager();
	public abstract AbstractDataManager getDataManager();
	public abstract IUiManager getUiManager();

	public abstract AbstractArbiter getArbiter();
	public abstract ArbiterCriterion getCriterion();

	public abstract Collection<AbstractRule> getRules();
	public abstract Collection<AbstractEmotion> getEmotions();

	public abstract Collection<IDataSource<?>> getDataSources();

}

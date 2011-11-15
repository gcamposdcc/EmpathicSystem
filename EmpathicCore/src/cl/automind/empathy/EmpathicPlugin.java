package cl.automind.empathy;

import java.util.Collection;

import cl.automind.empathy.data.AbstractDataManager;
import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.feedback.AbstractEmotion;
import cl.automind.empathy.rule.AbstractRule;
import cl.automind.empathy.rule.IRuleManager;
import cl.automind.empathy.ui.IUiManager;

public abstract class EmpathicPlugin {
	public abstract IRuleManager getRuleManager();
	public abstract AbstractDataManager getDataManager();
	public abstract IUiManager getUiManager();

	public abstract IArbiter getArbiter();
	public abstract IArbiterCriterion getCriterion();

	public abstract Collection<AbstractRule> getRules();
	public abstract Collection<AbstractEmotion> getEmotions();

	public abstract Collection<IDataSource<?>> getDataSources();

}

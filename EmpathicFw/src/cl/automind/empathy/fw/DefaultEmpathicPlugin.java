package cl.automind.empathy.fw;

import java.util.ArrayList;
import java.util.Collection;

import cl.automind.empathy.AbstractEmpathicPlugin;
import cl.automind.empathy.IArbiter;
import cl.automind.empathy.IArbiterCriterion;
import cl.automind.empathy.data.IDataManager;
import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.feedback.AbstractEmotion;
import cl.automind.empathy.fw.arbiter.DefaultArbiter;
import cl.automind.empathy.fw.arbiter.LessUsedCriterion;
import cl.automind.empathy.fw.data.DefaultDataManager;
import cl.automind.empathy.fw.rule.DefaultRuleManager;
import cl.automind.empathy.fw.ui.DefaultUiManager;
import cl.automind.empathy.rule.IRule;
import cl.automind.empathy.rule.IRuleManager;
import cl.automind.empathy.ui.IUiManager;

public class DefaultEmpathicPlugin extends AbstractEmpathicPlugin {
	private final IArbiter arbiter = new DefaultArbiter();
	private final IArbiterCriterion criterion = new LessUsedCriterion();

	private final IRuleManager ruleManager = new DefaultRuleManager();
	private final IDataManager dataManager = new DefaultDataManager();
	private final IUiManager uiManager = new DefaultUiManager();

	@Override
	public IRuleManager getRuleManager() {
		return ruleManager;
	}

	@Override
	public IDataManager getDataManager() {
		return dataManager;
	}

	@Override
	public IUiManager getUiManager() {
		return uiManager;
	}

	@Override
	public IArbiter getArbiter() {
		return arbiter;
	}

	@Override
	public IArbiterCriterion getCriterion() {
		return criterion;
	}

	@Override
	public Collection<IRule> getRules() {
		return new ArrayList<IRule>();
	}

	@Override
	public Collection<AbstractEmotion> getEmotions() {
		return new ArrayList<AbstractEmotion>();
	}

	@Override
	public Collection<IDataSource<?>> getDataSources() {
		return new ArrayList<IDataSource<?>>();
	}

}

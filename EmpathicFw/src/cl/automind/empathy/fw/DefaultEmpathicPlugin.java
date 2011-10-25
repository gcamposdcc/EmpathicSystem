package cl.automind.empathy.fw;

import java.util.ArrayList;
import java.util.Collection;

import cl.automind.empathy.ArbiterCriterion;
import cl.automind.empathy.EmpathicPlugin;
import cl.automind.empathy.IArbiter;
import cl.automind.empathy.data.AbstractDataManager;
import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.feedback.AbstractEmotion;
import cl.automind.empathy.fw.arbiter.DefaultArbiter;
import cl.automind.empathy.fw.arbiter.LessUsedCriterion;
import cl.automind.empathy.fw.data.DefaultDataManager;
import cl.automind.empathy.fw.rule.DefaultRuleManager;
import cl.automind.empathy.fw.ui.DefaultUiManager;
import cl.automind.empathy.rule.AbstractRule;
import cl.automind.empathy.rule.IRuleManager;
import cl.automind.empathy.ui.IUiManager;

public class DefaultEmpathicPlugin extends EmpathicPlugin {
	private final IArbiter arbiter = new DefaultArbiter();
	private final ArbiterCriterion criterion = new LessUsedCriterion();

	private final IRuleManager ruleManager = new DefaultRuleManager();
	private final AbstractDataManager dataManager = new DefaultDataManager();
	private final IUiManager uiManager = new DefaultUiManager();

	@Override
	public IRuleManager getRuleManager() {
		return ruleManager;
	}

	@Override
	public AbstractDataManager getDataManager() {
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
	public ArbiterCriterion getCriterion() {
		return criterion;
	}

	@Override
	public Collection<AbstractRule> getRules() {
		return new ArrayList<AbstractRule>();
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

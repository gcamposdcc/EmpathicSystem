package cl.automind.empathy.test.t00;

import java.util.Collection;

import cl.automind.empathy.data.AbstractDataManager;
import cl.automind.empathy.fw.DefaultEmpathicPlugin;
import cl.automind.empathy.rule.AbstractRule;

public class TestPlugin extends DefaultEmpathicPlugin {
	private AbstractRule[] rules = {
		new ScoreStreakRule(),
		new TotalScoreRule()
	};
	private AbstractDataManager dataManager = new TestDataManager();
	@Override
	public AbstractDataManager getDataManager() {
		return dataManager;
	}
	@Override
	public Collection<AbstractRule> getRules() {
		Collection<AbstractRule> newRules = super.getRules();
		for(AbstractRule rule: rules){
			newRules.add(rule);
		}
		return newRules;
	}
}

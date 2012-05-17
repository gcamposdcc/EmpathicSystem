package cl.automind.empathy.test.t00;

import java.util.Collection;

import cl.automind.empathy.data.IDataManager;
import cl.automind.empathy.fw.DefaultEmpathicPlugin;
import cl.automind.empathy.rule.AbstractRule;
import cl.automind.empathy.rule.IRule;

public class TestPlugin extends DefaultEmpathicPlugin {
	private AbstractRule[] rules = {
		new ScoreStreakRule(),
		new TotalScoreRule()
	};
	private IDataManager dataManager = new TestDataManager();
	@Override
	public IDataManager getDataManager() {
		return dataManager;
	}
	@Override
	public Collection<IRule> getRules() {
		Collection<IRule> newRules = super.getRules();
		for(IRule rule: rules){
			newRules.add(rule);
		}
		return newRules;
	}
}

package cl.automind.empathy.rule;

import java.util.List;

import cl.automind.empathy.data.IDataManager;
import cl.automind.empathy.data.IQueryCriterion;
import cl.automind.empathy.data.IQueryOption;

public class DataRuleMediator {
	private IDataManager dataManager;
	private IRuleManager ruleManager;
	private final void setDataManager(IDataManager dataManager) {
		this.dataManager = dataManager;
	}
	private final IDataManager getDataManager() {
		return dataManager;
	}
	private final void setRuleManager(IRuleManager ruleManager) {
		this.ruleManager = ruleManager;
	}
	@SuppressWarnings("unused")
	private final IRuleManager getRuleManager() {
		return ruleManager;
	}
	public DataRuleMediator(IDataManager dataManager, IRuleManager ruleManager){
		setDataManager(dataManager);
		setRuleManager(ruleManager);
	}
	public <T> List<T> getValue(String dataSourceName, IQueryOption option, IQueryCriterion<T>... criteria) {
		return getDataManager().getValue(dataSourceName, option, criteria);
	}
	public int countElements(String dataSourceName) {
		return getDataManager().countElements(dataSourceName);
	}
	public <T> List<T> getAllInSource(String dataSourceName, T template){
		return getDataManager().getAll(dataSourceName, template);
	}
}

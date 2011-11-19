package cl.automind.empathy.rule;

import java.util.List;

import cl.automind.empathy.data.IDataManager;
import cl.automind.empathy.data.IQueryCriterion;
import cl.automind.empathy.data.IQueryOption;

public class DataRuleMediator {
	private IDataManager dataManager;
	private IRuleManager ruleManager;
	protected void setDataManager(IDataManager dataManager) {
		this.dataManager = dataManager;
	}
	protected IDataManager getDataManager() {
		return dataManager;
	}
	protected void setRuleManager(IRuleManager ruleManager) {
		this.ruleManager = ruleManager;
	}
	protected IRuleManager getRuleManager() {
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

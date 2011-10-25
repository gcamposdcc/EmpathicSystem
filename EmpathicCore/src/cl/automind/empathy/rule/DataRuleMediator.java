package cl.automind.empathy.rule;

import java.util.List;

import cl.automind.empathy.data.AbstractDataManager;

public class DataRuleMediator {
	private AbstractDataManager dataManager;
	private IRuleManager ruleManager;
	protected void setDataManager(AbstractDataManager dataManager) {
		this.dataManager = dataManager;
	}
	protected AbstractDataManager getDataManager() {
		return dataManager;
	}
	protected void setRuleManager(IRuleManager ruleManager) {
		this.ruleManager = ruleManager;
	}
	protected IRuleManager getRuleManager() {
		return ruleManager;
	}
	public DataRuleMediator(AbstractDataManager dataManager, IRuleManager ruleManager){
		setDataManager(dataManager);
		setRuleManager(ruleManager);
	}
	public Object getValueById(String dataSourceName, int id) {
		return getDataManager().getValueById(dataSourceName, id);
	}
	public int countElements(String dataSourceName) {
		return getDataManager().countElements(dataSourceName);
	}
	public <T> List<T> getAllInSource(String dataSourceName, T template){
		return getDataManager().getAll(dataSourceName, template);
	}
}

package cl.automind.empathy;

import cl.automind.empathy.data.IDataManager;
import cl.automind.empathy.rule.AbstractRuleManager;
import cl.automind.empathy.rule.IRuleManager;
import cl.automind.empathy.ui.IUiManager;
/**
 * This is class is responsible for the manager containment and
 * administration, as such it should provide the getters and setters
 * to access them and the methods required for their initialization.
 * This class should only be used paired with a {@link EmpathicKernel}
 * @author Guillermo
 */
public final class EmpathicManagers {
	/**
	 * The manager responsible of the data management.
	 * @see AbstractDataManager
	 */
	private IDataManager dataManager;
	/**
	 * The manager responsible of the rule management.
	 * @see AbstractRuleManager
	 */
	private IRuleManager ruleManager;
	/**
	 * The manager responsible of the ui operations.
	 * @see AbstractUiManager
	 */
	private IUiManager uiManager;

	// GETTERS AND SETTERS
	public void setDataManager(IDataManager dataManager) {
		this.dataManager = dataManager;
	}

	public IDataManager getDataManager() {
		return dataManager;
	}

	public void setUiManager(IUiManager uiManager) {
		this.uiManager = uiManager;
	}

	public IUiManager getUiManager() {
		return uiManager;
	}

	public void setRuleManager(IRuleManager ruleManager) {
		this.ruleManager = ruleManager;
	}

	public IRuleManager getRuleManager() {
		return ruleManager;
	}
}

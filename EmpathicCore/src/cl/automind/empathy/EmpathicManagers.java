package cl.automind.empathy;

import cl.automind.empathy.data.IDataManager;
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
	 * @see IDataManager
	 */
	private IDataManager dataManager;
	/**
	 * The manager responsible of the rule management.
	 * @see IRuleManager
	 */
	private IRuleManager ruleManager;
	/**
	 * The manager responsible of the ui operations.
	 * @see IUiManager
	 */
	private IUiManager uiManager;

	// GETTERS AND SETTERS
	/**
	 * Setter for the data manager
	 * @param dataManager the new data manager
	 * @see IDataManager
	 */
	public void setDataManager(IDataManager dataManager) {
		this.dataManager = dataManager;
	}

	/**
	 * Getter for the data manager
	 * @see IDataManager
	 */
	public IDataManager getDataManager() {
		return dataManager;
	}

	/**
	 * Setter for the user interface manager
	 * @param uiManager the new user interface manager
	 * @see IUiManager
	 */
	public void setUiManager(IUiManager uiManager) {
		this.uiManager = uiManager;
	}

	/**
	 * Getter for the user interface manager
	 * @see IUiManager
	 */
	public IUiManager getUiManager() {
		return uiManager;
	}

	/**
	 * Setter for the rule manager
	 * @param ruleManager the new rule manager
	 * @see IRuleManager
	 */
	public void setRuleManager(IRuleManager ruleManager) {
		this.ruleManager = ruleManager;
	}

	/**
	 * Getter for the rule manager
	 * @see IRuleManager
	 */
	public IRuleManager getRuleManager() {
		return ruleManager;
	}
}

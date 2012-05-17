package cl.automind.empathy;

import java.util.Collection;

import cl.automind.empathy.data.IDataManager;
import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.feedback.AbstractEmotion;
import cl.automind.empathy.rule.IRule;
import cl.automind.empathy.rule.IRuleManager;
import cl.automind.empathy.ui.IUiManager;
/**
 * The extensibility and mutability of this library is oriented to
 * class aggregation. To make this all new elements are added via
 * plugins (even the elements for default execution). This class is responsible
 * of containing the new elements.
 * Almost anything of the default execution can be replaced using plugins.
 * The new elements are accessed using the different getters.
 * Correct usage of this class is done via extension.
 * For example:
 * If attempting to replace the default {@link IRuleManager}
 *
 * @author Guillermo
 */
public abstract class AbstractEmpathicPlugin {
	public abstract IRuleManager getRuleManager();
	public abstract IDataManager getDataManager();
	public abstract IUiManager getUiManager();

	public abstract IArbiter getArbiter();
	public abstract IArbiterCriterion getCriterion();

	public abstract Collection<IRule> getRules();
	public abstract Collection<AbstractEmotion> getEmotions();

	public abstract Collection<IDataSource<?>> getDataSources();

}

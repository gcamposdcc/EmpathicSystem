package cl.automind.empathy;

import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;

import cl.automind.empathy.data.IDataManager;
import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.feedback.AbstractEmotion;
import cl.automind.empathy.feedback.AbstractMessage;
import cl.automind.empathy.feedback.ForcedMessage;
import cl.automind.empathy.rule.DataRuleMediator;
import cl.automind.empathy.rule.EmptyRule;
import cl.automind.empathy.rule.IRule;
import cl.automind.empathy.rule.IRuleManager;
import cl.automind.empathy.ui.IUiManager;

/**
 * The Core of the Engine, this class is used as a Facade for common
 * engine operation, as such it should not contain any logic but the
 * necesary to make calls to the managers contained in the
 * {@link EmpathicManagers}
 * @author Guillermo
 * @see EmpathicManagers
 */
public class EmpathicKernel {

	public EmpathicKernel(){
		setManagers(new EmpathicManagers());
		loadEmpathy();
		getManagers().getRuleManager().registerRule(EmptyRule.INSTANCE.getName(), EmptyRule.INSTANCE);
		startEmpathy();
	}
	// <arbiter>
	/**
	 * The arbiter responsible of the {@link IRule} selection and priorization.
	 */
	private IArbiter arbiter;
	/**
	 * Sets the arbiter for the current {@link EmpathicKernel}
	 * @param arbiter the new arbiter
	 */
	public final void setArbiter(IArbiter arbiter) {
		this.arbiter = arbiter;
	}
	/**
	 * Gets the current arbiter
	 * @return the current arbiter
	 */
	public final IArbiter getArbiter() {
		return arbiter;
	}
	// </arbiter>
	// <data-rule-mediator>
	/**
	 * The mediator between {@link IDataSource}s and {@link IRule}s.
	 */
	private DataRuleMediator dataRuleMediator;
	/**
	 * Sets the current data-rule mediator
	 * @param dataRuleMediator the new data-rule mediator
	 */
	private final void setDataRuleMediator(DataRuleMediator dataRuleMediator) {
		this.dataRuleMediator = dataRuleMediator;
	}
	/**
	 * Gets the current data-rule mediator
	 * @return the current data-rule mediator
	 */
	private final DataRuleMediator getDataRuleMediator() {
		return dataRuleMediator;
	}
	// </data-rule-mediator>
	// <managers>
	/**
	 * The containter of the different managers.
	 */
	private EmpathicManagers managers;
	/**
	 * Sets the manager container
	 * @param managers the new manager container
	 */
	private final void setManagers(EmpathicManagers managers) {
		this.managers = managers;
	}
	/**
	 * Gets the current manager container
	 * @return the current manager container
	 */
	protected final EmpathicManagers getManagers() {
		return managers;
	}
	// </managers>

	public void showEmpathy(){
		IRule rule = getArbiter().getValidRule();
		if (rule!= null && rule != EmptyRule.INSTANCE){
			showEmpathy(rule.getMessage());
		}
	}
	public void showEmpathy(AbstractMessage message){
		getManagers().getUiManager().displayMessage(message);
	}
	public void showEmpathy(String message){
		getManagers().getUiManager().displayMessage(new ForcedMessage(message));
	}
	public void showEmpathy(AbstractMessage message, long timeout){
		getManagers().getUiManager().displayMessage(message);
		try { Thread.sleep(timeout); }
		catch (Exception e){ Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage()); }
		finally { hideEmpathy(); }
	}
	public final void hideEmpathy(){
		getManagers().getUiManager().hideCurrentMessage();
	}
	public final void loadEmpathy(){
		Collection<AbstractEmpathicPlugin> plugins = loadPluginClasses();
		for (AbstractEmpathicPlugin plugin: plugins){
			deployPluginArbiter(plugin);
		}
		for (AbstractEmpathicPlugin plugin: plugins){
			deployPluginManagers(plugin);
		}
		setDataRuleMediator(new DataRuleMediator(getManagers().getDataManager(), getManagers().getRuleManager()));
		for (AbstractEmpathicPlugin plugin: plugins){
			deployPluginCriterion(plugin);
		}
		for (AbstractEmpathicPlugin plugin: plugins){
			deployPluginContent(plugin);
		}
	}
	private final void deployPluginArbiter(AbstractEmpathicPlugin plugin) {
		IArbiter arbiter = plugin.getArbiter();
		if (arbiter != null){
			arbiter.setEmpathicKernel(this);
			setArbiter(arbiter);
		}
	}
	private final void deployPluginManagers(AbstractEmpathicPlugin plugin) {
		IDataManager dataManager = plugin.getDataManager();
		IRuleManager ruleManager = plugin.getRuleManager();
		IUiManager uiManager = plugin.getUiManager();
		if (dataManager != null) {
			getManagers().setDataManager(dataManager);
		}
		if (ruleManager != null) {
			getManagers().setRuleManager(ruleManager);
		}
		if (uiManager != null) {
			getManagers().setUiManager(uiManager);
		}

	}
	private final void deployPluginCriterion(AbstractEmpathicPlugin plugin) {
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine("Load::Criterion::"+plugin.getCriterion());
		if (plugin.getCriterion() != null) {
			getArbiter().setCriterion(plugin.getCriterion());
		}
	}
	private final void deployPluginContent(AbstractEmpathicPlugin plugin) {
		Collection<IRule> rules = plugin.getRules();
		Collection<AbstractEmotion> emotions = plugin.getEmotions();
		for (IRule rule: rules){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine("Load::Rule::"+rule);
			rule.setDataMediator(getDataRuleMediator());
			getManagers().getRuleManager().registerRule(rule.getName(), rule);
		}
		for (AbstractEmotion emotion: emotions){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine("Load::Emotion::"+emotion);
			getManagers().getUiManager().registerEmotion(emotion.getName(), emotion);
		}
		for (IDataSource<?> dataSource : plugin.getDataSources()){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine("Load::Source::"+dataSource.getName());
			getManagers().getDataManager().registerDataSource(dataSource.getName(), dataSource);
		}
	}
	public Collection<AbstractEmpathicPlugin> loadPluginClasses(){
		return new java.util.ArrayList<AbstractEmpathicPlugin>();
	}
	public void startEmpathy(){

	}

	public <T> int pushValue(String dataSourceName, T value){
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine("PushTo::"+dataSourceName+"::Value::"+value);
		return getManagers().getDataManager().pushValue(dataSourceName, value);
	}

	public void registerRule(IRule rule){
		getManagers().getRuleManager().registerRule(rule.getName(), rule);
	}

	public IRule getRule(String rulename){
		return getManagers().getRuleManager().getRule(rulename);
	}

	public Set<String> getAllRuleNames(){
		return getManagers().getRuleManager().getAllRulenames();
	}

	public void triggerEmpathy(){
		showEmpathy(getArbiter().getValidRule().getMessage());
	}
	public void triggerEmpathy(long millis){
		showEmpathy(getArbiter().getValidRule().getMessage(), millis);
	}

	public void putParams(String ruleName, Object... params){
		IRule rule = getManagers().getRuleManager().getRule(ruleName);
		if(rule != null) rule.setParams(params);
	}

}

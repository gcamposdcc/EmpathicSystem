package cl.automind.empathy;

import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
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
 * engine operation. Any subclass should not contain any logic but the
 * necesary to make calls to the managers contained in the
 * {@link EmpathicManagers}
 * @author Guillermo
 * @see EmpathicManagers
 */
public class EmpathicKernel {

	/**
	 * Standard constructor initializes the {@link EmpathicManagers} responsible for 
	 * managers management.
	 */
	public EmpathicKernel(){
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.WARNING);
		setManagers(new EmpathicManagers());
	}
	/**
	 * Lock for multiple {@code initEmpathy()} method calls;
	 */
	private volatile boolean inited = false;
	/**
	 * Starts up this {@link EmpathicKernel}. This method is executed only once and 
	 * following calls make no effect. This method calls the {@code onPostLoad()} methods upon ending.
	 */
	protected final void initEmpathy(){
		if (inited) return; 
		inited = true;
		loadEmpathy();
		getManagers().getRuleManager().registerRule(EmptyRule.INSTANCE.getName(), EmptyRule.INSTANCE);
		onPostLoad();
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
	protected final void setManagers(EmpathicManagers managers) {
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
	/**
	 * Start a request for a feedback message for the user.
	 * If the conditions for presenting a message are met, then an {@link AbstractMessage} is shown,
	 * otherwise, no message will be shown.
	 */
	public void showEmpathy(){
		IRule rule = getArbiter().getValidRule();
		if (rule!= null && rule != EmptyRule.INSTANCE){
			showEmpathy(rule.getMessage());
		}
	}
	/**
	 * Uses an {@link AbstractMessage} to display feedback to the user. Usage of this method
	 * overrides the filter of this {@link EmpathicKernel}, skipping all the evaluation of 
	 * {@link IRule}s.
	 * @param message The message to be shown
	 */
	public void showEmpathy(AbstractMessage message){
		getManagers().getUiManager().displayMessage(message);
	}
	/**
	 * Uses an custom text to display feedback to the user. Usage of this method
	 * overrides the filter of this {@link EmpathicKernel}, skipping all the evaluation of 
	 * {@link IRule}s. Instead of using an {@link AbstractMessage} as base, an instance of
	 * {@link ForcedMessage} is created to trigger the call.
	 * @param message The message to be shown
	 * @see {@link ForcedMessage}
	 */
	public void showEmpathy(String message){
		getManagers().getUiManager().displayMessage(new ForcedMessage(message));
	}
	/**
	 * Uses an {@link AbstractMessage} to display feedback to the user. Usage of this method
	 * overrides the filter of this {@link EmpathicKernel}, skipping all the evaluation of 
	 * {@link IRule}s. After <b>timeout</b> milliseconds a call to the {@code hideMessage()}
	 * method is triggered.
	 * @param message The message to be shown
	 * @param timeout The delay in milliseconds before hiding the message
	 */
	public void showEmpathy(AbstractMessage message, long timeout){
		getManagers().getUiManager().displayMessage(message);
		try { Thread.sleep(timeout); }
		catch (Exception e){ Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage()); }
		finally { hideEmpathy(); }
	}
	/**
	 * Hides the feedback that is currently beeing shown (if any).
	 */
	public final void hideEmpathy(){
		getManagers().getUiManager().hideCurrentMessage();
	}
	/**
	 * Loads all the {@link AbstractEmpathicPlugin}s.
	 */
	private final void loadEmpathy(){
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
	/**
	 * Extract the {@link IArbiter} contained in the plugin and sets it as this
	 * {@link EmpathicKernel} arbiter.
	 * @param plugin The {@link IArbiter} source
	 */
	private final void deployPluginArbiter(AbstractEmpathicPlugin plugin) {
		IArbiter arbiter = plugin.getArbiter();
		if (arbiter != null){
			arbiter.setEmpathicKernel(this);
			setArbiter(arbiter);
		}
	}
	/**
	 * Extract the different managers contained in the plugin and sets it as this
	 * {@link EmpathicKernel} managers
	 * @param plugin The managers source
	 */
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
	/**
	 * Extract the {@link IArbiterCriterion} contained in the plugin and sets it as this
	 * {@link EmpathicKernel} criterion for selecting rule.
	 * @param plugin The {@link IArbiterCriterion} source
	 */
	private final void deployPluginCriterion(AbstractEmpathicPlugin plugin) {
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine("Load::Criterion::"+plugin.getCriterion());
		if (plugin.getCriterion() != null) {
			getArbiter().setCriterion(plugin.getCriterion());
		}
	}
	/**
	 * Extract the different {@link IDataSource}s and {@link IRule}s contained in the plugin and sets it as this
	 * {@link EmpathicKernel} content
	 * @param plugin The contents source
	 */
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
	/**
	 * Returns a Collection of {@link AbstractEmpathicPlugin} that will be loaded and used
	 * as this {@link EmpathicKernel}, arbiter, criterion, managers and content.
	 * @return a Collection of the {@link AbstractEmpathicPlugin} that will be loaded
	 */
	public Collection<AbstractEmpathicPlugin> loadPluginClasses(){
		return new java.util.ArrayList<AbstractEmpathicPlugin>();
	}
	/**
	 * Called at the end of the {@code initEmpathy()} method.
	 * The default implementation does nothing.
	 */
	public void onPostLoad(){

	}
	/**
	 * Inserts the {@code value} of type {@code T} in the {@link IDataSource} of name
	 * {@code dataSourceName}
	 * @param dataSourceName The target datasource name
	 * @param value The value to be inserted
	 * @return an id value
	 */
	public <T> int pushValue(String dataSourceName, T value){
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine("PushTo::"+dataSourceName+"::Value::"+value);
		return getManagers().getDataManager().pushValue(dataSourceName, value);
	}
	/**
	 * Adds the {@link IRule} to the current set of rules.
	 * @param rule The new rule to be added
	 */
	public void registerRule(IRule rule){
		getManagers().getRuleManager().registerRule(rule.getName(), rule);
	}
	/**
	 * Returns the {@link IRule} registered with the name {@code rulename}.
	 * If no rule has been registered with that name it return <b>null</b>
	 * @param rulename The name of the rule to be searched
	 * @return the rule registered with the rulename, <b>null</b> if not rule with
	 * that name has been registered
	 */
	public IRule getRule(String rulename){
		return getManagers().getRuleManager().getRule(rulename);
	}
	/**
	 * Returns a set with all the names of the rules registered in this
	 * {@link EmpathicKernel}.
	 * @return a Set with all the names of the rules registered
	 */
	public Set<String> getAllRuleNames(){
		return getManagers().getRuleManager().getAllRulenames();
	}
	/**
	 * Alias for the method {@code showEmpathy()}
	 */
	public void triggerEmpathy(){
		showEmpathy();
	}
	/**
	 * Calls the method {@code showEmpathy()} and hides the message in 
	 * {@code timeout} milliseconds
	 * @param timeout The milliseconds that the message will be shown
	 */
	public void triggerEmpathy(long timeout){
		showEmpathy(getArbiter().getValidRule().getMessage(), timeout);
	}
	/**
	 * Puts an array of parameters for the execution of the {@link IRule} 
	 * registered with name {@code rulename}. If no rule is registered with
	 * the given name, this method does nothing.
	 * @param ruleName The rule that will use the params
	 * @param params The array of parameters for the rule
	 */
	public void putParams(String ruleName, Object... params){
		IRule rule = getManagers().getRuleManager().getRule(ruleName);
		if(rule != null) rule.setParams(params);
	}

}

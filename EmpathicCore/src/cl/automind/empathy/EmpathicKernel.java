package cl.automind.empathy;

import java.util.Collection;
import java.util.Set;

import cl.automind.empathy.data.IDataManager;
import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.feedback.AbstractEmotion;
import cl.automind.empathy.feedback.AbstractMessage;
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
public abstract class EmpathicKernel {

	public EmpathicKernel(){
		setManagers(new EmpathicManagers());
		loadEmpathy();
		getManagers().getRuleManager().registerRule(EmptyRule.instance.getName(), EmptyRule.instance);
		startEmpathy();
	}
	// <arbiter>
	private IArbiter arbiter;
	public void setArbiter(IArbiter arbiter) {
		this.arbiter = arbiter;
	}
	public IArbiter getArbiter() {
		return arbiter;
	}
	// </arbiter>
	// <data-rule-mediator>
	private DataRuleMediator dataRuleMediator;
	private void setDataRuleMediator(DataRuleMediator dataRuleMediator) {
		this.dataRuleMediator = dataRuleMediator;
	}
	private DataRuleMediator getDataRuleMediator() {
		return dataRuleMediator;
	}
	// </data-rule-mediator>
	// <managers>
	private EmpathicManagers managers;
	private void setManagers(EmpathicManagers managers) {
		this.managers = managers;
	}
	protected EmpathicManagers getManagers() {
		return managers;
	}
	// </managers>

	public void showEmpathy(){
		IRule rule = getArbiter().getValidRule();
		if (rule!= null && rule != EmptyRule.instance){
			showEmpathy(rule.getMessage());
		}
	}
	public void showEmpathy(AbstractMessage message){
		getManagers().getUiManager().displayMessage(message);
	}
	public void showEmpathy(AbstractMessage message, long timeout){
		getManagers().getUiManager().displayMessage(message);
		try { Thread.sleep(timeout); }
		catch (Exception e){ }
		finally { hideEmpathy(); }
	}
	public void hideEmpathy(){
		getManagers().getUiManager().hideCurrentMessage();
	}
	public final void loadEmpathy(){
		Collection<EmpathicPlugin> plugins = loadPluginClasses();
		for (EmpathicPlugin plugin: plugins){
			deployPluginArbiter(plugin);
		}
		for (EmpathicPlugin plugin: plugins){
			deployPluginManagers(plugin);
		}
		setDataRuleMediator(new DataRuleMediator(getManagers().getDataManager(), getManagers().getRuleManager()));
		for (EmpathicPlugin plugin: plugins){
			deployPluginCriterion(plugin);
		}
		for (EmpathicPlugin plugin: plugins){
			deployPluginContent(plugin);
		}
	}
	private void deployPluginArbiter(EmpathicPlugin plugin) {
		IArbiter arbiter = plugin.getArbiter();
		if (arbiter != null){
			arbiter.setEmpathicKernel(this);
			setArbiter(arbiter);
		}
	}
	private final void deployPluginManagers(EmpathicPlugin plugin) {
		IDataManager dataManager = plugin.getDataManager();
		IRuleManager ruleManager = plugin.getRuleManager();
		IUiManager uiManager = plugin.getUiManager();
		if (dataManager != null) getManagers().setDataManager(dataManager);
		if (ruleManager != null) getManagers().setRuleManager(ruleManager);
		if (uiManager != null) getManagers().setUiManager(uiManager);

	}
	private void deployPluginCriterion(EmpathicPlugin plugin) {
		System.out.println("Load::Criterion::"+plugin.getCriterion());
		if (plugin.getCriterion() != null) getArbiter().setCriterion(plugin.getCriterion());
	}
	private final void deployPluginContent(EmpathicPlugin plugin) {
		Collection<IRule> rules = plugin.getRules();
		Collection<AbstractEmotion> emotions = plugin.getEmotions();
		for (IRule rule: rules){
			System.out.println("Load::Rule::"+rule);
			rule.setDataMediator(getDataRuleMediator());
			getManagers().getRuleManager().registerRule(rule.getName(), rule);
		}
		for (AbstractEmotion emotion: emotions){
			System.out.println("Load::Emotion::"+emotion);
			getManagers().getUiManager().registerEmotion(emotion.getName(), emotion);
		}
		for (IDataSource<?> dataSource : plugin.getDataSources()){
			System.out.println("Load::Source::"+dataSource.getName());
			getManagers().getDataManager().registerDataSource(dataSource.getName(), dataSource);
		}
	}
	public abstract Collection<EmpathicPlugin> loadPluginClasses();
	public void startEmpathy(){

	}

	public <T> int pushValue(String dataSourceName, T value){
		System.out.println("PushTo::"+dataSourceName+"::Value::"+value);
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

// °: bubuja que resuelve bugs

function Managers ()
{
	var dataManager = null;
	var ruleManager = null;
	var uiManager = null;
	this.getDataManager = function () { return dataManager; }
	this.getRuleManager = function () { return ruleManager; }
	this.getUiManager = function () { return uiManager; }
	this.setDataManager = function (value) { dataManager = value; }
	this.setRuleManager = function (value) { ruleManager = value; }
	this.setUiManager = function (value) { uiManager = value; }
}

function EmpathicPlugin() 
{
	this.getArbiter = function () { return null; }
	this.getArbiterCriterion = function () { return null; }
	this.getDataManager = function () { return null; }
	this.getRuleManager = function () { return null; }
	this.getUiManager = function () { return null; }
	this.getRules = function () { return []; }
	this.getSources = function () { return []; }
}


function EmpathicKernel(plugins)
{
	var arbiter = null;
	var managers = new Managers();
	this.getManagers = function () { return managers; }
	// ADD MANAGERS
	for (index in plugins) {
		if (plugins[index].getArbiter() != null) {
			getManagers().setArbiter(plugins[index].getArbiter());
			getManagers().getArbiter().setEmpathy(this);
		}
		if (plugins[index].getDataManager() != null) getManagers().setDataManager(plugins[index].getDataManager());
		if (plugins[index].getRuleManager() != null) getManagers().setRuleManager(plugins[index].getRuleManager());
		if (plugins[index].getUiManager() != null) getManagers().setUiManager(plugins[index].getUiManager());
	}
	for (index in plugins)
	{
		if (plugins[index].getArbiterCriterion() != null) getManagers().getArbiter().setCriterion(plugins[index].getArbiterCriterion());
	}
	// ADD RULES AND SOURCES
	for (index in plugins) {
		var rules = plugins[index].getRules();
		if (rules != null) {
			for (r_index in rules) {
				getManagers().getRuleManager().put(rules[r_index].getName(), rules[r_index]);
			}
		}
		var sources = plugins[index].getSources();
		if (sources != null) {
			for (s_index in sources) {
				getManager().getDataManager().put(sources[s_index].getName(), sources[s_index]);
			}
		}
	}
}

EmpathicKernel.prototype.getRule = 
	function (rulename)
	{
		return this.getManagers().getRuleManager().get(rulename);
	}

EmpathicKernel.prototype.getRuleNames = 
	function ()
	{
		return this.getManagers().getRuleManager().getNames();
	}


function DefaultEmpathicPlugin() 
{
	EmpathicPlugin.call(this);
	var arbiter = new EmpathicArbiter();
	var criterion = new LessUsedArbiterCriterion();
	var dataManager = new DataManager();
	var ruleManager = new RuleManager();
	this.getArbiter = function () { return arbiter; }
	this.getArbiterCriterion = function () { return criterion; }
	this.getDataManager = function () { return dataManager; }
	this.getRuleManager = function () { return ruleManager; }
}
DefaultEmpathicPlugin.subclassOf(EmpathicPlugin);
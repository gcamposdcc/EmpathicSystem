// °: bubuja que resuelve bugs

function Managers ()
{
	var dataManager = null;
	var ruleManager = null;
	var uiManager = null;
	var arbiter = null;
	this.getDataManager = function () { return dataManager; }
	this.getRuleManager = function () { return ruleManager; }
	this.getUiManager = function () { return uiManager; }
	this.getArbiter = function () { return arbiter; }
	this.setDataManager = function (value) { dataManager = value; }
	this.setRuleManager = function (value) { ruleManager = value; }
	this.setUiManager = function (value) { uiManager = value; }
	this.setArbiter = function (value) { arbiter = value; }
}

function EmpathicPlugin() 
{
}
EmpathicPlugin.prototype.getArbiter = function () { return null; }
EmpathicPlugin.prototype.getArbiterCriterion = function () { return null; }
EmpathicPlugin.prototype.getDataManager = function () { return null; }
EmpathicPlugin.prototype.getRuleManager = function () { return null; }
EmpathicPlugin.prototype.getUiManager = function () { return null; }
EmpathicPlugin.prototype.getRules = function () { return []; }
EmpathicPlugin.prototype.getSources = function () { return []; }


function EmpathicKernel(plugins)
{
	ObservableContext.call(this);
	ObserverContext.call(this);
	var managers = new Managers();
	this.getManagers = function () { return managers; }
	// ADD MANAGERS
	for (index in plugins) {
		if (plugins[index].getArbiter() != null) {
			this.getManagers().setArbiter(plugins[index].getArbiter());
			this.getManagers().getArbiter().setEmpathy(this);
		}
		if (plugins[index].getDataManager() != null) this.getManagers().setDataManager(plugins[index].getDataManager());
		if (plugins[index].getRuleManager() != null) this.getManagers().setRuleManager(plugins[index].getRuleManager());
		if (this.getManagers().getRuleManager() != null && this.getManagers().getDataManager() != null){
			this.getManagers().getRuleManager().setDataMediator(new DataRuleMediator(this.getManagers().getDataManager()));
		}
		if (plugins[index].getUiManager() != null) this.getManagers().setUiManager(plugins[index].getUiManager());
	}
	for (index in plugins)
	{
		if (plugins[index].getArbiterCriterion() != null) this.getManagers().getArbiter().setCriterion(plugins[index].getArbiterCriterion());
	}
	// ADD RULES AND SOURCES
	for (index in plugins) {
		var rules = plugins[index].getRules();
		if (rules != null) {
			for (r_index in rules) {
				this.getManagers().getRuleManager().put(rules[r_index].getName(), rules[r_index]);
			}
		}
		var sources = plugins[index].getSources();
		if (sources != null) {
			for (s_index in sources) {
				this.getManagers().getDataManager().put(sources[s_index].getName(), sources[s_index]);
			}
		}
	}
}

EmpathicKernel.prototype.getRule = 
	function (rulename)
	{
		console.log("kernel:getrule:"+rulename);
		return this.getManagers().getRuleManager().get(rulename);
	}

EmpathicKernel.prototype.getRuleNames = 
	function ()
	{
		return this.getManagers().getRuleManager().getNames();
	}

EmpathicKernel.prototype.showEmpathy = 
	function ()
	{
		var handler = function (source, value) {
			console.log("rule: " + value.getName());
			this.getManagers().getUiManager().showMessage(value.getMessage());
		};
		this.getManagers().getArbiter().addObserver(this);
		this.addHandler("rule:arbiter", handler);
		this.getManagers().getArbiter().getValidRule();
	}
EmpathicKernel.prototype.ins = 
	function (ds_name, value){
		this.getManagers().getDataManager().ins(ds_name, value);
	}

function DefaultEmpathicPlugin() 
{
	EmpathicPlugin.call(this);
	var arbiter = new EmpathicArbiter();
	var criterion = new LessUsedArbiterCriterion();
	var dataManager = new DataManager();
	var ruleManager = new RuleManager();
	var uiManager = new UiManager();
	this.getArbiter = function () { return arbiter; }
	this.getArbiterCriterion = function () { return criterion; }
	this.getDataManager = function () { return dataManager; }
	this.getRuleManager = function () { return ruleManager; }
	this.getUiManager = function () { return uiManager; }
}
DefaultEmpathicPlugin.subclassOf(EmpathicPlugin);
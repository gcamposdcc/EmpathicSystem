function DataRuleMediator(dataMan)
{
	ObservableContext.call(this);
	var dataManager = dataMan;
	this.get =
		function (ds_name, option, args)
		{
			var ds = dataManager.get(ds_name);
			if (ds != null) {
				return ds.get(option, args);
			} else {
				return {};
			}
		}
}


function RuleManager()
{
	FlyweightFactory.call(this);
	var dataMediator = null;
	this.setDataMediator = 
		function (dataMed)
		{
			dataMediator = dataMed;
			var rules = items();
			for (key in rules)
			{
				rules[key].dataMediator.set(dataMed);
			}
		}
	this.getDataMediator = function () { return dataMediator; }
}

RuleManager.subclassOf(FlyweightFactory);
RuleManager.prototype.put = 
	function (key, value) {
		if (key == null) {
			console.log('key cannot be null');
			return;
		} else if (value == null) {
			console.log('value cannot be null');
		} else if (getObjectClass(key) != 'String') {
			console.log('key must be a String');
			return;
		}
		getItems()[key] = value;
		getNames().push(key);
		value.dataMediator.set(this.dataMediator.get());
	}

function EmpathicRule(ename)
{
	ObservableContext.call(this);
	ObserverContext.call(this);
	var dataMediator = null;
	this.getDataMediator = function () { return dataMediator; }
	this.setDataMediator = function (dataMed) { dataMediator = dataMed; }
	this.getName = function () { return ename; }
	this.get = 
		function (option, args)
		{
			if (dataMediator.get() != null){
				dataMediator.get().get(option, args);
			} else {
				return {};
			}
		}
}

EmpathicRule.prototype.isSelectable = function () { return true; }
EmpathicRule.prototype.canEvaluate = function (args) { return true; }
EmpathicRule.prototype.evaluate = function (args) { return 0.0; }
EmpathicRule.prototype.getMessage =
	function ()
	{
		var mess = new EmpathicMessage();
		mess.setRulename(this.getName());
		return mess;
	}
EmpathicRule.prototype.getParams =
	function ()
	{
		return {};
	}

function EmpathicMessage()
{
	var context = new EmpathicMessageContext();
	this.getContext = function () { return context; }
	var unfilteredText = "This is the default EmpathicMessage text";
	this.getUnfilteredText = function () { return unfilteredText; }
	this.setUnfilteredText = function (utext) { unfilteredText = utext; }
	this.getText = function () { return filterText(this.getUnfilteredText()); }
	var values = {};
	this.filterText = 
		function () 
		{
			var text = this.getUnfilteredText();
			for(key in values){
				text = text.replace(new RegExp(key, 'g'), values[key]);
			}
			return text;
		}
	this.putValue = 
		function (key, value) 
		{
			if (key == null || value == null) return;
			values[key] = value;
		}
	this.clearValues = function () { values = {} }
}
EmpathicMessage.prototype.setRulename = function (n) { this.getContext().setRulename(n); }
EmpathicMessage.prototype.getRulename = function () { this.getContext().getRulename(); }

function EmpathicMessageContext()
{
	this.rulename = "";
}
EmpathicMessageContext.prototype.setRulename = function (n) { this.rulename = n; }
EmpathicMessageContext.prototype.getRulename = function () { return this.rulename; }
EmpathicMessageContext.subclassOf(FlyweightFactory);


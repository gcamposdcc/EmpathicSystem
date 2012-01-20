function DataRuleMediator(dataMan)
{
	ObserverContext.call(this);
	ObservableContext.call(this);
	var dataManager = dataMan;
	this.get =
		function (ds_name, option, args)
		{
			console.log("mediating select: rule " + option.getHandlerName() + ",ds " + ds_name);
			var ds = dataManager.get(ds_name);
			var eventName = 'select:ds:' + ds_name + ':' + option.getHandlerName();
			if (ds != null) {
				ds.addObserver(this);
				var callback = function (source, value)
				{
					this.removeHandler(eventName);
					this.notifyAll(eventName, source, value);
					ds.removeObserver(this);
				}
				this.addHandler(eventName, callback);
				option.callbackHandler = this;
				ds.sel(option, args);
			} else {
				this.notifyAll(eventName, null, {});
			}
		}

	this.count =
		function (ds_name, option, args)
		{
			console.log("mediating count: rule " + option.getHandlerName() + ",ds " + ds_name);
			var ds = dataManager.get(ds_name);
			var eventName = 'count:ds:' + ds_name + ':' + option.getHandlerName();
			if (ds != null) {
				ds.addObserver(this);
				var callback = function (source, value)
				{
					this.removeHandler(eventName);
					this.notifyAll(eventName, source, value);
					ds.removeObserver(this);
				}
				this.addHandler(eventName, callback);
				option.callbackHandler = this;
				ds.count(option, args);
			} else {
				this.notifyAll(eventName, null, {});
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
			for (key in this.getItems()) {
				this.getItems()[key].setDataMediator(dataMed);
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
			return;
		} else if (getObjectClass(key) != 'String') {
			console.log('key must be a String');
			return;
		}
		console.log("registering rule '" + key + "'");
		this.getItems()[key] = value;
		this.getNames().push(key);
		value.setDataMediator(this.getDataMediator());
	}

function EmpathicMessageContext()
{
	FlyweightFactory.call(this);
	this.rulename = "";
	this.setRulename = function (n) { this.rulename = n; }
	this.getRulename = function () { return this.rulename; }
}
EmpathicMessageContext.subclassOf(FlyweightFactory);

function EmpathicMessage()
{
	this.context = new EmpathicMessageContext();
	this.getContext = function () { return this.context; }
	var unfilteredText = "This is the default EmpathicMessage text";
	this.getUnfilteredText = function () { return unfilteredText; }
	this.setUnfilteredText = function (utext) { unfilteredText = utext; }
	this.getText = function () { return this.filterText(this.getUnfilteredText()); }
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
	this.setRulename = function (n) { this.getContext().setRulename(n); }
	this.getRulename = function () { this.getContext().getRulename(); }
}

function EmpathicRule(ename)
{
	ObservableContext.call(this);
	ObserverContext.call(this);
	var dataMediator = null;
	this.result = 0;
	this.getDataMediator = function () { 
		console.log(dataMediator);
		return dataMediator; 
	}
	this.setDataMediator = function (dataMed) { dataMediator = dataMed; }
	this.getName = function () { return ename; }
	this.get = 
		function (ds_name, option, args, callback)
		{
			var eventName = 'select:ds:'+ds_name;
			var c =
				function(source, value){
					this.removeHandler(eventName);
					callback.call(this, source, value);
				}
			this.addHandler(eventName, c);
			this.getDataMediator().get(option, args);
		}
	var mess = new EmpathicMessage();
	mess.setRulename(this.getName());
	this.getMessage = function () { return mess; }
}

EmpathicRule.prototype.isSelectable = 
	function () 
	{ 
		this.notifyAll('selectable:rule:'+this.getName(), this, true);
		return true; 
	}
EmpathicRule.prototype.canEvaluate = 
	function (args) 
	{
		this.notifyAll('evaluable:rule:'+this.getName(), this, true);
		return true; 
	}
EmpathicRule.prototype.evaluate = 
	function (args) 
	{
		this.notifyAll('evaluate:rule:'+this.getName(), this, this.result);
		return this.result; 
	}
EmpathicRule.prototype.getParams =
	function ()
	{
		return {};
	}
EmpathicRule.prototype.sel =
	function (ds_name, option, args, handler)
	{
		this.getDataMediator().addObserver(this);
		this.addHandler('select:ds:'+ds_name+':'+this.getName(), handler);
		this.getDataMediator().count(ds_name, option, args);
	}
EmpathicRule.prototype.count =
	function (ds_name, option, args, handler)
	{
		this.getDataMediator().addObserver(this);
		this.addHandler('count:ds:'+ds_name+':'+this.getName(), handler);
		this.getDataMediator().count(ds_name, option, args);
	}


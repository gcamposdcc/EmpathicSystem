function RuleUsage()
{
	var usage = [];
	this.newUsage = function () { usage.push(new Date()); }
	this.getTimesUsed = function () { return usage.length; }
}


function EmpathicArbiterCriterion() 
{
	ObservableContext.call(this);
	ObserverContext.call(this);
}

EmpathicArbiterCriterion.prototype.visit = function (arbiter) {
	this.notifyAll("rule:arbitercriterion", this, null);
}

function EmpathicArbiter(k)
{
	ObservableContext.call(this);
	ObserverContext.call(this);
	var kernel = k ? k : null;
	this.setEmpathy = function (value) { kernel = value; }
	this.getEmpathy = function () { return kernel; }
	var criterion = new EmpathicArbiterCriterion();
	this.setCriterion = function (value) { criterion = value; }
	this.getCriterion = function () { return criterion; }
	var validRule = null;
	var ruleUsages = {};
	this.getRuleUsages = function () { return ruleUsages; }
	this.getRuleUsageCount =
		function (rulaname) 
		{
			if (ruleUsages[rulename] == null) return 0;
			else return ruleUsages[rulename].getTimesUsed();
		}
	this.getAllRuleUsageCount =
		function () 
		{
			var sum = 0;
			for (u_index in ruleUsages){
				sum += ruleUsages[u_index];
			}
			return sum;
		}
	this.getRuleUsageData =
		function (rulename)
		{
			return ruleUsages[rulename] == null ? ruleUsages[rulename] : new RuleUsage();
		}
	this.addRuleUsageData = 
		function (rulename) {
			if (ruleUsages[rulename] == null) ruleUsages[rulename] = new RuleUsage();
			ruleUsages[rulename].newUsage();
		}
	this.getCurrentValidRule = function () { return validRule; }
	this.updateValidRule = 
		function ()
		{
			var handler = function (source, value){
				console.log("newValidRule");
				console.log(value);
				validRule = this.getRule(value);
				this.addRuleUsageData(this.getCurrentValidRule().getName());
				this.notifyAll("rule:arbiter", this, validRule);
			}
			this.getCriterion().addObserver(this);
			this.addHandler("rule:arbitercriterion", handler);
			this.accept(this.getCriterion());
		}
	this.getValidRule = 
		function () 
		{ 
			this.updateValidRule();
		}
	this.setValidRule = 
		function (value)
		{
			validRule = value;
		}
}

EmpathicArbiter.prototype.getRule =
	function (rulename)
	{
		console.log("arbiter:getrule: " + this.getEmpathy());
		return this.getEmpathy().getRule(rulename);
	}

EmpathicArbiter.prototype.getAllRuleNames = 
	function ()
	{
		return this.getEmpathy().getRuleNames();
	}

EmpathicArbiter.prototype.accept = 
	function (criterion)
	{
		criterion.visit(this);
	}
EmpathicArbiter.prototype.getEvaluableRules = 
	function ()
	{
		var list = new Array();
		var rule;
		var rulenames = this.getAllRuleNames();
		console.log("arbiter:canEvaluate: " + rulenames);
		var rulename = '';
		var count = 0;
		function processRule (rnames, target){
			var rule = this.getRule(target);
			function processRuleHandler (source, value){
				if (value){
					list.push(target);
					console.log("Rule::"+target+"::Evaluable?:YES");
				} else {
					console.log("Rule::"+target+"::Evaluable?:NOT");
				}
				++count;
				if (count == rnames.length) this.notifyAll("evaluable:arbiter", this, list);
			}
			rule.addObserver(this);
			this.addHandler("evaluable:rule:" + target, processRuleHandler);
			rule.canEvaluate(rule.getParams());
		}
		for (rn_index in rulenames){
			rulename = rulenames[rn_index];
			rule = this.getRule(rulename);
			console.log(rulename + " : " + rule)
			try{
				processRule.call(this, rulenames, rulename)
			} catch (err){
				console.log(err.description);
				console.log(err);
			}
		}
	}
EmpathicArbiter.prototype.getSelectableRulesImpl = 
	function (source, rulenames)
	{
		console.log("arbiter.getSelectableRulesImpl("+rulenames+")");
		var list = new Array();
		var rule = null;
		var rulename = '';
		var count = 0;

		var selectable_list = new Array();
		var selectable_count = 0;

		function filterSelectable (rnames, target){
			var rule = this.getRule(target);
			var rname = rule.getName();
			function filterSelectableHandler (source, value){
				if (value){
					selectable_list.push(rname);
					console.log("Rule::"+rname+"::Selectable?:YES");
				} else {
					console.log("Rule::"+rname+"::Selectable?:NOT");
				}
				selectable_count++;
			}
			rule.addObserver(this);
			this.addHandler("selectable:rule:"+rule.getName(), filterSelectableHandler);
			rule.isSelectable(rule.getParams());
			if (selectable_count == rulenames.length) {
				console.log("BEGIN:arbiter:selectable_rules");
				console.log(selectable_list);
				console.log("END:arbiter:selectable_rules");
				this.notifyAll("selectable:arbiter", this, selectable_list);
			}
		}
		function filterSelectables () {
			for (rname in rulenames){
				filterSelectable.call(this, rulenames, rulenames[rname]);
			}
		}
		function evaluateRule (rulenames, target){
			console.log("arbiter:evaluating: " + rulenames);
			var rule = this.getRule(target);
			var rulename = rule.getName();
			function evaluateHandler (source, value){
				count++;
			}
			rule.addObserver(this);
			this.addHandler("evaluate:rule:" + rule.getName(), evaluateHandler);
			rule.evaluate(rule.getParams());
			if (count == rulenames.length) {
				filterSelectables.call(this);
			}
		}
		for (rname in rulenames){
			evaluateRule.call(this,rulenames, rulenames[rname]);
		}
}
EmpathicArbiter.prototype.getSelectableRules =
	function ()
	{
		console.log("arbiter.getSelectableRules");
		this.addObserver(this);
		this.addHandler("evaluable:arbiter", this.getSelectableRulesImpl);
		this.getEvaluableRules();
	}

EmpathicArbiter.prototype.timesSelected =
	function (rulename, not_found)
	{
		var rud = this.getRuleUsageData(rulename);
		if (rud != null) {
			console.log("rule:"+rulename+":used:" + rud.getTimesUsed());
			return rud.getTimesUsed();
		}
		else {
			if (this.getAllRuleNames().indexOf(rulename) >= 0) {
				console.log("rule:"+rulename+":found?:yes:used?:no");
				return 0;
			} else {
				console.log("rule:"+rulename+":found?:no:used?:no(thx_captain)");
				return not_found;
			}
		}
	}


LessUsedArbiterCriterion.subclassOf(EmpathicArbiterCriterion);
function LessUsedArbiterCriterion()
{
	EmpathicArbiterCriterion.call(this);
}

LessUsedArbiterCriterion.prototype.lessUsed =  
	function (arbiter, rulenames) {
		var max = 1000000;
		var min = 1000000;
		var current = 0;
		var l_rulenames = new Array();
		var rulename = '';
		console.log("rulenames");
		console.log(rulenames);
		for (rn_index in rulenames){
			rulename = rulenames[rn_index];
			// if (rulename.equals(EmptyRule.instance.getName())) continue;
			current = arbiter.timesSelected(rulename, max);
			console.log("Rule::"+rulename+"::Used::"+current);
			if (current < min){
				min = current;
				l_rulenames = new Array();
				l_rulenames.push(rulename);
			} else if (current == min){
				l_rulenames.push(rulename);
			}
		}
		console.log("LessUsedRules");
		for (rn_index in l_rulenames){
			console.log("-- "+ l_rulenames[rn_index]);
		}
		switch(l_rulenames.length){
		case 0:
			return new EmpathicRule('__empty_rule__');
		case 1:
			console.log("lessUsedRule:index:0:name:"+l_rulenames[0]);
			return l_rulenames[0];
		default:
			var index = Math.floor(Math.random()*(l_rulenames.length));
			console.log("lessUsedRule:index:"+index+":name:"+l_rulenames[index]);
			return l_rulenames[index];
		}
	}

LessUsedArbiterCriterion.prototype.visit =
	function (arbiter)
	{
		var handler = function (source, value){
			console.log("filteringLessUsed:" + value);
			var result = this.lessUsed(arbiter, value);
			this.notifyAll("rule:arbitercriterion", this, result);
		}
		arbiter.addObserver(this);
		this.addHandler("selectable:arbiter", handler);
		arbiter.getSelectableRules();
	}
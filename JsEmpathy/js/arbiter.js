function RuleUsage()
{
	var usage = [];
	this.newUsage = function () { usage.push(new Date()); }
	this.getTimesUsed = function () { return usage.length; }
}


function EmpathicArbiterCriterion() 
{
	
}

EmpathicArbiterCriterion.prototype.visit = function (arbiter) {}

function EmpathicArbiter()
{
	var kernel = null;
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
	var updateValidRule = 
		function ()
		{
			validRule = accept(getCriterion());
		}
	this.getValidRule = 
		function () 
		{ 
			updateValidRule();
			addRuleUsage(getCurrentValidRule().getName());
			return getCurrentValidRule(); 
		}
	this.getCurrentValidRule = function () { return validRule; }
}

EmpathicArbiter.prototype.getRule =
	function (rulename)
	{
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
		var rulename = '';
		for (rn_index in rulenames){
			rulename = rulenames[rn_index];
			rule = this.getRule(rulename);
			try{
				if (rule.canEvaluate(rule.getParams())){
					list.push(rulename);
					console.log("Rule::"+rulename+"::Evaluable?:YES");
				} else {
					console.log("Rule::"+rulename+"::Evaluable?:NOT");
				}
			} catch (err){
				console.log(err.description);
			}
		}
		return list;
	}

EmpathicArbiter.prototype.getSelectableRules =
	function ()
	{
		var list = new Array();
		var rule = null;
		var rulenames = this.getEvaluableRules();
		var rulename = '';
		for (rn_index in rulenames){
			rulename = rulenames[rn_index];
			try{
				rule = this.getRule(rulename);
				rule.evaluate(rule.getParams());
				if (rule.isSelectable()) {
					list.push(rulename);
				}
			} catch (err){
				console.log(err.description);
			}
		}
		return list;
	}

EmpathicArbiter.prototype.timesSelected =
	function (rulename, not_found)
	{
		var rud = this.getRuleUsageData(rulename);
		if (rud != null) return rud.getTimesUsed();
		else return this.getAllRuleNames()[rulename] != null ? 0: not_found;
	}


function LessUsedArbiterCriterion()
{
	EmpathicArbiterCriterion.call(this);
}

LessUsedArbiterCriterion.subclassOf(EmpathicArbiterCriterion);

LessUsedArbiterCriterion.prototype.lessUsed = 
	function (arbiter, rulenames)
	{
		var max = 1000000;
		var min = 1000000;
		var current = 0;
		var l_rulenames = new Array();
		var rulename = '';
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
		for (rn_index in rulenames){
			console.log("-- "+rulenames[rn_index]);
		}
		switch(l_rulenames.length){
		case 0:
			return new EmpathicRule('__empty_rule__');
		case 1:
			return l_rulenames[0];
		default:
			return l_rulenames[Math.floor(Math.random()*(l_rulenames.length))];
		}
	}

LessUsedArbiterCriterion.prototype.visit =
	function (arbiter)
	{
		return this.lessUsed(arbiter, arbiter.getSelectableRules());
	}
function Pair(k, v)
{
	var key = k;
	this.value = v;
	this.getKey = function () { return key; }
	this.setKey = function (nk) { key = nk; }
	this.compareValue = 
		function (v)
		{
			return v == this.value;
		}
	this.apply =
		function (target){
			return target[key] == this.value;
		}
}

function QueryCriterion(p)
{
	this.target = null;
	this.params = (p == null ? {} : p);
}
QueryCriterion.prototype.apply = 
	function (target) {
		if (params == null) return true;
		var passes = true;
		for (p_index in params){
			try {
				if (params[p_index] != target[p_index]){
					return false;
				}
			} catch (err){
				return false;
			}
		}
		return passes;
	}

function QueryOption(t, v)
{
	var type = t;
	var value = v > 0 ? v : 1;
	this.getType = function () { return type; }
	this.getValue = function () { return value; }
}

function DataManager() 
{
	FlyweightFactory.call(this);
}

DataManager.subclassOf(FlyweightFactory);

DataManager.prototype.put = 
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
	}

DataManager.prototype.ins = 
	function (ds_name, value) 
	{
		if (this.items[ds_name] != null) 
		{
			return this.items[ds_name].ins(value);
		}
		return -1;
	}
DataManager.prototype.upd = 
	function (ds_name, new_value, option, args) 
	{
		if (this.items[ds_name] != null) 
		{
			return this.items[ds_name].upd(new_value, option, args);
		}
		return null;
	}
DataManager.prototype.sel = 
	function (ds_name, option, args)
	{
		if (this.items[ds_name] != null) 
		{
			return this.items[ds_name].ins(option, args);
		}
		return null;
	}
DataManager.prototype.del = 
	function (ds_name, option, args)
	{
		if (this.items[ds_name] != null) 
		{
			return this.items[ds_name].del(option, args);
		}
		return false;
	}

function DataSource(ds_name, template)
{
	this.getName = function () { return ds_name; }
	var type_name = template != null ? getObjectClass(template) : '__noType__';
	this.getTypeName = function () { return typename; }
}
DataSource.prototype.ins = function (value) { return -1; }
DataSource.prototype.upd = function (new_value, option, args) {}
DataSource.prototype.sel = function (option, args) { return []; }
DataSource.prototype.del = function (option, args) { return false; }

DataSource.prototype.passes = 
	function (target, args)
	{
		for (a_index in args) {
			try{
				if (!args[a_index].apply(target)){
					return false;
				}
			} catch (err){
				return false;
			}
		}
		return true;
	}

function MemoryDataSource(ds_name, template)
{
	DataSource.call(this, ds_name, template);
	var items = new Array();
	this.getItems = function() { return items; }
	this.setItems = function(it) { items = it; }
}
MemoryDataSource.subclassOf(DataSource);

MemoryDataSource.prototype.ins = 
	function (value) 
	{
		if (getObjectClass(value) != this.getTypeName()) 
		{
			console.log('Cannot assing an ' + getObjectClass(value) + ' to a DS of ' + this.getTypeName());
			return -1;
		}
		var l_id = this.getItems().push(value) - 1;
		console.log(this.getName() + '::' + this.items());
		return l_id;
	}
MemoryDataSource.prototype.upd = 
	function (new_value, option, args) {
		var its = this.getItems();
		var result = new Array();
		var count = 0;
		var type = option != null ? (option.getType() != null ? option.getType() : 'all') : 'all';
		var value = option != null ? (option.getValue() > 0 ? option.getValue() : 1) : 1;
		if (type == 'id') {
			this.getItems()[value] = new_value;
			return true;
		}
		for (i_index in its) {
			if (this.filter(its[i_index], args)) {
				result.push(its[i_index]);
			}
			if (type == 'limit' && result.length == value) break;
		}
		for (r_index in result){
			this.getItems()[result[r_index]] = new_value;
		}
		return result.length > 0;
	}
MemoryDataSource.prototype.sel = 
	function (option, args) 
	{
		if (args == null) return this.getItems().slice(0);
		if (args.length == 0) return this.getItems().slice(0);
		var its = this.getItems();
		var result = new Array();
		var count = 0;
		var type = option != null ? (option.getType() != null ? option.getType() : 'all') : 'all';
		var value = option != null ? (option.getValue() > 0 ? option.getValue() : 1) : 1;
		if (type == 'id') return its[value];
		for (i_index in its) {
			if (this.filter(its[i_index], args)) {
				result.push(its[i_index]);
			}
			if (type == 'limit' && result.length == value) break;
		}
		return result;
	}
MemoryDataSource.prototype.del = 
	function (option, args) {
		var its = this.getItems();
		var result = new Array();
		var count = 0;
		var type = option != null ? (option.getType() != null ? option.getType() : 'all') : 'all';
		var value = option != null ? (option.getValue() > 0 ? option.getValue() : 1) : 1;
		if (type == 'id') {
			delete this.getItems()[value];
			return true;
		}
		for (i_index in its) {
			if (this.filter(its[i_index], args)) {
				result.push(its[i_index]);
			}
			if (type == 'limit' && result.length == value) break;
		}
		for (r_index in result){
			delete this.getItems()[result[r_index]];
		}
		return result.length > 0;
	}

function WebMetadata(sel, ins, upd, del)
{
	var selectUrl = sel;
	var insertUrl = ins;
	var updateUrl = upd;
	var deleteUrl = del;
	this.getSelectUrl = function () { return selectUrl; }
	this.getInsertUrl = function () { return insertUrl; }
	this.getUpdateUrl = function () { return updateUrl; }
	this.getDeleteUrl = function () { return deleteUrl; }
}

private final T template;
private final String name;
private final boolean useSameUrl;
private String defaultUrl;
private String updateUrl;
private String selectUrl;
private String deleteUrl;
private String insertUrl;
private final ExecutorService executorService;
function WebDataSource(ds_name, template, meta){
	DataSource.call(this, ds_name, template);
	var metadata = meta;
	this.getSelectUrl = function () { return metadata.getSelectUrl(); }
	this.getInsertUrl = function () { return metadata.getInsertUrl(); }
	this.getUpdateUrl = function () { return metadata.getUpdateUrl(); }
	this.getDeleteUrl = function () { return metadata.getDeleteUrl(); }
}

WebDataSource.subclassOf(DataSource);

WebDataSource.prototype.params = 
	function (target, args)
	{
		var result = '';
		for (a_index in args) {
			try{
				result = result + args[a_index].getKey() + '=' + args[a_index].getValue();
			} catch (err){
				continue;
			}
		}
		return result;
	}

WebDataSource.prototype.request =
	function (url, params, method, callback)
	{
		var xhr = new XMLHttpRequest;
		xhr.open(method, url + '?' + params, true);
		//xhr.responseType = "arraybuffer";
		xhr.onreadystatechange = function() {
	  			if (request.readyState == 4){
	    			return callback.call(this, xhr.responseText);
	  			}
			};
		xhr.send(null);
		return ;
	}

WebDataSource.prototype.ins = 
	function (value) 
	{
		var pars = this.params(args);
		var method = 'POST';
		var callback = 
			function (response)
			{
				
			};
		var r = this.request(this.getInsertUrl(), pars, method, callback);
	}
WebDataSource.prototype.upd = 
	function (new_value, option, args) {
		var pars = this.params(args);
		var method = 'POST';
		var callback = 
			function (response)
			{
				
			};
		var r = this.request(this.getUpdateUrl(), pars, method, callback);
	}
WebDataSource.prototype.sel = 
	function (option, args) 
	{
		var pars = this.params(args);
		var method = 'POST';
		var callback = 
			function (response)
			{
				
			};
		var r = this.request(this.getSelectUrl(), pars, method, callback);
	}
WebDataSource.prototype.del = 
	function (option, args) {
		var pars = this.params(args);
		var method = 'POST';
		var callback = 
			function (response)
			{
				
			};
		var r = this.request(this.getDeleteUrl(), pars, method, callback);
	}

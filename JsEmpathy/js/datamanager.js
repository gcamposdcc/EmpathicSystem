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

function QueryCriterion(p, c)
{
	this.target = null;
	this.params = (p == null ? {} : p);
	this.callback = c ? c : null;
}
QueryCriterion.prototype.apply = 
	function (target) {
		if (this.callback != null){
			return this.callback.call(this, target, params);
		}
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

function QueryOption(t, v, h)
{
	var type = t;
	var value = v > 0 ? v : 1;
	var handlerName = h ? h : 'all';
	this.getType = function () { return type; }
	this.getValue = function () { return value; }
	this.getHandlerName = function () { return handlerName; }
	this.setHandlerName = function (h_name) { handlerName = h_name; }
}

function DataManager() 
{
	FlyweightFactory.call(this);
	ObservableContext.call(this);
	ObserverContext.call(this);
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
		this.getItems()[key] = value;
		this.getNames().push(key);
	}

DataManager.prototype.ins = 
	function (ds_name, value) 
	{
		var task_name = 'insert:ds:' + ds_name;
		if (this.getItems()[ds_name] != null) 
		{
			var handler = function (source, value) { 
				this.notifyAll(task_name, this, value);
				this.removeHandler(task_name);
			};
			this.getItems()[ds_name].addObserver(this);
			this.addHandler(task_name, handler);
			this.getItems()[ds_name].ins(value);
		} else {
			this.notifyAll(task_name, this, -1); 
		}
	}
DataManager.prototype.upd = 
	function (ds_name, new_value, option, args) 
	{ 
		var task_name = 'update:ds:' + ds_name+ ':' + option.getHandlerName();
		if (this.getItems()[ds_name] != null) 
		{
			var handler = function (source, value) { 
				this.notifyAll(task_name, this, value);
				this.removeHandler(task_name);
			};
			this.getItems()[ds_name].addObserver(this);
			this.addHandler(task_name, handler);
			this.getItems()[ds_name].ins(value);
		} else {
			this.notifyAll(task_name, this, null); 
		}
	}
DataManager.prototype.sel = 
	function (ds_name, option, args)
	{
		var task_name = 'select:ds:' + ds_name+ ':' + option.getHandlerName();
		if (this.getItems()[ds_name] != null) 
		{
			var handler = function (source, value) { 
				this.notifyAll(task_name, this, value);
				this.removeHandler(task_name);
			};
			this.getItems()[ds_name].addObserver(this);
			this.addHandler(task_name, handler);
			this.getItems()[ds_name].ins(value);
		} else {
			this.notifyAll(task_name, this, []); 
		}
	}
DataManager.prototype.del = 
	function (ds_name, option, args)
	{
		var task_name = 'delete:ds:' + ds_name+ ':' + option.getHandlerName();
		if (this.getItems()[ds_name] != null) 
		{
			var handler = function (source, value) { 
				this.notifyAll(task_name, this, value);
				this.removeHandler(task_name);
			};
			this.getItems()[ds_name].addObserver(this);
			this.addHandler(task_name, handler);
			this.getItems()[ds_name].ins(value);
		} else {
			this.notifyAll(task_name, this, false); 
		}
	}

function DataSource(ds_name, template)
{
	ObservableContext.call(this);
	var type_name = template != null ? getObjectClass(template) : '__notype__';
	this.getName = function () { return ds_name; }
	this.getTypeName = function () { return type_name; }
}
DataSource.prototype.ins = 
	function (value) 
	{ 
		this.notifyAll('insert:ds:' + this.getName(), this, -1); 
	}
DataSource.prototype.upd = 
	function (new_value, option, args) 
	{ 
		this.notifyAll('update:ds:' + this.getName() + ':' + option.getHandlerName(), this, true); 
	}
DataSource.prototype.sel = 
	function (option, args) 
	{ 
		this.notifyAll('insert:ds:' + this.getName() + ':' + option.getHandlerName(), this, []);
	}
DataSource.prototype.del = 
	function (option, args) 
	{ 
		this.notifyAll('insert:ds:' + this.getName() + ':' + option.getHandlerName(), this, true);
	}
DataSource.prototype.count = 
	function (option, args)
	{
		this.notifyAll('count:ds' + this.getName() + ':' + option.getHandlerName(), this, 0);
	}

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
		var task_name = 'insert:ds:' + this.getName();
		if (getObjectClass(value) != this.getTypeName()) 
		{
			console.log('Cannot assing an ' + getObjectClass(value) + ' to a DS of ' + this.getTypeName());
			this.notifyAll(task_name, this, -1);
			return -1;
		}
		var l_id = this.getItems().push(value) - 1;
		console.log(this.getName() + '::' + this.getItems());
		this.notifyAll(task_name, this, l_id);
		return l_id;
	}
MemoryDataSource.prototype.upd = 
	function (new_value, option, args) {
		var task_name = 'update:ds:' + this.getName() + ':' + option.getgetHandlerName();
		var its = this.getItems();
		var result = new Array();
		var count = 0;
		var type = option != null ? (option.getType() != null ? option.getType() : 'all') : 'all';
		var value = option != null ? (option.getValue() > 0 ? option.getValue() : 1) : 1;
		if (type == 'id') {
			this.getItems()[value] = new_value;
			this.notifyAll(task_name, this, true);
			return true;
		}
		for (i_index in its) {
			if (this.passes(its[i_index], args)) {
				result.push(its[i_index]);
			}
			if (type == 'limit' && result.length == value) break;
		}
		for (r_index in result){
			this.getItems()[result[r_index]] = new_value;
		}
		this.notifyAll(task_name, this, result.length > 0);
		return result.length > 0;
	}
MemoryDataSource.prototype.sel = 
	function (option, args) 
	{
		var task_name = 'select:ds:' + this.getName() + ':' + option.getHandlerName();
		if (args == null) {
			this.notifyAll(task_name, this, this.getItems().slice(0));
			return this.getItems().slice(0);
		}
		if (args.length == 0) {
			this.notifyAll(task_name, this, this.getItems().slice(0));
			return this.getItems().slice(0);
		}
		var its = this.getItems();
		var result = new Array();
		var count = 0;
		var type = option != null ? (option.getType() != null ? option.getType() : 'all') : 'all';
		var value = option != null ? (option.getValue() > 0 ? option.getValue() : 1) : 1;
		if (type == 'id') {
			this.notifyAll(task_name, this, its[value]);
			return its[value];
		}
		for (i_index in its) {
			if (this.passes(its[i_index], args)) {
				result.push(its[i_index]);
			}
			if (type == 'limit' && result.length == value) break;
		}
		this.notifyAll(task_name, this, result);
		return result;
	}
MemoryDataSource.prototype.del = 
	function (option, args) {
		var task_name = 'delete:ds:' + this.getName() + ':' + option.getHandlerName();
		var its = this.getItems();
		var result = new Array();
		var count = 0;
		var type = option != null ? (option.getType() != null ? option.getType() : 'all') : 'all';
		var value = option != null ? (option.getValue() > 0 ? option.getValue() : 1) : 1;
		if (type == 'id') {
			delete this.getItems()[value];
			this.notifyAll(task_name, this, true);
			return true;
		}
		for (i_index in its) {
			if (this.passes(its[i_index], args)) {
				result.push(its[i_index]);
			}
			if (type == 'limit' && result.length == value) break;
		}
		for (r_index in result){
			delete this.getItems()[result[r_index]];
		}
		this.notifyAll(task_name, this, result.length > 0);
		return result.length > 0;
	}

MemoryDataSource.prototype.count =
	function (option, args) {
		var task_name = 'count:ds:' + this.getName() + ':' + option.getHandlerName();
		if (args == null) {
			this.notifyAll(task_name, this, this.getItems().length);
			return this.getItems().slice(0);
		}
		if (args.length == 0) {
			this.notifyAll(task_name, this, this.getItems().length);
			return this.getItems().slice(0);
		}
		var its = this.getItems();
		var result = new Array();
		var count = 0;
		var type = option != null ? (option.getType() != null ? option.getType() : 'all') : 'all';
		var value = option != null ? (option.getValue() > 0 ? option.getValue() : 1) : 1;
		if (type == 'id') {
			this.notifyAll(task_name, this, its[value] != null ? 1 : 0);
			return its[value] != null ? 1 : 0;
		}
		if (type == 'all') {
			this.notifyAll(task_name, this, this.getItems().length);
			return this.getItems().length;
		}
		for (i_index in its) {
			if (this.passes(its[i_index], args)) {
				result.push(its[i_index]);
			}
			if (type == 'limit' && result.length == value) break;
		}
		this.notifyAll(task_name, this, result.length);
		return result.length;
	}

function WebMetadata(def)
{
	this.defaultUrl = def;
	this.selectUrl = this.defaultUrl;
	this.insertUrl = this.defaultUrl;
	this.updateUrl = this.defaultUrl;
	this.deleteUrl = this.defaultUrl;
	this.parseSelect = function (response) { return response; }
	this.parseDelete = function (response) { return false; }
	this.parseInsert = function (response) { return -1; }
	this.parseUpdate = function (response) { return false; }
	this.webString = function (value) { return "" + value; }
}

function WebDataSource(ds_name, template, meta){
	DataSource.call(this, ds_name, template);
	var metadata = meta;
	this.getSelectUrl = function () { return metadata.selectUrl; }
	this.getInsertUrl = function () { return metadata.insertUrl; }
	this.getUpdateUrl = function () { return metadata.updateUrl; }
	this.getDeleteUrl = function () { return metadata.deleteUrl; }
	this.parseSelect = function (response) { return metadata.parseSelect(response); }
	this.parseDelete = function (response) { return metadata.parseDelete(response); }
	this.parseInsert = function (response) { return metadata.parseInsert(response); }
	this.parseUpdate = function (response) { return metadata.parseUpdate(response); }
	this.webString = function (value) { return metadata.webString(value); }
}

WebDataSource.subclassOf(DataSource);
WebDataSource.prototype.webString

WebDataSource.prototype.params = 
	function (target, args)
	{
		var result = '';
		for (a_index in args) {
			try{
				result = result + args[a_index].getKey() + '=' + args[a_index].getValue() + '&';
			} catch (err){
				continue;
			}
		}
		return result;
	}

WebDataSource.prototype.request =
	function (url, params, method, callback, handler)
	{
		var xhr = new XMLHttpRequest;
		xhr.open(method, url + '?' + params, true);
		//xhr.responseType = "arraybuffer";
		xhr.onreadystatechange = 
			function() {
	  			if (xhr.readyState == 4){
	    			return callback.call(handler, xhr.responseText);
	  			}
			};
		xhr.send(null);
	}

WebDataSource.prototype.ins = 
	function (value) 
	{
		var pars = this.webString(value);
		var method = 'POST';
		var callback = 
			function (response)
			{
				this.notifyAll('insert:ds:' + this.getName(), this, this.parseInsert(response));
			};
		var r = this.request(this.getInsertUrl(), pars, method, callback, this);
	}
WebDataSource.prototype.upd = 
	function (new_value, option, args) {
		var pars = this.params(args);
		var method = 'POST';
		var callback = 
			function (response)
			{
				this.notifyAll('update:ds:' + this.getName() + ':' + option.getHandlerName(), this, this.parseUpdate(response));
			};
		var r = this.request(this.getUpdateUrl(), pars, method, callback, this);
	}
WebDataSource.prototype.sel = 
	function (option, args) 
	{
		var pars = this.params(args);
		var method = 'POST';
		var callback = 
			function (response)
			{
				this.notifyAll('select:ds:' + this.getName() + ':' + option.getHandlerName(), this, this.parseSelect(response));
			};
		var r = this.request(this.getSelectUrl(), pars, method, callback, this);
	}
WebDataSource.prototype.del = 
	function (option, args) {
		var pars = this.params(args);
		var method = 'POST';
		var callback = 
			function (response)
			{
				this.notifyAll('delete:ds:' + this.getName() + ':' + option.getHandlerName(), this, this.parseDelete(response));
			};
		var r = this.request(this.getDeleteUrl(), pars, method, callback, this);
	}

function ReadOnlyProperty(val)
{
	var m_val = val;
	this.get = function() { return m_val; }
}

function Property(val)
{
	var m_val = val;
	this.get = function() { return m_val; }
	this.set = function(n_val) { m_val = n_val; }
}
function Entry(value)
{
	var v = value;
	this.getValue = function () { return v; }
}
function IndexedEntry(id, value)
{
	var i = id;
	var v = value;
	this.getIndex = function () { return i; }
	this.getValue = function () { return v; }
}

function FlyweightFactory()
{
	console.log('creating flyweight factory');
	var i = {};
	var n = new Array();
	this.getItems = function () { return i; }
	this.getNames = function () { return n; }
}

FlyweightFactory.prototype.contains =
	function (key) { return this.getItems()[key] != null; }

FlyweightFactory.prototype.put = 
	function (key, value) {
		if (key == null) {
			console.log('key cannot be null');
			return;
		} else if (value == null) {
			console.log('value cannot be null');
		} else if (key.className != 'String') {
			console.log('key must be a String');
			return;
		}
		this.getItems()[key] = value;
		this.getNames().push(key);
	}

FlyweightFactory.prototype.get =
	function (key) { return this.getItems()[key]; }

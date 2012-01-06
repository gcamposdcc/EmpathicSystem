Function.prototype.subclassOf = function( parentClassOrObject ){ 
	if ( parentClassOrObject.constructor == Function ) 
	{ 
		//Normal Inheritance 
		this.prototype = new parentClassOrObject;
		this.prototype.constructor = this;
		this.prototype.parent = parentClassOrObject.prototype;
	} 
	else 
	{ 
		//Pure Virtual Inheritance 
		this.prototype = parentClassOrObject;
		this.prototype.constructor = this;
		this.prototype.parent = parentClassOrObject;
	} 
	return this;
} 

function getObjectClass(obj) {
    if (obj && obj.constructor && obj.constructor.toString) {
        var arr = obj.constructor.toString().match(
            /function\s*(\w+)/);

        if (arr && arr.length == 2) {
            return arr[1];
        }
    }

    return undefined;
}

function ObservableContext ()
{
	this.observers = new Array();
	this.addObserver = 
		function (observer) 
		{
			if (this.observers.indexOf(observer) == -1) this.observers.push(observer);
		}
	this.removeObserver = 
		function (observer) 
		{
			var index = this.observers.indexOf(observer);
			if (index != -1) {
				this.observers.splice(index, 1);
			}
		}
	this.notify = 
		function (observer, task, source, value)
		{
			if (observer != null) {
				observer.update(task, source, value);
			}
		}
	this.notifyAll = 
		function (task, source, value)
		{
			for (o_index in this.observers) {
				notify(observer, task, source, value);
			}
		}
}

function ObserverContext ()
{
	this.handlers = {};
	this.addHandler = 
		function (task, handler)
		{
			if (!existsHandlerFor(task)) {
				this.handlers[task] = handler;
			}
		}
	this.removeHandler = 
		function (task)
		{
			if (existsHandlerFor(task)) {
				delete handlers[task];
			}
		}
	this.existsHandlerFor =
		function (task)
		{
			return this.handlers[task] != null;
		}
	this.update = 
		function (task, source, value)
		{
			if (existsHandlerFor(task)) {
				this.handlers[task](source, value);
			}
		}
}
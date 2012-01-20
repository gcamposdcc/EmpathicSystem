Function.prototype.subclassOf = function( parentClassOrObject ){ 
	if ( parentClassOrObject.constructor == Function )  { 
		//Normal Inheritance 
		this.prototype = new parentClassOrObject;
		this.prototype.constructor = this;
		this.prototype.parent = parentClassOrObject.prototype;
	}  else  { 
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
			if (this.observers.indexOf(observer) == -1) {
				console.log("adding observer");
				this.observers.push(observer);
			}
		}
	this.removeObserver = 
		function (observer) 
		{
			var index = this.observers.indexOf(observer);
			if (index != -1) {
				console.log("removing observer");
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
			console.log("notifying all observers of task " + task);
			for (o_index in this.observers) {
				this.notify(this.observers[o_index], task, source, value);
			}
		}
}

function ObserverContext ()
{
	this.handlers = {};
	this.addHandler = 
		function (task, handler)
		{
			// if (!this.existsHandlerFor(task)) {
				console.log("setting handler for '" + task + "'");
				this.handlers[task] = handler;
			// }
		}
	this.removeHandler = 
		function (task)
		{
			if (this.existsHandlerFor(task)) {
				console.log("removing handler for '" + task + "'");
				delete this.handlers[task];
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
			if (this.existsHandlerFor(task)) {
				console.log("handling task '"+task + "'");
				this.handlers[task].call(this, source, value);
			} else {
				console.log("no handler for task '"+task + "'");
			}
		}
}

function MessengerContext(){
	ObservableContext.call(this);
	ObserverContext.call(this);
}

function assert(value, expected)
{
	if (value == expected) console.log('all ok: ' + value + " == " + expected);
	else alert('not ok: ' + value + " != " + expected);
}

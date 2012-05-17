package cl.automind.empathy.data;

import cl.automind.empathy.data.IQueryOption.Type;

public abstract class QueryOptions {
	public final static IQueryOption ALL = new QueryOption(IQueryOption.Type.All){
		@Override public int getValue(){
			return IQueryOption.Type.All.getDefaultValue();
		}
	};
	public final static IQueryOption FILTER = new QueryOption(IQueryOption.Type.Filter){
		@Override public int getValue(){
			return IQueryOption.Type.Filter.getDefaultValue();
		}
	};
	public final static IQueryOption NONE = new QueryOption(IQueryOption.Type.None);

	static public IQueryOption get(){
		return new QueryOption();
	}
	static public IQueryOption get(Type type){
		return new QueryOption(type);
	}
	static public IQueryOption get(Type type, int value){
		return new QueryOption(type, value);
	}
}

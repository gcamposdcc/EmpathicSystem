package cl.automind.empathy.data;

public abstract class QueryCriterions {
	static public <T> IQueryCriterion<T> get(T value){
		return new QueryCriterion<T>(value, new NamedValuePair<?>[0]);
	}
	static public <T> IQueryCriterion<T> get(T value, NamedValuePair<?>... params){
		return new QueryCriterion<T>(value, params);
	}
}

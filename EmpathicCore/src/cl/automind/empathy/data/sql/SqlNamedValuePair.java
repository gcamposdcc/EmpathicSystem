package cl.automind.empathy.data.sql;


import java.sql.PreparedStatement;

import cl.automind.empathy.data.NamedValuePair;
import cl.automind.empathy.data.Pairs;


public abstract class SqlNamedValuePair<V> extends NamedValuePair<V> {
	private ComparisonType comparison;
	public SqlNamedValuePair(String key, V value){
		super(key, value);
		this.comparison = ComparisonType.Equal;
	}
	public SqlNamedValuePair(String key, V value, ComparisonType comparison){
		super(key, value);
		this.comparison = comparison;
	}

	abstract public void set(PreparedStatement statement, int position);

	public void setComparison(ComparisonType comparison) {
		this.comparison = comparison;
	}

	public ComparisonType getComparison() {
		return comparison;
	}

	public abstract Pairs.Type getType();

	@Override
	public String toString(){
		String result = "";
		switch (getComparison()){
		case Equal:
			result = getKey() + " = " + getValue() + " "; break;
		case Distinct:
			result = getKey() + " <> " + getValue() + " "; break;
		case Greater:
			result = getKey() + " > " + getValue() + " "; break;
		case GreaterOrEqual:
			result = getKey() + " >= " + getValue() + " "; break;
		case Lesser:
			result = getKey() + " < " + getValue() + " "; break;
		case LesserOrEqual:
			result = getKey() + " <= " + getValue() + " "; break;
		case ContainedIn:
			result = getKey() + " = " + getValue() + " "; break;
		case Contains:
			result = getKey() + " = " + getValue() + " "; break;
		case StartsWith:
			result = getKey() + " = " + getValue() + " "; break;
		case EndsWith:
			result = getKey() + " = " + getValue() + " "; break;
		default:
			result = getKey() + " = " + getValue() + " "; break;
		}
		return result;
	}
}

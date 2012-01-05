package cl.automind.empathy.data.sql;


import java.sql.PreparedStatement;

import cl.automind.empathy.data.NamedValuePair;
import cl.automind.empathy.data.Pairs;


public abstract class SqlNamedValuePair<V> extends NamedValuePair<V> {
	private ComparisonType comparison;
	public SqlNamedValuePair(String key, V value){
		super(key, value);
		setComparison(ComparisonType.Equal);
	}
	public SqlNamedValuePair(String key, V value, ComparisonType comparison){
		super(key, value);
		setComparison(comparison);
	}

	abstract public void set(PreparedStatement s, int position);

	public void setComparison(ComparisonType comparison) {
		this.comparison = comparison;
	}

	public ComparisonType getComparison() {
		return comparison;
	}

	public abstract Pairs.Type getType();

	@Override
	public String toString(){
		switch (getComparison()){
		case Equal:
			return getKey() + " = " + getValue() + " ";
		case Distinct:
			return getKey() + " <> " + getValue() + " ";
		case Greater:
			return getKey() + " > " + getValue() + " ";
		case GreaterOrEqual:
			return getKey() + " >= " + getValue() + " ";
		case Lesser:
			return getKey() + " < " + getValue() + " ";
		case LesserOrEqual:
			return getKey() + " <= " + getValue() + " ";
		case ContainedIn:
			return getKey() + " = " + getValue() + " ";
		case Contains:
			return getKey() + " = " + getValue() + " ";
		case StartsWith:
			return getKey() + " = " + getValue() + " ";
		case EndsWith:
			return getKey() + " = " + getValue() + " ";
		default:
			return getKey() + " = " + getValue() + " ";
		}
	}
}

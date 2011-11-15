package cl.automind.empathy.data.sql;

import java.sql.PreparedStatement;

import util.NamedValuePair;

public abstract class SqlNamedValuePair<V> extends NamedValuePair<V> {

	public SqlNamedValuePair(String key, V value){
		super(key, value);
	}

	abstract public void set(PreparedStatement s, int position);

}

package cl.automind.empathy.data.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import cl.automind.empathy.data.Pairs;

public abstract class SqlPairs {

	static public final SqlNamedValuePair<?>[] EMPTY = new SqlNamedValuePair<?>[]{};
	static public SqlNamedValuePair<Boolean> pair(String key, Boolean value){
		return new SqlPairs.SqlBoolean(key, value);
	}
	static public SqlNamedValuePair<Boolean> pair(String key, Boolean value, ComparisonType type){
		return new SqlPairs.SqlBoolean(key, value, type);
	}
	static public SqlNamedValuePair<java.sql.Date> pair(String key, java.sql.Date value){
		return new SqlPairs.SqlDate(key, value);
	}
	static public SqlNamedValuePair<java.sql.Date> pair(String key, java.sql.Date value, ComparisonType type){
		return new SqlPairs.SqlDate(key, value, type);
	}
	static public SqlNamedValuePair<Short> pair(String key, Short value){
		return new SqlPairs.SqlShort(key, value);
	}
	static public SqlNamedValuePair<Short> pair(String key, Short value, ComparisonType type){
		return new SqlPairs.SqlShort(key, value, type);
	}
	static public SqlNamedValuePair<Integer> pair(String key, Integer value){
		return new SqlPairs.SqlInt(key, value);
	}
	static public SqlNamedValuePair<Integer> pair(String key, Integer value, ComparisonType type){
		return new SqlPairs.SqlInt(key, value, type);
	}
	static public SqlNamedValuePair<Long> pair(String key, Long value){
		return new SqlPairs.SqlLong(key, value);
	}
	static public SqlNamedValuePair<Long> pair(String key, Long value, ComparisonType type){
		return new SqlPairs.SqlLong(key, value, type);
	}
	static public SqlNamedValuePair<Float> pair(String key, Float value){
		return new SqlPairs.SqlFloat(key, value);
	}
	static public SqlNamedValuePair<Float> pair(String key, Float value, ComparisonType type){
		return new SqlPairs.SqlFloat(key, value, type);
	}
	static public SqlNamedValuePair<Double> pair(String key, Double value){
		return new SqlPairs.SqlDouble(key, value);
	}
	static public SqlNamedValuePair<Double> pair(String key, Double value, ComparisonType type){
		return new SqlPairs.SqlDouble(key, value, type);
	}
	static public SqlNamedValuePair<String> pair(String key, String value){
		return new SqlPairs.SqlString(key, value);
	}
	static public SqlNamedValuePair<String> pair(String key, String value, ComparisonType type){
		return new SqlPairs.SqlString(key, value, type);
	}

	static public class SqlBoolean extends SqlNamedValuePair<Boolean>{

		public SqlBoolean(String key, Boolean value, ComparisonType type) {
			super(key, value, type);
		}

		public SqlBoolean(String key, Boolean value) {
			super(key, value);
		}

		@Override
		public void setIn(PreparedStatement statement, int position) {
			try {
				statement.setBoolean(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		@Override
		public String toString(){
			return getKey() + (getComparison() == ComparisonType.Distinct ? " <> " : " = ")+ getValue() + " ";
		}

		@Override
		public Pairs.Type getType() {
			return Pairs.Type.Boolean;
		}

	}
	static public class SqlDate extends SqlNamedValuePair<java.sql.Date>{

		public SqlDate(String key, java.sql.Date value) {
			super(key, value);
		}

		public SqlDate(String key, java.sql.Date value, ComparisonType type) {
			super(key, value, type);
		}

		@Override
		public void setIn(PreparedStatement statement, int position) {
			try {
				statement.setDate(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Pairs.Type getType() {
			return Pairs.Type.Date;
		}
	}
	static public class SqlString extends SqlNamedValuePair<String>{

		public SqlString(String key, String value) {
			super(key, value);
		}

		public SqlString(String key, String value, ComparisonType type) {
			super(key, value, type);
		}

		@Override
		public void setIn(PreparedStatement statement, int position) {
			try {
				statement.setString(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		@Override
		public String toString(){
			switch (getComparison()){
			case Distinct:
				return getKey() + " NOT LIKE " + "'" + getValue() + "'" + " ";
			case Contains:
				return getKey() + " LIKE " + "'%" + getValue() + "%'" + " ";
			case StartsWith:
				return getKey() + " LIKE " + "'" + getValue() + "%'" + " ";
			case EndsWith:
				return getKey() + " LIKE " + "'%" + getValue() + "'" + " ";
			default:
				return getKey() + " LIKE " + "'" + getValue() + "'" + " ";
			}
		}

		@Override
		public Pairs.Type getType() {
			return Pairs.Type.String;
		}
	}
	static public class SqlFloat extends SqlNamedValuePair<Float>{

		public SqlFloat(String key, Float value) {
			super(key, value);
		}

		public SqlFloat(String key, Float value, ComparisonType type) {
			super(key, value, type);
		}

		@Override
		public void setIn(PreparedStatement statement, int position) {
			try {
				statement.setFloat(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Pairs.Type getType() {
			return Pairs.Type.Float;
		}
	}
	static public class SqlDouble extends SqlNamedValuePair<Double>{

		public SqlDouble(String key, Double value) {
			super(key, value);
		}

		public SqlDouble(String key, Double value, ComparisonType type) {
			super(key, value, type);
		}

		@Override
		public void setIn(PreparedStatement statement, int position) {
			try {
				statement.setDouble(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Pairs.Type getType() {
			return Pairs.Type.Double;
		}
	}
	static public class SqlShort extends SqlNamedValuePair<Short>{

		public SqlShort(String key, Short value) {
			super(key, value);
		}

		public SqlShort(String key, Short value, ComparisonType type){
			super(key, value, type);
		}

		@Override
		public void setIn(PreparedStatement s, int position) {
			try {
				s.setShort(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Pairs.Type getType() {
			return Pairs.Type.Short;
		}
	}
	static public class SqlInt extends SqlNamedValuePair<Integer>{

		public SqlInt(String key, Integer value) {
			super(key, value);
		}

		public SqlInt(String key, Integer value, ComparisonType type) {
			super(key, value, type);
		}

		@Override
		public void setIn(PreparedStatement s, int position) {
			try {
				s.setInt(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Pairs.Type getType() {
			return Pairs.Type.Integer;
		}
	}
	static public class SqlLong extends SqlNamedValuePair<Long>{

		public SqlLong(String key, Long value) {
			super(key, value);
		}

		public SqlLong(String key, Long value, ComparisonType type) {
			super(key, value, type);
		}

		@Override
		public void setIn(PreparedStatement s, int position) {
			try {
				s.setLong(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Pairs.Type getType() {
			return Pairs.Type.Long;
		}
	}

}

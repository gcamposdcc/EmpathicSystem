package cl.automind.empathy.data.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Sql {

	public enum Type {
		Boolean, Char, Date, Short, Integer, Long, Float, Double, String
	}

	static public SqlNamedValuePair<Boolean> pair(String key, Boolean value){
		return new Sql.SqlBoolean(key, value);
	}
	static public SqlNamedValuePair<Boolean> pair(String key, Boolean value, ComparisonType type){
		return new Sql.SqlBoolean(key, value, type);
	}
	static public SqlNamedValuePair<Date> pair(String key, Date value){
		return new Sql.SqlDate(key, value);
	}
	static public SqlNamedValuePair<Date> pair(String key, Date value, ComparisonType type){
		return new Sql.SqlDate(key, value, type);
	}
	static public SqlNamedValuePair<Short> pair(String key, Short value){
		return new Sql.SqlShort(key, value);
	}
	static public SqlNamedValuePair<Short> pair(String key, Short value, ComparisonType type){
		return new Sql.SqlShort(key, value, type);
	}
	static public SqlNamedValuePair<Integer> pair(String key, Integer value){
		return new Sql.SqlInt(key, value);
	}
	static public SqlNamedValuePair<Integer> pair(String key, Integer value, ComparisonType type){
		return new Sql.SqlInt(key, value, type);
	}
	static public SqlNamedValuePair<Long> pair(String key, Long value){
		return new Sql.SqlLong(key, value);
	}
	static public SqlNamedValuePair<Long> pair(String key, Long value, ComparisonType type){
		return new Sql.SqlLong(key, value, type);
	}
	static public SqlNamedValuePair<Float> pair(String key, Float value){
		return new Sql.SqlFloat(key, value);
	}
	static public SqlNamedValuePair<Float> pair(String key, Float value, ComparisonType type){
		return new Sql.SqlFloat(key, value, type);
	}
	static public SqlNamedValuePair<Double> pair(String key, Double value){
		return new Sql.SqlDouble(key, value);
	}
	static public SqlNamedValuePair<Double> pair(String key, Double value, ComparisonType type){
		return new Sql.SqlDouble(key, value, type);
	}
	static public SqlNamedValuePair<String> pair(String key, String value){
		return new Sql.SqlString(key, value);
	}
	static public SqlNamedValuePair<String> pair(String key, String value, ComparisonType type){
		return new Sql.SqlString(key, value, type);
	}

	static public class SqlBoolean extends SqlNamedValuePair<Boolean>{

		public SqlBoolean(String key, Boolean value, ComparisonType type) {
			super(key, value, type);
		}

		public SqlBoolean(String key, Boolean value) {
			super(key, value);
		}

		@Override
		public void set(PreparedStatement s, int position) {
			try {
				s.setBoolean(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		@Override
		public String toString(){
			switch (getComparison()){
			case Distinct:
				return getKey() + " <> " + getValue() + " ";
			default:
				return getKey() + " = " + getValue() + " ";
			}
		}

		@Override
		public Sql.Type getType() {
			return Sql.Type.Boolean;
		}

	}
	static public class SqlDate extends SqlNamedValuePair<Date>{

		public SqlDate(String key, Date value) {
			super(key, value);
		}

		public SqlDate(String key, Date value, ComparisonType type) {
			super(key, value, type);
		}

		@Override
		public void set(PreparedStatement s, int position) {
			try {
				s.setDate(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Sql.Type getType() {
			return Sql.Type.Date;
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
		public void set(PreparedStatement s, int position) {
			try {
				s.setString(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		@Override
		public String toString(){
			switch (getComparison()){
			case Equal:
				return getKey() + " LIKE " + "'" + getValue() + "'" + " ";
			case Distinct:
				return getKey() + " NOT LIKE " + "'" + getValue() + "'" + " ";
			case Greater:
				return getKey() + " LIKE " + "'" + getValue() + "'" + " ";
			case GreaterOrEqual:
				return getKey() + " LIKE " + "'" + getValue() + "'" + " ";
			case Lesser:
				return getKey() + " LIKE " + "'" + getValue() + "'" + " ";
			case LesserOrEqual:
				return getKey() + " LIKE " + "'" + getValue() + "'" + " ";
			case ContainedIn:
				return getKey() + " LIKE " + "'" + getValue() + "'" + " ";
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
		public Sql.Type getType() {
			return Sql.Type.String;
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
		public void set(PreparedStatement s, int position) {
			try {
				s.setFloat(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Sql.Type getType() {
			return Sql.Type.Float;
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
		public void set(PreparedStatement s, int position) {
			try {
				s.setDouble(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Sql.Type getType() {
			return Sql.Type.Double;
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
		public void set(PreparedStatement s, int position) {
			try {
				s.setShort(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Sql.Type getType() {
			return Sql.Type.Short;
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
		public void set(PreparedStatement s, int position) {
			try {
				s.setInt(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Sql.Type getType() {
			return Sql.Type.Integer;
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
		public void set(PreparedStatement s, int position) {
			try {
				s.setLong(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Sql.Type getType() {
			return Sql.Type.Long;
		}
	}

}
package cl.automind.empathy.data.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlTypes {

	static public SqlNamedValuePair<Boolean> pair(String key, Boolean value){
		return new SqlTypes.SqlBoolean(key, value);
	}
	static public SqlNamedValuePair<Date> pair(String key, Date value){
		return new SqlTypes.SqlDate(key, value);
	}
	static public SqlNamedValuePair<Short> pair(String key, Short value){
		return new SqlTypes.SqlShort(key, value);
	}
	static public SqlNamedValuePair<Integer> pair(String key, Integer value){
		return new SqlTypes.SqlInt(key, value);
	}
	static public SqlNamedValuePair<Long> pair(String key, Long value){
		return new SqlTypes.SqlLong(key, value);
	}
	static public SqlNamedValuePair<Float> pair(String key, Float value){
		return new SqlTypes.SqlFloat(key, value);
	}
	static public SqlNamedValuePair<Double> pair(String key, Double value){
		return new SqlTypes.SqlDouble(key, value);
	}
	static public SqlNamedValuePair<String> pair(String key, String value){
		return new SqlTypes.SqlString(key, value);
	}

	static public class SqlBoolean extends SqlNamedValuePair<Boolean>{

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
	}
	static public class SqlDate extends SqlNamedValuePair<Date>{

		public SqlDate(String key, Date value) {
			super(key, value);
		}

		@Override
		public void set(PreparedStatement s, int position) {
			try {
				s.setDate(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	static public class SqlString extends SqlNamedValuePair<String>{

		public SqlString(String key, String value) {
			super(key, value);
		}

		@Override
		public void set(PreparedStatement s, int position) {
			try {
				s.setString(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	static public class SqlFloat extends SqlNamedValuePair<Float>{

		public SqlFloat(String key, Float value) {
			super(key, value);
		}

		@Override
		public void set(PreparedStatement s, int position) {
			try {
				s.setFloat(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	static public class SqlDouble extends SqlNamedValuePair<Double>{

		public SqlDouble(String key, Double value) {
			super(key, value);
		}

		@Override
		public void set(PreparedStatement s, int position) {
			try {
				s.setDouble(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	static public class SqlShort extends SqlNamedValuePair<Short>{

		public SqlShort(String key, Short value) {
			super(key, value);
		}

		@Override
		public void set(PreparedStatement s, int position) {
			try {
				s.setShort(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	static public class SqlInt extends SqlNamedValuePair<Integer>{

		public SqlInt(String key, Integer value) {
			super(key, value);
		}

		@Override
		public void set(PreparedStatement s, int position) {
			try {
				s.setInt(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	static public class SqlLong extends SqlNamedValuePair<Long>{

		public SqlLong(String key, Long value) {
			super(key, value);
		}

		@Override
		public void set(PreparedStatement s, int position) {
			try {
				s.setLong(position, getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
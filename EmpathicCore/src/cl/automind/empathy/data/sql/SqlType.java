package cl.automind.empathy.data.sql;


public enum SqlType {
	SHORT {
		@Override public String getSqlName() { return "SHORT"; }
	},
	INTEGER {
		@Override public String getSqlName() { return "INTEGER"; }
	},
	LONG {
		@Override public String getSqlName() { return "LONG"; }
	},
	FLOAT {
		@Override public String getSqlName() { return "FLOAT"; }
	},
	DOUBLE {
		@Override public String getSqlName() { return "DOUBLE"; }
	},
	BOOLEAN {
		@Override public String getSqlName() { return "BOOLEAN"; }
	},
	TEXT {
		@Override public String getSqlName() { return "TEXT"; }
	},
	VARCHAR {
		@Override public String getSqlName() { return "VARCHAR"; }
	},
	NONE;
	public String getSqlName(){
		return "none";
	}
	public static SqlType inferSqlType(Class<?> fieldClass){
		try{
			if (fieldClass.isPrimitive()){
				if (fieldClass.isAssignableFrom(Integer.TYPE)) return INTEGER;
				else if (fieldClass.isAssignableFrom(Double.TYPE)) return DOUBLE;
				else if (fieldClass.isAssignableFrom(Boolean.TYPE)) return BOOLEAN;
				else if (fieldClass.isAssignableFrom(Short.TYPE)) return SHORT;
				else if (fieldClass.isAssignableFrom(Long.TYPE)) return LONG;
				else if (fieldClass.isAssignableFrom(Float.TYPE)) return FLOAT;
			} else {
				if (fieldClass.isAssignableFrom(String.class)) return VARCHAR;
				else fieldClass.isAssignableFrom(java.sql.Date.class);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return SqlType.NONE;
	}
}

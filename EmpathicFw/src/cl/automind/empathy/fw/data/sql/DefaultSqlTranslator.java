package cl.automind.empathy.fw.data.sql;

import cl.automind.empathy.data.sql.ISqlTranslator;
import cl.automind.empathy.data.sql.SqlType;

public class DefaultSqlTranslator implements ISqlTranslator {

	@Override
	public String toSqlType(SqlType type) {
		return type.getSqlName();
	}

}

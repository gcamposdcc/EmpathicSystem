package cl.automind.empathy.testquery;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cl.automind.empathy.data.sql.ISqlConnector;
import cl.automind.empathy.data.sql.SqlPairs;
import cl.automind.empathy.data.sql.SqlMetadata;
import cl.automind.empathy.data.sql.SqlNamedValuePair;
import cl.automind.empathy.fw.data.sql.AbstractSqlDataSource;

@SqlMetadata(name = "empathy")
public class EmpathySqlDataSource extends AbstractSqlDataSource<EmpathyMetadata>{

	public EmpathySqlDataSource(EmpathyMetadata template, ISqlConnector connector) {
		super(template, connector);
	}

	@Override
	public List<EmpathyMetadata> parse(ResultSet rs) {
		List<EmpathyMetadata> list = new ArrayList<EmpathyMetadata>();
		try {
			ResultSetMetaData metadata = rs.getMetaData();
			int columns =  metadata.getColumnCount();
			while (rs.next()){
				EmpathyMetadata sagdeMetadata = new EmpathyMetadata();
				for (int i = 1; i <= columns; i++){
					if (metadata.getColumnName(i).equals("id")){
						sagdeMetadata.setId(rs.getInt(i));
					} else if (metadata.getColumnName(i).equals("idsource")){
						sagdeMetadata.setIdsource(rs.getInt(i));
					} else if (metadata.getColumnName(i).equals("idtype")){
						sagdeMetadata.setIdtype(rs.getInt(i));
					} else if (metadata.getColumnName(i).equals("evaluated")){
						sagdeMetadata.setEvaluated(rs.getBoolean(i));
					} else if (metadata.getColumnName(i).equals("answered")){
						sagdeMetadata.setAnswered(rs.getBoolean(i));
					} else if (metadata.getColumnName(i).equals("liked")){
						sagdeMetadata.setLiked(rs.getBoolean(i));
					} else if (metadata.getColumnName(i).equals("created_at")){
						sagdeMetadata.setCreatedAt(rs.getDate(i));
					}
				}
				list.add(sagdeMetadata);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Collection<SqlNamedValuePair<?>> toPairs(EmpathyMetadata target) {
		Collection<SqlNamedValuePair<?>> pairs = new ArrayList<SqlNamedValuePair<?>>();
		pairs.add(SqlPairs.pair("id", target.getId()));
		pairs.add(SqlPairs.pair("idsource", target.getIdsource()));
		pairs.add(SqlPairs.pair("idtype", target.getIdtype()));
		pairs.add(SqlPairs.pair("evaluated", target.isEvaluated()));
		pairs.add(SqlPairs.pair("answered", target.isAnswered()));
		pairs.add(SqlPairs.pair("liked", target.isLiked()));
		pairs.add(SqlPairs.pair("created_at", target.getCreatedAt()));
		return pairs;
	}

}

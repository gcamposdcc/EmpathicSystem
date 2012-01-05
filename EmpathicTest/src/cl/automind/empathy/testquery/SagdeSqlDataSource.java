package cl.automind.empathy.testquery;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cl.automind.empathy.data.sql.ISqlConnector;
import cl.automind.empathy.data.sql.NamedQueries;
import cl.automind.empathy.data.sql.NamedQuery;
import cl.automind.empathy.data.sql.SqlPairs;
import cl.automind.empathy.data.sql.SqlMetadata;
import cl.automind.empathy.data.sql.SqlNamedValuePair;
import cl.automind.empathy.fw.data.sql.AbstractSqlDataSource;

@SqlMetadata(name = "empathy_info_sagde",
		queries = @NamedQueries(
				queries = {
						@NamedQuery (name = "byIdEmpathy", query = "SELECT * FROM empathy_info_sagde WHERE idempathy = ?;"),
						@NamedQuery (name = "byIdSector", query = "SELECT * FROM empathy_info_sagde WHERE idsector = ?;")})
)
public class SagdeSqlDataSource extends AbstractSqlDataSource<SagdeMetadata>{
	public SagdeSqlDataSource(SagdeMetadata template, ISqlConnector connector) {
		super(template, connector);
	}

	@Override
	public List<SagdeMetadata> parse(ResultSet rs) {
		List<SagdeMetadata> list = new ArrayList<SagdeMetadata>();
		try {
			ResultSetMetaData metadata = rs.getMetaData();
			int columns =  metadata.getColumnCount();
			while (rs.next()){
				SagdeMetadata sagdeMetadata = new SagdeMetadata();
				for (int i = 1; i <= columns; i++){
					if (metadata.getColumnName(i).equals("idsector")){
						sagdeMetadata.setIdSector(rs.getInt(i));
					} else if (metadata.getColumnName(i).equals("ideje")){
						sagdeMetadata.setIdAxis(rs.getInt(i));
					} else if (metadata.getColumnName(i).equals("idcmo")){
						sagdeMetadata.setIdCmo(rs.getInt(i));
					} else if (metadata.getColumnName(i).equals("idobjcon")){
						sagdeMetadata.setIdKo(rs.getInt(i));
					} else if (metadata.getColumnName(i).equals("idnivel")){
						sagdeMetadata.setIdLevel(rs.getInt(i));
					} else if (metadata.getColumnName(i).equals("idempathy")){
						sagdeMetadata.setIdEmpathy(rs.getInt(i));
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
	public Collection<SqlNamedValuePair<?>> toPairs(SagdeMetadata target) {
		Collection<SqlNamedValuePair<?>> pairs = new ArrayList<SqlNamedValuePair<?>>();
		pairs.add(SqlPairs.pair("idsector", target.getIdSector()));
		pairs.add(SqlPairs.pair("ideje", target.getIdAxis()));
		pairs.add(SqlPairs.pair("idcmo", target.getIdCmo()));
		pairs.add(SqlPairs.pair("idobjcon", target.getIdKo()));
		pairs.add(SqlPairs.pair("idnivel", target.getIdLevel()));
		pairs.add(SqlPairs.pair("idempathy", target.getIdEmpathy()));
		return pairs;
	}
}

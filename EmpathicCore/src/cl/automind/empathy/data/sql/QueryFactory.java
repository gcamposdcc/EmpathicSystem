package cl.automind.empathy.data.sql;

import java.util.ArrayList;
import java.util.Collection;

import cl.automind.empathy.data.IDataSource;

public class QueryFactory {
	public Collection<NamedQuery> buildQueries(IDataSource<?> datasource){
		Collection<NamedQuery> queries = new ArrayList<NamedQuery>();
		return queries;
	}
}

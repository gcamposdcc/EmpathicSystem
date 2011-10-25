package cl.automind.empathy.query;

import java.util.Collection;

public interface IQuery<T> {
	public Collection<T> execute(IQueryFilter... filters);
	public Collection<T> execute(Collection<IQueryFilter> filters, IQueryOption... options);
}

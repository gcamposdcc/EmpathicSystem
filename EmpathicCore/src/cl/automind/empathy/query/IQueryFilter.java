package cl.automind.empathy.query;

public interface IQueryFilter {
	public boolean eval(String key, Object value);
}

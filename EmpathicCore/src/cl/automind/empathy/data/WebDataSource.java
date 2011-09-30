package cl.automind.empathy.data;

public abstract class WebDataSource<T> implements IDataSource<T> {
	public enum Format {Plain, XML, JSON}
}

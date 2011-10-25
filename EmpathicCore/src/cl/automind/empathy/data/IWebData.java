package cl.automind.empathy.data;

public interface IWebData<T> {
	public T fromWebString(String text);
	public String toWebString();
}

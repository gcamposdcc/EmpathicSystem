package cl.automind.empathy.data.web;

public interface IWebData<T> {
	public T fromWebString(String text);
	public String toWebString();
}

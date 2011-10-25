package cl.automind.connectivity;

public interface IHttpClient {
	public abstract void setUrl(String url);
	public abstract void addRequestParameter(String key, String value);
	public abstract void sendRequest();
	abstract public String getRequestString();
	public abstract String getResponse();
}

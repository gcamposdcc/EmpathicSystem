package net;

public interface IHttpClient {
	abstract public void setUrl(String url);
	abstract public void addRequestParameter(String key, String value);
	abstract public String sendRequest();
	abstract public String getRequestString();
	abstract public String getResponse();
}

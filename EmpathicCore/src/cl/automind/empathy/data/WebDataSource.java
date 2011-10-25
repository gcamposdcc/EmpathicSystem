package cl.automind.empathy.data;

public abstract class WebDataSource<T> implements IDataSource<T> {
	public enum Format {Plain, XML, JSON}
	private String targetUrl;
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
}

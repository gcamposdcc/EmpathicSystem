package gcampos.dev.net;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHttpClient implements gcampos.dev.net.IHttpClient{
	private String url;
	private Map<String, String> params = new HashMap<String, String>();

	@Override
	public void setUrl(String url) {
		this.url = url;
	}
	protected String getUrl(){
		return url;
	}
	protected Map<String, String> getParams(){
		return params;
	}

	@Override
	public void addRequestParameter(String key, String value) {
		getParams().put(key, value);
	}

	protected String getRequestParamString(){
		StringBuilder builder = new StringBuilder("");
        for (Map.Entry<String, String> entry : getParams().entrySet()){
        	builder.append(entry.getKey());
        	builder.append("=");
//        	builder.append(entry.getValue());
        	builder.append(escape(entry.getValue()));
        	builder.append("&");
        }
        if (builder.length() > 0) builder.delete(builder.length() - 1, builder.length());
        String request = builder.toString();
        System.out.println("Using::" + request);
        return request;
	}
	@Override
	public String getRequestString() {
		return getUrl() + "?" + getRequestParamString();
	}
	public String escape(String s){
		return Escaper.escape(s);
	}
	
}

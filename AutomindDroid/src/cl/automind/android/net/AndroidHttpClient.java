package cl.automind.android.net;

import gcampos.dev.net.AbstractHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

public class AndroidHttpClient extends AbstractHttpClient{
	private String response = "";
	@Override
	public String sendRequest() {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	    HttpClient httpclient = new DefaultHttpClient(params);
	    HttpPost httppost = new HttpPost(getUrl());
	    BufferedReader in = null;
	    String answer = "EmptyResponse";
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        for (Map.Entry<String, String> pair: getParams().entrySet()){
	        	nameValuePairs.add(new BasicNameValuePair(pair.getKey(), pair.getValue()));
	        }
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        in = new BufferedReader
            (new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String page = sb.toString();
            Logger.global.info(page);
	        setResponse(page);
	        return page;
	    } catch (Exception e){
	    	e.printStackTrace();
	    	answer = (e.getMessage()+"::"+e.getLocalizedMessage()+"::"+e.getCause());
	    	for (StackTraceElement stackTrace : e.getStackTrace()){
	    		answer += "::" + stackTrace.toString();
	    	}
	    } finally {
            if (in != null) {
                try {
                    in.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                }
            }
	    }
		return answer;
	}
	protected String setResponse(String response){
		String temp = this.response;
		this.response = response;
		return temp;
	}
	@Override
	public String getResponse() {
		return response;
	}

}

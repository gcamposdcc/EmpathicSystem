package gcampos.dev.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;



public class SimpleHttpClient extends AbstractHttpClient{

	private String response;

	protected void setResponse(String response){
		this.response = response;
	}

	@Override
	public String sendRequest() {
        try {

            // Send the request
            URL url = new URL(getUrl()+getRequestParamString());
            URLConnection conn = url.openConnection();
        	Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(""+conn.getURL());
            conn.connect();

            Map<String, List<String>> headers = conn.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()){
            	String log = "";
            	log += "Header::"+entry.getKey()+" -> ";
            	for(String s : entry.getValue()) log += s+";";
            	Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(log);
            }

            // Get the response
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("NewResponseLine::"+line);
            }
            reader.close();

            // Output the response
            setResponse("Response::"+answer.toString());
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(answer.toString());
            return answer.toString();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
	}

	@Override
	public String getResponse() {
		return response;
	}

}

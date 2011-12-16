package gcampos.dev.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;



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
        	System.out.println(conn.getURL());
            conn.connect();

            Map<String, List<String>> headers = conn.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()){
            	System.out.print("Header::"+entry.getKey()+" -> ");
            	for(String s : entry.getValue()) System.out.print(s+";");
            	System.out.println();
            }

            // Get the response
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
                System.out.println("NewResponseLine::"+line);
            }
            reader.close();

            // Output the response
            setResponse("Response::"+answer.toString());
            System.out.println(answer.toString());
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

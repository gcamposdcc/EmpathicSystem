package cl.automind.desk.connectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import cl.automind.connectivity.AbstractHttpClient;

public class DeskHttpClient extends AbstractHttpClient{

	private String response;

	protected void setResponse(String response){
		this.response = response;
	}

	@Override
	public void sendRequest() {
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

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	@Override
	public String getResponse() {
		return response;
	}

}

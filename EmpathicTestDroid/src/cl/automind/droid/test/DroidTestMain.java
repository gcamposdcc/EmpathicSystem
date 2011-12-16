package cl.automind.droid.test;

import gcampos.dev.net.IHttpClient;
import gcampos.dev.util.NamedValuePair;

import java.util.logging.Logger;

import cl.automind.android.net.AndroidHttpClient;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DroidTestMain extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    @Override
    protected void onStart() {
    	super.onStart();
		((TextView)findViewById(R.id.TextView01)).setText("Hola");
		Log.w("Message", "DebugTest");
		Logger.global.warning("DebugTest");
    }

	@SuppressWarnings("unchecked")
	public void actionButton(View view){
		(new WebRequest()).execute();
	}

	public void button01(View view){

	}

	public void button02(View view){

	}

	class WebRequest extends AsyncTask<NamedValuePair<String>, String, Object>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Object doInBackground(NamedValuePair<String>... params) {
			String result = "NoResponse";
			try{
				IHttpClient client = new AndroidHttpClient();
				client.setUrl("http://www.google.cl");
				for (NamedValuePair<String> pair: params){
					client.addRequestParameter(pair.getKey(), pair.getValue());
				}

				client.setUrl("http://192.168.1.102:8080/TestWebService/CmoPerformance");
				client.addRequestParameter("idcmo", "333");
				client.addRequestParameter("idestablishment", "181");
				client.addRequestParameter("user", "196324577");

				result = client.sendRequest();
				publishProgress(result);
			} catch (Exception e){
				publishProgress(e.getMessage()+"::"+e.getLocalizedMessage()+"::"+e.getCause());
			}
			return result;
		}
		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			if (values.length > 0){
				((TextView)findViewById(R.id.TextView01)).setText(values[0]);
			}
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
		}

	}
}
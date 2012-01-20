package cl.automind.empathy.test;

import gcampos.dev.util.concurrent.AsyncTask;

import java.util.logging.Logger;

public class TestTask extends AsyncTask<Integer, String, String> {

	@Override
	protected String doInBackground(Integer... params) {
		int id = params[0];
		int cycles = params[1];

		for (int i = 0; i < cycles; i++){
//			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("BackThread"+"Id:" + id + "::Cycle:" + i);
			publishProgress("Id:"+id, "Cycle:"+i);
		}

		return "Id" + id + "::Complete";
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
//		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("MainThread" + values[0] + "::" + values[1]);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("MainThread" + result);
	}

}

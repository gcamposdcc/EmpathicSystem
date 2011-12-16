package cl.automind.empathy.test;

import gcampos.dev.util.concurrent.AsyncTask;

public class TestTask extends AsyncTask<Integer, String, String> {

	@Override
	protected String doInBackground(Integer... params) {
		int id = params[0];
		int cycles = params[1];

		for (int i = 0; i < cycles; i++){
//			System.out.println("BackThread"+"Id:" + id + "::Cycle:" + i);
			publishProgress("Id:"+id, "Cycle:"+i);
		}

		return "Id" + id + "::Complete";
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
//		System.out.println("MainThread" + values[0] + "::" + values[1]);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		System.out.println("MainThread" + result);
	}

}

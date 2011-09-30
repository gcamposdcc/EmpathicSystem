package cl.automind.empathy.test.t00;

import cl.automind.empathy.fw.data.DefaultDataManager;

public class TestDataManager extends DefaultDataManager {
	public final static String DS_SCORE = "scores";
	public TestDataManager() {
		super();
		createDataSource(DS_SCORE, new Score(0,0));
	}
}

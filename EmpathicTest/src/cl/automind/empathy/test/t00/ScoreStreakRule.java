package cl.automind.empathy.test.t00;

import java.util.List;

import cl.automind.empathy.feedback.AbstractMessage;
import cl.automind.empathy.rule.AbstractRule;
import cl.automind.empathy.rule.RuleMetadata;

@RuleMetadata(minVal = 0, maxVal = 10, threshold = 3)
public class ScoreStreakRule extends AbstractRule {
	private final static Score dummyScore = new Score(0,0);
	private static final String STREAK = "streak";
	public ScoreStreakRule(){
		super();
		setMessage(new ScoreStreakRuleMessage());
	}
	@Override
	public boolean canEvaluate(Object... params) {
		int count = countAllInSource(TestDataManager.DS_SCORE);
		return count > 3;
	}

	@Override
	public double evaluateImpl(Object... params) {
		int total = 0;
		List<Score> scores = getAllInSource(TestDataManager.DS_SCORE, dummyScore);
		for (int i = scores.size() - 1; i >= 0; i--){
//			System.out.println("ScoreStreak::Score"+i+"::Value::"+scores.get(i).getValue());
			if (scores.get(i).getValue() > 0) total++;
			else break;
		}
		if (total > 0) {
			clearValues();
			putValue(STREAK, total);
		}
		return total;
	}
	public class ScoreStreakRuleMessage extends AbstractMessage{

		@Override
		public String getName() {
			return "mScoreStreak00";
		}

		@Override
		public String getEmotionName() {
			return "happiness";
		}

		@Override
		public String getUnfilteredText() {
			return "Acabas de anotar @streak puntos consecutivos!!!";
		}
	}
}

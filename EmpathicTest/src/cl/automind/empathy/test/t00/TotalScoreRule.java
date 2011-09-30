package cl.automind.empathy.test.t00;

import java.util.Collection;

import cl.automind.empathy.feedback.AbstractMessage;
import cl.automind.empathy.rule.AbstractRule;
import cl.automind.empathy.rule.RuleMetadata;

@RuleMetadata(minVal = 0, maxVal = 10, threshold = 0)
public class TotalScoreRule extends AbstractRule {
	private final static Score dummyScore = new Score(0,0);

	public TotalScoreRule(){
		super();
		setMessage(new TotalScoreRuleMessage());
	}
	@Override
	public boolean canEvaluate(Object... params) {
		int count = countAllInSource(TestDataManager.DS_SCORE);
		return count > 3;
	}

	@Override
	public double evaluateImpl() {
		int total = 0;
		Collection<Score> scores = getAllInSource(TestDataManager.DS_SCORE, dummyScore);
		for (Score score: scores){
			total += score.getValue();
		}
		putValue("score", total);
		return total;
	}

	@Override
	public AbstractMessage getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}

	public class TotalScoreRuleMessage extends AbstractMessage{

		@Override
		public String getName() {
			return "mTotalScore00";
		}

		@Override
		public String getEmotionName() {
			return "happiness";
		}

		@Override
		public String getUnfilteredText() {
			return "Llevas @score puntos!!!";
		}
	}
}

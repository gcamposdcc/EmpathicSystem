package cl.automind.empathy.test;

import java.util.Calendar;

import cl.automind.empathy.feedback.AbstractMessage;
import cl.automind.empathy.rule.AbstractRule;
import cl.automind.empathy.rule.RuleMetadata;

@RuleMetadata(name = "sample_name", minVal = 0, threshold = 800, maxVal = 1000)
public class SampleRule extends AbstractRule {
	private final AbstractMessage message = new SampleMessage();
	@Override
	public boolean canEvaluate(Object... params) {
		return Calendar.getInstance().get(Calendar.MONTH) == Calendar.APRIL;
	}
	@Override
	public double evaluateImpl(Object... params) {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (day == 23) {
			putValue("valor", "el método putValue()");
			return 1000;
		}
		else return 0;
	}
	@Override
	public AbstractMessage getMessage() {
		return message;
	}

	private static class SampleMessage extends AbstractMessage {
		@Override
		public String getName() {
			return "SampleMessageName";
		}
		@Override
		public String getEmotionName() {
			return "happiness";
		}
		@Override
		public String getUnfilteredText() {
			return "Este mensaje es de prueba," +
					"puedes poner valores con " + key("valor");
		}
	}
}

package cl.automind.empathy.feedback;

public class EmptyMessage extends AbstractMessage{

	@Override
	public String getName() {
		return "EMPTY_MESSAGE";
	}

	@Override
	public String getEmotionName() {
		return "EMPTY_EMOTION";
	}

	@Override
	public String getUnfilteredText() {
		return "This is the default message, remember to override this message by assigning value to the preferredMessage in your rule: "+ getContext().getCallingRuleName();
	}

}

package cl.automind.empathy.feedback;

public class ForcedMessage extends AbstractMessage{
	private final String message;
	public ForcedMessage(String message){
		super();
		this.message = message;
		getContext().setCallingRuleName("NoRule");
	}
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
		return message;
	}

}

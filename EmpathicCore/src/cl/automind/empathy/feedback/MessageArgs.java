package cl.automind.empathy.feedback;

import java.util.Map;


public abstract class MessageArgs {
	private String emotionName;
	private String text;
	
	protected MessageArgs(String emotionName, String text, Map<String, Object> vars){
		setEmotionName(emotionName);
		setText(text);
	}
	
	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}

	public void setEmotionName(String emotionName) {
		this.emotionName = emotionName;
	}

	public String getEmotionName() {
		return emotionName;
	}
	
}

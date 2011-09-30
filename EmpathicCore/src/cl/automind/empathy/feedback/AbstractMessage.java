package cl.automind.empathy.feedback;

import java.util.HashMap;
import java.util.Map;

import interfaces.structural.INamed;

public abstract class AbstractMessage implements INamed{
	public final static String KEY_PREFIX = "@";
	public final static String KEY_SUFFIX = "";
	public final static String key(String s){
		return KEY_PREFIX+s+KEY_SUFFIX;
	}
	private Map<String, Object> defaultValues = new HashMap<String, Object>();
	/**
	 * @return The codename of the EmpathicMessage, this should be unique.
	 */
	@Override public abstract String getName();
	/**
	 * @return The message that will be shown to the player
	 */
	public final String getText(){
		return getText(getDefaultValues());
	}
	/**
	 * @return The message that will be shown to the player
	 */
	public final String getText(Map<String, Object> values){
		return filterMessage(values);
	}
	/**
	 * @return The codename of the EmpathicEmotion that will be displayed with this message.
	 */
	public abstract String getEmotionName();

	public abstract String getUnfilteredText();

	protected String filterMessage(Map<String, Object> values){
		String text = getUnfilteredText();
		for (String key: values.keySet()){
			text = text.replaceAll(KEY_PREFIX + key + KEY_SUFFIX, values.get(key).toString());
		}
		return text;
	}
	public void setDefaultValues(Map<String, Object> defaultValues) {
		this.defaultValues = defaultValues;
	}
	public Map<String, Object> getDefaultValues() {
		return defaultValues;
	}
}

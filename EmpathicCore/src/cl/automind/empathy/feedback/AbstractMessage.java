package cl.automind.empathy.feedback;

import gcampos.dev.interfaces.structural.INamed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMessage implements INamed{
	public final static String KEY_PREFIX = "@";
	public final static String KEY_SUFFIX = "@";
	public final static String key(String value){
		return KEY_PREFIX + value + KEY_SUFFIX;
	}

	private final Context context = new Context();

	public final Context getContext(){
		return this.context;
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
	public String getEmotionName(){
		return "happiness";
	}

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

	static public class Context{
		private String callingRuleName = "";
		private List<Object> data = new ArrayList<Object>();

		public void setCallingRuleName(String rulename){
			this.callingRuleName = rulename;
		}
		public String getCallingRuleName(){
			return callingRuleName;
		}
		public void setData(List<Object> data) {
			this.data = data;
		}
		public List<Object> getData() {
			return data;
		}
		public Context duplicate(){
			AbstractMessage.Context output =  new AbstractMessage.Context();
			output.setCallingRuleName(getCallingRuleName());
			for (Object data: getData()){
				output.getData().add(data);
			}
			return output;
		}
	}
}

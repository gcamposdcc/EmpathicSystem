package cl.automind.empathy.ui;

import java.util.Set;

import cl.automind.empathy.feedback.AbstractEmotion;
import cl.automind.empathy.feedback.AbstractMessage;

public interface IUiManager {

	public abstract void displayMessage(AbstractMessage message);

	public abstract void hideCurrentMessage();

	public abstract void registerEmotion(String emotionName, AbstractEmotion emotion);

	public abstract AbstractEmotion getEmotion(String emotionName);

	public abstract Set<String> getAllEmotionnames();

	public abstract void printElements();

	public void registerMessageOcurrence(AbstractMessage.Context context, boolean evaluated, boolean answered, boolean liked);

}
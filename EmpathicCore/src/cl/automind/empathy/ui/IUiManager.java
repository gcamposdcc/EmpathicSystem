package cl.automind.empathy.ui;

import java.util.Set;

import cl.automind.empathy.feedback.AbstractEmotion;
import cl.automind.empathy.feedback.AbstractMessage;

public interface IUiManager {

	void displayMessage(AbstractMessage message);

	void hideCurrentMessage();

	void registerEmotion(String emotionName, AbstractEmotion emotion);

	AbstractEmotion getEmotion(String emotionName);

	Set<String> getAllEmotionnames();

	void printElements();

	void registerMessageOcurrence(AbstractMessage.Context context, boolean evaluated, boolean answered, boolean liked);

}
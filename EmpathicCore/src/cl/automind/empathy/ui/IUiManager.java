package cl.automind.empathy.ui;

import java.util.Set;

import cl.automind.empathy.feedback.AbstractEmotion;
import cl.automind.empathy.feedback.AbstractMessage;
/**
 * The interface for the implementation of the classes responsible of
 * showing the feedback to the user. It provides method for showing and hidding messages. 
 * @author Guillermo
 */
public interface IUiManager {
	/**
	 * Displays feedback to the user based on a specific {@link AbstractMessage}
	 * @param message The message from which the feedback will be extracted
	 */
	void displayMessage(AbstractMessage message);
	/**
	 * Hides the current feedback that is beeing shown (if any);
	 */
	void hideCurrentMessage();
	/**
	 * Registers a new {@link AbstractEmotion} with the name {@code emotionName}
	 * @param emotionName The name of the new emotion
	 * @param emotion The emotion to be registered
	 */
	void registerEmotion(String emotionName, AbstractEmotion emotion);
	/**
	 * Gets the {@link AbstractEmotion} registered with the name {@code emotionName}.
	 * If no emotion has been registered with such name, then returns <b>null</b>.
	 * @param emotionName The name to search the emotion
	 * @return The emotion registered with the given name, <b>null</b> if no emotion
	 * is registered with that name
	 */
	AbstractEmotion getEmotion(String emotionName);

    /**
     * Gets a {@link Set} of {@link String} containing the names of all the {@link AbstractEmotion}
     * registered in this {@link IUiManager}
     * @return The names of all the rules
     */
	Set<String> getAllEmotionnames();
	/**
	 * Prints all the emotions registered.
	 */
	void printElements();
	/**
	 * Triggers the register of the current feedback event.
	 * This method should only be used as a trigger and should not contain any logic 
	 * that performs the registry of the event.
	 * @param context The context where this feedback event has been originated
	 * @param evaluated <b>true</b> if asked the user to evaluate the feedback <b>false</b> if not
	 * @param answered <b>true</b> if the user answered when asked to evaluate the feedback <b>false</b> if not
	 * @param liked <b>true</b> if the user evaluated positively the feedback <b>false</b> if not
	 */
	void registerMessageOcurrence(AbstractMessage.Context context, boolean evaluated, boolean answered, boolean liked);

}
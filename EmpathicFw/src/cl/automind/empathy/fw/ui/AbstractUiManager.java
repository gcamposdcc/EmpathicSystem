package cl.automind.empathy.fw.ui;

import gcampos.dev.patterns.creational.FlyweightNamedFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import cl.automind.empathy.feedback.AbstractEmotion;
import cl.automind.empathy.feedback.AbstractMessage;
import cl.automind.empathy.ui.IUiManager;

public abstract class AbstractUiManager implements IUiManager {


	public AbstractUiManager(){
		emotionFactory = new EmpathicEmotionFactory();
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.ui.IUiManager#displayMessage(cl.automind.empathy.feedback.AbstractMessage)
	 */
	@Override
	public abstract void displayMessage(AbstractMessage message);
	/* (non-Javadoc)
	 * @see cl.automind.empathy.ui.IUiManager#hideCurrentMessage()
	 */
	@Override
	public abstract void hideCurrentMessage();


	// <emotion-factory>

	private final EmpathicEmotionFactory emotionFactory;

	protected EmpathicEmotionFactory initializeEmotionFactory(){
		return new EmpathicEmotionFactory();
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.ui.IUiManager#registerEmotion(java.lang.String, cl.automind.empathy.feedback.AbstractEmotion)
	 */
	@Override
	public void registerEmotion(String emotionName, AbstractEmotion emotion){
		getEmotionFactory().registerElement(emotionName, emotion);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.ui.IUiManager#getEmotion(java.lang.String)
	 */
	@Override
	public AbstractEmotion getEmotion(String emotionName){
		return getEmotionFactory().createElement(emotionName);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.ui.IUiManager#getAllEmotionnames()
	 */
	@Override
	public Set<String> getAllEmotionnames(){
		return getEmotionFactory().getAllEmotionnames();
	}
	protected EmpathicEmotionFactory getEmotionFactory(){
		return emotionFactory;
	}
	protected Collection<AbstractEmotion> ruleByFamily(String familyName){
		return getEmotionFactory().getEmotionsByFamily(familyName);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.ui.IUiManager#printElements()
	 */
	@Override
	public void printElements(){
		getEmotionFactory().printElements();
	}

	protected class EmpathicEmotionFactory extends FlyweightNamedFactory<AbstractEmotion>{

		@Override
		public Map<String, AbstractEmotion> initializeMap() {
			return new Hashtable<String, AbstractEmotion>();
		}
		public Collection<AbstractEmotion> getEmotionsByFamily(String strategyName){
			Collection<AbstractEmotion> emotions = getRegistry().values();
			Collection<AbstractEmotion> selectedEmotions = new ArrayList<AbstractEmotion>();
			for (AbstractEmotion emotion : emotions){
				if (emotion.hasFamily(strategyName)) {
					selectedEmotions.add(emotion);
				}
			}
			return selectedEmotions;
		}
		public Set<String> getAllEmotionnames(){
			return getRegistry().keySet();
		}
	}
	// </emotion-factory>
}

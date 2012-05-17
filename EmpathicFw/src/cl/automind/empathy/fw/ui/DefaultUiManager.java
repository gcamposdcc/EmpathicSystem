package cl.automind.empathy.fw.ui;

import java.util.logging.Logger;

import cl.automind.empathy.feedback.AbstractMessage;
import cl.automind.empathy.feedback.AbstractMessage.Context;

public class DefaultUiManager extends AbstractUiManager{

	private boolean showing;
	private AbstractMessage currentMessage;


	@Override
	public void displayMessage(AbstractMessage message) {
		doDisplay(message);
	}

	private synchronized void doDisplay(AbstractMessage message){
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("= MessageRequest");
		if (isShowing()){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("== RequestFailed::ShowingMessage::"+message.getName());
			return;
		}
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("= StartingMessage");
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("==  ShowingMessage::"+message.getName());
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("==  ShowingMessage::"+message.getUnfilteredText());
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("==  ShowingMessage::"+message.getEmotionName());
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("==  ShowingMessage::"+message.getText());
		Thread t = new Thread(){
			@Override public void run(){
				AbstractMessage message = getCurrentMessage();
				try{ Thread.sleep(5000); }
				catch(Exception e){ Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(e.getMessage());}
				finally {hideMessage(message); }
			}
		};
		setShowing(true);
		setCurrentMessage(message);
		t.start();
	}

	@Override
	public void hideCurrentMessage() {
		doHide();
	}

	private void hideMessage(AbstractMessage message){
		doHide(message);
	}
	private synchronized void doHide(){
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("= HiddingMessage");
		setShowing(false);
		setCurrentMessage(null);
	}
	private synchronized void doHide(AbstractMessage message){
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("= HiddingMessage");
		if (message == getCurrentMessage()){
			doHide();
		}
	}
	public void setShowing(boolean showing) {
		this.showing = showing;
	}

	public boolean isShowing() {
		return showing;
	}

	public void setCurrentMessage(AbstractMessage currentMessage) {
		this.currentMessage = currentMessage;
	}

	public AbstractMessage getCurrentMessage() {
		return currentMessage;
	}

	@Override
	public void registerMessageOcurrence(Context context, boolean evaluated, boolean answered, boolean liked) {
		// TODO Auto-generated method stub

	}
}

package cl.automind.empathy.fw.ui;

import cl.automind.empathy.feedback.AbstractMessage;

public class DefaultUiManager extends AbstractUiManager{

	private boolean showing;
	private AbstractMessage currentMessage;
	
	
	@Override
	public void displayMessage(AbstractMessage message) {
		doDisplay(message);
	}
	
	private synchronized void doDisplay(AbstractMessage message){
		System.out.println("= MessageRequest");
		if (isShowing()){
			System.out.println("== RequestFailed::ShowingMessage::"+message.getName());
			return;
		}
		System.out.println("= StartingMessage");
		System.out.println("==  ShowingMessage::"+message.getName());
		System.out.println("==  ShowingMessage::"+message.getUnfilteredText());
		System.out.println("==  ShowingMessage::"+message.getEmotionName());
		System.out.println("==  ShowingMessage::"+message.getText());
		Thread t = new Thread(){
			@Override public void run(){
				AbstractMessage message = getCurrentMessage();
				try{ Thread.sleep(5000); }
				catch(Exception e){}
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
		System.out.println("= HiddingMessage");
		setShowing(false);
		setCurrentMessage(null);
	}
	private synchronized void doHide(AbstractMessage message){
		System.out.println("= HiddingMessage");
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
}

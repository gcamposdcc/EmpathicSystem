package patterns.behavioral;

public interface IObserver<T> {
	public void onSuscription(T data);
	public void onNew(T data);
	public void onCompletition();
	
}

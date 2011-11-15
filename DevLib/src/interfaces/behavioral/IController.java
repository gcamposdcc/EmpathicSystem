package interfaces.behavioral;

import patterns.behavioral.ICommand;

public interface IController<T extends ICommand>  {
	public void setCommand(T loginCommand);
	public T getCommand();
	public void executeTask(Class<? extends T> commandType, Object... params);
}

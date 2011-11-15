package patterns.behavioral;

public interface ICommand extends IExecutable{
	public boolean canExecute(Object... params);
}

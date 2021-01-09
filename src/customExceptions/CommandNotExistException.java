package customExceptions;

public class CommandNotExistException extends Exception {
	public CommandNotExistException(String commandName){
        super("The command: '" + commandName + "' is not exist.");
    }
}

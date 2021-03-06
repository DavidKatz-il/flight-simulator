package commands;

import customExceptions.NumOfArgsException;
import expressions.Expression;
import interpreter.Utilities;

import java.util.List;

public class DisconnectCommand extends CommandBase{
    int numOfArgs = 1;
    @Override
    public Expression execute(List<String> args) throws Exception {
        Utilities.addMessage("bye");
        Thread.sleep(110);
        return () -> 0;
    }

    @Override
    public void checkArgs(List<String> args) throws Exception {
        if (args.size() != this.numOfArgs)
            throw new NumOfArgsException(this.getClass().getName(), this.numOfArgs, args.size());
    }
}

package commands;
import customExceptions.*;
import expressions.Expression;

import java.util.List;

public abstract class CommandBase implements Command {
    protected int numOfArgs;

    @Override
    public abstract Expression execute(List<String> args) throws Exception;

    @Override
    public void checkArgs(List<String> args) throws Exception {
        if (args.size() != this.numOfArgs)
            throw new NumOfArgsException(this.getClass().getName(), this.numOfArgs, args.size());
    }
}

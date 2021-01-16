package commands;

import customExceptions.NumOfArgsException;
import expressions.Expression;

import java.util.List;

public class SleepCommand extends CommandBase{

    public SleepCommand() {
        this.numOfArgs = 2;
    }

    @Override
    public Expression execute(List<String> args) throws Exception {
        try {
            Thread.sleep(Long.parseLong(args.get(1)));
        }
        catch(Exception ignored) {}
        return () -> 0;
    }

    public void checkArgs(List<String> args) throws Exception {
        if (this.numOfArgs != args.size())
            throw new NumOfArgsException(this.getClass().getName(), this.numOfArgs, args.size());
    }
}

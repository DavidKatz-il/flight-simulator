package commands;

import customExceptions.NumOfArgsException;
import expressions.Expression;
import interpeter.CalcExpression;
import network.Client;

import java.util.List;

public class ConnectCommand extends CommandBase{
    int numOfArgs = 3;

    @Override
    public Expression execute(List<String> args) {
        String host = args.get(1);
        int port = (int) CalcExpression.parseExpression(args.get(2)).calculate();
        new Client(host, port);
        return () -> 0;
    }

    @Override
    public void checkArgs(List<String> args) throws Exception {
        if (args.size() != this.numOfArgs)
            throw new NumOfArgsException(this.getClass().getName(), this.numOfArgs, args.size());
    }

}

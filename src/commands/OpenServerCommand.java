package commands;

import customExceptions.NumOfArgsException;
import expressions.Expression;
import interpeter.CalcExpression;
import interpeter.Utilities;
import network.Server;

import java.util.List;

public class OpenServerCommand extends CommandBase{
    int numOfArgs = 3;

    @Override
    public Expression execute(List<String> args) {
        Utilities.stop = false;
        int port = (int) CalcExpression.parseExpression(args.get(1)).calculate();
        int sleep = (int) CalcExpression.parseExpression(args.get(2)).calculate();
        if (!Utilities.isPortExist(port))
            new Server(port, sleep);
        Utilities.addPort(port);
        return () -> 0;
    }

    @Override
    public void checkArgs(List<String> args) throws Exception {
        if (args.size() != this.numOfArgs)
            throw new NumOfArgsException(this.getClass().getName(), this.numOfArgs, args.size());
    }

}

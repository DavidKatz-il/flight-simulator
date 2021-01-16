package commands;

import expressions.Expression;
import interpreter.Utilities;

import java.util.List;

public class PrintCommand extends CommandBase{

    @Override
    public Expression execute(List<String> args) throws Exception {
        String ex = String.join("", args.subList(1, args.size()));
        if (Utilities.isVarExist(ex))
            ex = "" + Utilities.getVarSymbol(ex).calculate();
        System.out.println(ex);
        return () -> 0;
    }
    public void checkArgs(List<String> args) throws Exception {

    }
}

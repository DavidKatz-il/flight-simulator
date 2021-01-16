package commands;

import customExceptions.NumOfArgsException;
import expressions.Expression;
import expressions.Symbol;
import interpreter.Utilities;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DefineVarCommand extends CommandBase {

    public DefineVarCommand() {
        this.numOfArgs = 2;
    }

    @Override
    public Expression execute(List<String> args) throws Exception {

        if ((args.get(0).equals("var")) && args.get(1).contains("=")) {
            args = Stream.of(args.get(1).split("=")).collect(Collectors.toList());
            args.add(1, "=");
        }
        args.removeIf(s->s.equals("var"));
        String var = args.get(0);
        Utilities.setVarSymbol(var, new Symbol());
        if (args.contains("="))
            Utilities.getCommand("=").execute(args);

        return Utilities.getVarSymbol(var);
    }

    @Override
    public void checkArgs(List<String> args) throws Exception {
        if (args.size() < this.numOfArgs)
            throw new NumOfArgsException(this.getClass().getName(), this.numOfArgs, args.size());
    }

}

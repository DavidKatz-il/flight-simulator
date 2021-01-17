package commands;

import expressions.Expression;
import expressions.Symbol;
import interpreter.CalcExpression;
import interpreter.Utilities;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlacementCommand extends CommandBase{

    @Override
    public Expression execute(List<String> args) {
        if (args.get(0).contains("="))
            args = Stream.of(args.get(0).split("=")).collect(Collectors.toList());

        String var = args.get(0);
        if (!Utilities.isVarExist(var))
            return () -> -1;

        args.removeIf(s->s.equals("=") || s.equals(var));

        Symbol symbol = Utilities.getVarSymbol(var);
        if (args.get(0).equals("bind")){
            symbol.setSimulator(args.get(1));
        }
        else {
            String str = String.join("", args);
            double val = CalcExpression.parseExpression(str).calculate();
            symbol.setValue(val);
            String simulator = symbol.getSimulator();
            if ((simulator) != null)
                Utilities.addMessage("set " + simulator + " " + val);
        }
        return Utilities.getVarSymbol(var);
    }

    @Override
    public void checkArgs(List<String> args) throws Exception {
    }

}

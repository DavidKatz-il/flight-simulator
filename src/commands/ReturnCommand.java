package commands;

import expressions.Expression;
import interpreter.CalcExpression;

import java.util.List;

public class ReturnCommand extends CommandBase {

    @Override
    public Expression execute(List<String> args) {
        String ex = String.join("", args.subList(1, args.size()));
        return CalcExpression.parseExpression(ex);
    }

    @Override
    public void checkArgs(List<String> args) throws Exception {

    }
}

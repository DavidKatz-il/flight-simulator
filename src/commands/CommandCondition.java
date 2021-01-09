package commands;

import expressions.Expression;
import interpeter.CalcExpression;
import interpeter.Lexer;
import interpeter.Utilities;

import java.util.LinkedList;
import java.util.List;

public abstract class CommandCondition implements Command {
    Lexer lexer = new Lexer();
    List<Command> commands;
    List<List<String>> cmd_args;
    protected int numOfArgs;

    @Override
    public abstract Expression execute(List<String> arg) throws Exception;

    @Override
    public void checkArgs(List<String> args) throws Exception {
    }

    public void generateFlow(List<String> lines) {
        commands = new LinkedList<>();
        cmd_args = new LinkedList<>();

        for (String line: lines) {
            List<String> tokens = this.lexer.lexer(line.replaceAll("\\s+", ""));
            String cmdName = tokens.get(0);
            if (cmdName.contains("=") || ((tokens.size() > 1) && tokens.get(1).equals("="))) {
                cmdName = "=";
            }
            if (Utilities.isCommandExist(cmdName)){
                commands.add(Utilities.getCommand(cmdName));
                cmd_args.add(tokens);
            }
        }
    }

    public boolean isCondition(String leftValue, String condition, String rightValue){
        double left = CalcExpression.parseExpression(leftValue).calculate();
        double right = CalcExpression.parseExpression(rightValue).calculate();

        switch(condition) {
            case "<":
                return left < right;
            case "<=":
                return left <= right;
            case ">":
                return left > right;
            case ">=":
                return left >= right;
            case "==":
                return left == right;
            case "!=":
                return left != right;
            default:
                return false;
        }
    }
}

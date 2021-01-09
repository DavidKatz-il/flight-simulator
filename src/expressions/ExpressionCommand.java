package expressions;

import commands.Command;

import java.util.List;

public class ExpressionCommand implements Expression {

    Command command;
    List<String> args;

    public ExpressionCommand(Command command, List<String> args){
        this.command = command;
        this.args = args;
    }

    @Override
    public double calculate() {
        return 0;
    }

//    @Override
//    public Expression calculate() {
//        try {
//            return this.command.execute(this.args);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return () -> 0;
//    }
}

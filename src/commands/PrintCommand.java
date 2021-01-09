package commands;

import expressions.Expression;

import java.util.List;

public class PrintCommand extends CommandBase{

    @Override
    public Expression execute(List<String> args) throws Exception {
        args.forEach(System.out::println);
        return () -> 0;
    }
    public void checkArgs(List<String> args) throws Exception {

    }
}

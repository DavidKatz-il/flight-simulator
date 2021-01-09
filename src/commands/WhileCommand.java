package commands;

import expressions.Expression;

import java.util.List;

public class WhileCommand extends CommandCondition{

    @Override
    public Expression execute(List<String> args) throws Exception {
        String[] cond = args.get(0).split(" ");
        generateFlow(args.subList(1, args.size()-1));
        while (isCondition(cond[1], cond[2], cond[3])) {
            for (int i = 0; i<commands.size(); i++) {
                commands.get(i).execute(cmd_args.get(i));
            }
        }
        return () -> 0;
    }

    @Override
    public void checkArgs(List<String> args) throws Exception {

    }

}

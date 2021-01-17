package interpreter;

import commands.*;
import expressions.Expression;
import java.util.Arrays;


public class MyInterpreter {

    public MyInterpreter() {
        Utilities.setCommand("print", new PrintCommand());
        Utilities.setCommand("return", new ReturnCommand());
        Utilities.setCommand("sleep", new SleepCommand());
        Utilities.setCommand("var", new DefineVarCommand());
        Utilities.setCommand("=", new PlacementCommand());
        Utilities.setCommand("while", new WhileCommand());
        Utilities.setCommand("openDataServer", new OpenServerCommand());
        Utilities.setCommand("connect", new ConnectCommand());
        Utilities.setCommand("disconnect", new DisconnectCommand());
    }

    public int start(String text) {
        Expression result = null;
        Parser parser = new Parser();
        Parser.ParsedData info = parser.parse(Arrays.asList(text.split("\\n")));
        if (!info.errors.isEmpty()){
            System.out.println("List of Errors:");
            System.out.println(info.errors.toString());
            return -1;
        }
        while (!info.cmdQ.isEmpty()) {
            try {
                Command cmd = info.cmdQ.poll();
                result = cmd.execute(info.argsQ.poll());
            } catch (Exception e) { e.printStackTrace(); }
        }
        try {
            Thread.sleep(110);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Utilities.stop = true;
        if (result != null)
            return (int) result.calculate();
        else
            return -1;
    }

}

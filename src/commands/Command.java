package commands;

import expressions.Expression;

import java.util.List;

public interface Command {

    public Expression execute(List<String> arg) throws Exception;
    public void checkArgs(List<String> args) throws Exception;

}

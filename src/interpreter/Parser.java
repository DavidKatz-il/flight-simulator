package interpreter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import commands.*;
import customExceptions.CommandNotExistException;


public class Parser {
    Lexer lexer = new Lexer();

    public static class ParsedData {
        public Queue<Command> cmdQ = new LinkedList<Command>();
        public Queue<List<String>> argsQ = new LinkedList<List<String>>();
        public Queue<String> errors = new LinkedList<String>();
    }

    public ParsedData parse(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        return parseLines(lines);
    }

    public ParsedData parse(List<String> lines) {
        return parseLines(lines);
    }

    private ParsedData parseLines(List<String> lines) {
        ParsedData parsedData = new ParsedData();
        String line = null;
        Command cmd;
        String cmdName = null;
        List<String> tokens = null;
        boolean condition = false;


        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            line = lines.get(lineIndex);

            if (line.trim().length() == 0)
                continue;

            try {
                if (line.matches("(while|if).*")) {
                    tokens = new LinkedList<String>();
                    cmdName = line.split(" ")[0];
                    condition = true;
                }
                if (condition) {
                    tokens.add(line);
                    if (line.startsWith("}")) {
                        condition = false;
                        parsedData.argsQ.add(tokens);
                        parsedData.cmdQ.add(Utilities.getCommand(cmdName));
                    }
                    continue;
                }

                tokens = this.lexer.lexer(line);
                cmdName = tokens.get(0);
                if (cmdName.contains("=") || ((tokens.size() > 1) && tokens.get(1).equals("="))) {
                    cmdName = "=";
                }

                if (!Utilities.isCommandExist(cmdName)) {
                    parsedData.errors.add(
                            "Line: " + lineIndex + " cause the following exception: "
                                    + new CommandNotExistException(cmdName).toString()
                    );
                    continue;
                }

                cmd = Utilities.getCommand(cmdName);
                cmd.checkArgs(tokens);

                parsedData.argsQ.add(tokens);
                parsedData.cmdQ.add(cmd);

                if (cmdName.startsWith("return"))
                    break;

            } catch (Exception e) {
                parsedData.errors.add(lineIndex + " : " + e.getMessage());
            }
        }

//        if (((line == null) || !line.startsWith("return")) && parsedData.errors.isEmpty()) {
//            parsedData.errors.add("Program must contain a return statement.");
//        }

        return parsedData;
    }
}
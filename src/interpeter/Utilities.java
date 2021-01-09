package interpeter;

import commands.Command;
import expressions.Symbol;
import expressions.Number;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;


public class Utilities {

    private static Map<String, Symbol> varToSymbolTable = new HashMap<String, Symbol>();
    private static Map<String, Command> commandTable = new HashMap<String, Command>();
    private static ConcurrentHashMap<String, Number> simToNumberTable = new ConcurrentHashMap<String, Number>();
    private static BlockingQueue<String> massages = new LinkedBlockingDeque<String>();
    private static HashSet<Integer> openPorts = new HashSet<Integer>();
    public static volatile boolean stop = false;
    public static final ClientStatus clientStatus = new ClientStatus();

    public static Symbol getVarSymbol(String var) {
        return varToSymbolTable.getOrDefault(var, null);
    }
    public static void setVarSymbol(String var, Symbol symbol){ varToSymbolTable.put(var, symbol); }
    public static boolean isVarExist(String var) {
        return varToSymbolTable.containsKey(var);
    }

    public static Number getSimNumber(String sim) {
        return simToNumberTable.getOrDefault(sim, null);
    }
    public static void setSimNumber(String sim, Number number){ simToNumberTable.put(sim, number); }
    public static boolean isSimExist(String sim) {
        return simToNumberTable.containsKey(sim);
    }

    public static Command getCommand(String name) {
        return commandTable.getOrDefault(name, null);
    }
    public static void setCommand(String name, Command cmd) {
        commandTable.put(name, cmd);
    }
    public static boolean isCommandExist(String name) {
        return commandTable.containsKey(name);
    }

    public static void addMassage(String massage) {massages.add(massage);}
    public static BlockingQueue<String> getMassages() { return massages;}
    public static String pollMassage() { return massages.poll();}

    public static boolean isPortExist(int port) { return openPorts.contains(port); }
    public static void addPort(int port) { openPorts.add(port); }
    public static class ClientStatus { public boolean connected = false; }
}

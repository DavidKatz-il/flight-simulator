package interpreter;

import commands.Command;
import expressions.Symbol;
import expressions.Number;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;


public class Utilities {

    private static Map<String, Symbol> varToSymbolTable = new HashMap<String, Symbol>();
    private static Map<String, Command> commandTable = new HashMap<String, Command>();
    private static ConcurrentHashMap<String, Number> simToNumberTable = new ConcurrentHashMap<String, Number>();
    private static BlockingQueue<String> messages = new LinkedBlockingDeque<String>();
    private static HashSet<Integer> openPorts = new HashSet<Integer>();
    public static volatile boolean stop = false;
    public static final ClientStatus clientStatus = new ClientStatus();

    private static final List<String> nodes = ReadXMLFile();
    public static List<String> getNodes() { return nodes; }

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

    public static void addMessage(String message) {messages.add(message);}
    public static BlockingQueue<String> getMessages() { return messages;}
    public static String pollMessage() { return messages.poll();}

    public static boolean isPortExist(int port) { return openPorts.contains(port); }
    public static void addPort(int port) { openPorts.add(port); }
    public static class ClientStatus { public boolean connected = false; }

    private static List<String> ReadXMLFile() {
        List<String> nodes = new ArrayList<String>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("./resources/generic_small.xml"));
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("node");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                nodes.add(node.getTextContent());
            }
        } catch (ParserConfigurationException | SAXException | IOException e) { e.printStackTrace();}
        return nodes;
    }
}
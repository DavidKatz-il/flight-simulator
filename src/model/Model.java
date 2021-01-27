package model;

import interpreter.MyInterpreter;
import interpreter.Utilities;
import network.Client;
import network.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Observable;
import java.util.stream.Collectors;


public class Model extends Observable {
    String mapPathSolution;
    Socket clientMap = null;
    MyInterpreter interpreter = new MyInterpreter();

    public void runScript(String text) {
        new Thread(()->interpreter.start(text)).start();
    }

    public void setThrottle(double val) {
        Utilities.addMessage("set /controls/engines/current-engine/throttle " + val);
    }

    public void setRudder(double val) {
        Utilities.addMessage("set /controls/flight/rudder " + val);
    }

    public void setAileron(double val) {
        Utilities.addMessage("set /controls/flight/aileron " + val);
    }

    public void setElevator(double val) {
        Utilities.addMessage("set /controls/flight/elevator " + val);
    }

    public void connectToSimulator(int serverPort, int serverSleep, String clientHost, int clientPort) {
        new Server(serverPort, serverSleep);
        new Client(clientHost, clientPort);
        synchronized (Utilities.clientStatus) {
            if (Utilities.clientStatus.connected)
                notifyObservers("connectedToSimulator");
        }
    }

    public void connectToSolver(String solverIp, int solverPort) {
        try {
            clientMap = new Socket(solverIp, solverPort);
            clientMap.setSoTimeout(3000);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void calcMap(Integer[][] matrix, int srcX, int srcY, int dstX, int dstY) {
        if (clientMap == null)
            return;

        try {
            PrintWriter out = new PrintWriter(clientMap.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientMap.getInputStream()));

            String result;
            for (Integer[] integers : matrix) {
                result = Arrays.stream(integers).map(Object::toString).collect(Collectors.joining(","));
                if (result.isEmpty())
                    continue;
                out.println(result);
                out.flush();
            }

            out.println("end");
            out.flush();

            out.println(srcX + "," + srcY);
            out.flush();
            out.println(dstX + "," + dstY);
            out.flush();
            mapPathSolution = in.readLine();
            out.close();
            in.close();
            clientMap.close();
            notifyObservers("done map calculate");

        } catch (IOException e) {
            System.out.println("Could not connect to the map server!");
            clientMap = null;
        }
    }

    public String getPath() {
        return this.mapPathSolution;
    }

    public double getPlaneX() {
        return 0.0;
        // TODO: add to generic small file
//        return Utilities.getSimNumber("/sim/current-view/viewer-x-m").calculate();
    }

    public double getPlaneY() {
        return 0.0;
        // TODO: add to generic small file
//        return Utilities.getSimNumber("/sim/current-view/viewer-y-m").calculate();
    }

}

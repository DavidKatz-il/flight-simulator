package model;

import interpreter.MyInterpreter;
import interpreter.Utilities;
import network.Client;
import network.Server;

import java.util.Observable;

public class Model extends Observable {
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
        synchronized (Utilities.clientStatus) {
            if (Utilities.clientStatus.connected)
                notifyObservers("connectedToSolver");
        }
    }

    public void calcMap(Integer[][] matrix, int srcX, int srcY, int dstX, int dstY) {
    }
}

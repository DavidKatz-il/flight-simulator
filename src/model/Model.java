package model;

import interpreter.MyInterpreter;
import interpreter.Utilities;

import java.util.Observable;

public class Model extends Observable {
    MyInterpreter interpreter = new MyInterpreter();

    public void runScript(String text) {
        new Thread(()->interpreter.start(text)).start();
    }

    public void setThrottle(double val) {
        Utilities.addMassage("set /controls/engines/current-engine/throttle " + val);
    }

    public void setRudder(double val) {
        Utilities.addMassage("set /controls/flight/rudder " + val);
    }

    public void setAileron(double val) {
        Utilities.addMassage("set /controls/flight/aileron " + val);
    }

    public void setElevator(double val) {
        Utilities.addMassage("set /controls/flight/elevator " + val);
    }
}

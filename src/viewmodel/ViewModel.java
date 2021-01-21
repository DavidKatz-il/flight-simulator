package viewmodel;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Model;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {
    Model model;

    public StringProperty scriptText, serverPort, serverSleep, clientIp, clientPort;
    public DoubleProperty aileron, elevator, throttle, rudder;


    public ViewModel(Model model) {
        this.model = model;

        serverPort = new SimpleStringProperty();
        serverSleep = new SimpleStringProperty();
        clientIp = new SimpleStringProperty();
        clientPort = new SimpleStringProperty();
        aileron = new SimpleDoubleProperty();
        elevator = new SimpleDoubleProperty();
        throttle = new SimpleDoubleProperty();
        rudder = new SimpleDoubleProperty();
        scriptText = new SimpleStringProperty();
    }

    public void updateAileronAndElevator() {
        model.setAileron(aileron.get());
        model.setElevator(elevator.get());
    }

    public void updateThrottle() {
        model.setThrottle(throttle.get());
    }

    public void updateRudder() {
        model.setRudder(rudder.get());
    }

    public void runScript() {
        model.runScript(scriptText.get());
    }

    public void connectSimulator() {
        model.connectToSimulator(
                (int)Double.parseDouble(serverPort.get()),
                (int)Double.parseDouble(serverSleep.get()),
                clientIp.get(),
                (int)Double.parseDouble(clientPort.get())
        );
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == model) {
            if (arg.equals("connectedToSimulator")) {
                notifyObservers("closePopUp");
            }
        }
    }
}

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

    public StringProperty scriptText;
    public DoubleProperty aileron, elevator, throttle, rudder;


    public ViewModel(Model model) {
        this.model = model;

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

    @Override
    public void update(Observable o, Object arg) {

    }
}

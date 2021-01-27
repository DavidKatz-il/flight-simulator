package viewmodel;

import javafx.beans.property.*;
import model.Model;
import view.MapCanvas;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {
    Model model;

    public StringProperty scriptText, serverPort, serverSleep, clientIp, clientPort, solverIp, solverPort, mapPath;
    public DoubleProperty aileron, elevator, throttle, rudder,simPlaneX,simPlaneY;

    public IntegerProperty planeX, planeY, destX, destY;
    public MapCanvas mapCanvas;


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
        solverIp = new SimpleStringProperty();
        solverPort = new SimpleStringProperty();
        planeX = new SimpleIntegerProperty();
        planeY = new SimpleIntegerProperty();
        destX = new SimpleIntegerProperty();
        destY = new SimpleIntegerProperty();
        mapCanvas = new MapCanvas();
        mapPath = new SimpleStringProperty();
        simPlaneX = new SimpleDoubleProperty();
        simPlaneY = new SimpleDoubleProperty();
        simPlaneX.set(model.getPlaneX());
        simPlaneY.set(model.getPlaneY());
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

    public void connectSolver() {
        model.connectToSolver(
                solverIp.get(),
                (int)Double.parseDouble(solverPort.get())
        );
    }

    public void calcMap(Integer[][] matrix) {
        model.calcMap(matrix, this.planeX.get(),this.planeY.get(),this.destX.get(),this.destY.get());
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == model) {
            if (arg.equals("connectedToSimulator") | arg.equals("connectedToSolver")) {
                notifyObservers("closePopUp");
            }
            else if(arg.equals("done map calculate")) {
                mapPath.set(model.getPath());
                notifyObservers("done map calculate");
            }
        }
    }

}

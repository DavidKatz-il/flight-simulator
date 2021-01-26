package viewmodel;

import javafx.beans.property.*;
import model.Model;
import view.MapCanvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {
    Model model;

    public StringProperty scriptText, serverPort, serverSleep, clientIp, clientPort, solverIp, solverPort;
    public DoubleProperty aileron, elevator, throttle, rudder;
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

    public void runClient(int port){
        Socket s=null;
        PrintWriter out=null;
        BufferedReader in=null;
        try{
            s=new Socket(solverIp.get(),(int)Double.parseDouble(solverPort.get()));
            s.setSoTimeout(3000);
            /*
            * need to fill in the logic here
            * */

        }catch(SocketTimeoutException e){
            System.out.println("\tYour Server takes over 3 seconds to answer");
        }catch(IOException e){
            System.out.println("\tYour Server ran into some IOException");
        }finally{
            /*try {
                in.close();
                out.close();
                s.close();
            } catch (IOException e) {
                System.out.println("\tYour Server ran into some IOException");
            }*/
        }
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
        }
    }

}

package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import viewmodel.ViewModel;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class MainWindowController implements Initializable, Observer {

    @FXML
    private Circle joystickRadius;
    @FXML
    private Sphere joystick;
    @FXML
    private TextArea textArea;
    @FXML
    private Slider rudderSlider, throttleSlider;
    @FXML
    public RadioButton manualPilot, autoPilot;
    @FXML
    private MapCanvas mapCanvas;

    ViewModel viewModel;
    private double JoystickRadius, JoystickCenterX, JoystickCenterY, JoystickInitializedCenterX, JoystickInitializedCenterY;
    public DoubleProperty aileron, elevator, planeCordX, planeCordY, destCordX, destCordY, simPlaneX, simPlaneY;
    public StringProperty mapPath;
    private int maxMapValue = 0, minMapValue = 0;

    public MainWindowController() {
        aileron = new SimpleDoubleProperty();
        elevator = new SimpleDoubleProperty();
        planeCordX = new SimpleDoubleProperty();
        planeCordY = new SimpleDoubleProperty();
        destCordX = new SimpleDoubleProperty();
        destCordY = new SimpleDoubleProperty();
        mapPath = new SimpleStringProperty();
        simPlaneX = new SimpleDoubleProperty();
        simPlaneY = new SimpleDoubleProperty();
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.aileron.bind(this.aileron);
        viewModel.elevator.bind(this.elevator);
        viewModel.throttle.bind(this.throttleSlider.valueProperty());
        viewModel.rudder.bind(this.rudderSlider.valueProperty());
        viewModel.scriptText.bind(this.textArea.textProperty());
        viewModel.planeX.bind(this.planeCordX);
        viewModel.planeY.bind(this.planeCordY);
        viewModel.destX.bind(this.destCordX);
        viewModel.destY.bind(this.destCordY);

        this.mapPath.bind(viewModel.mapPath);
        this.simPlaneX.bind(viewModel.simPlaneX);
        this.simPlaneY.bind(viewModel.simPlaneY);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JoystickInitializedCenterX = joystick.getLayoutX();
        JoystickInitializedCenterY = joystick.getLayoutY();
        JoystickRadius = joystickRadius.getRadius();
        JoystickCenterX = (joystick.localToScene(joystick.getBoundsInLocal()).getMinX() + joystick.localToScene(joystick.getBoundsInLocal()).getMaxX()) / 2;
        JoystickCenterY = (joystick.localToScene(joystick.getBoundsInLocal()).getMinY() + joystick.localToScene(joystick.getBoundsInLocal()).getMaxY()) / 2;
    }

    public void loadCsv() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select your csv file");
        fc.setInitialDirectory(new File("./resources/"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Csv Files", "*.csv");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                Scanner s = new Scanner(file);
                String content = s.useDelimiter("\\A").next().trim();
                String[] rows = content.replace("\r", "").split("\n");
                String[] corX_corY = rows[0].split(",");
                double corX = Double.parseDouble(corX_corY[0].replace("\"", "").trim());
                double corY = Double.parseDouble(corX_corY[1].replace("\"", "").trim());
                double distance = Double.parseDouble(rows[1].replaceAll("[\",]", "").trim());
                int numOfColumns = (rows[2].split(",")).length;

                int max = 0, min = 0;
                Integer[][] coords = new Integer[rows.length - 2][numOfColumns];
                for (int i = 2; i < rows.length; i++) {
                    coords[i - 2] = Arrays.stream(rows[i].split(","))
                            .map(Integer::valueOf)
                            .toArray(Integer[]::new);
                    max = Collections.max(Arrays.asList(coords[i-2]));
                    min = Collections.min(Arrays.asList(coords[i-2]));
                    if (max > maxMapValue)
                        maxMapValue = max;
                    if (min < minMapValue)
                        minMapValue = min;
                }
                mapCanvas.isMapLoaded = true;
                planeCordX.set(corX);
                planeCordY.set(corY);
                mapCanvas.setData(coords, corX, corY, maxMapValue, minMapValue, distance);
                mapCanvas.draw();
                mapCanvas.markPlane(corX, corY);
                viewModel.mapCanvas = mapCanvas;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select your script file");
        fc.setInitialDirectory(new File("./resources/"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                Scanner s = new Scanner(file);
                String content = s.useDelimiter("\\A").next();
                textArea.setText(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void connectPopUp() throws IOException {
        FXMLLoader fxl = new FXMLLoader(getClass().getResource("ConnectPopUpWindow.fxml"));
        AnchorPane root = fxl.load();

        Scene scene = new Scene(root,300,250);
        Stage primaryStage = new Stage();
        primaryStage.getIcons().add(new Image(new FileInputStream("./resources/connect.png")));
        primaryStage.setTitle("Connect to Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

        ConnectPopUpController mwc= fxl.getController();
        mwc.setViewModel(viewModel);
        viewModel.addObserver(mwc);
    }

    public void calcPathPopUp() throws IOException {
        if(!mapCanvas.isMapLoaded)
        {
            //We want to make sure the user loaded a map before trying to calculate
            JOptionPane.showMessageDialog(null, "You need to load a map first!", "Loading Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        FXMLLoader fxl = new FXMLLoader(getClass().getResource("CalcPathPopUpWindow.fxml"));
        AnchorPane root = fxl.load();

        Scene scene = new Scene(root,300,150);
        Stage primaryStage = new Stage();
        primaryStage.getIcons().add(new Image(new FileInputStream("./resources/calc.png")));
        primaryStage.setTitle("Connect to MapSolver");
        primaryStage.setScene(scene);
        primaryStage.show();

        CalcPathPopUpController mwc= fxl.getController();
        mwc.setViewModel(viewModel);
        viewModel.addObserver(mwc);
    }

    public void joystickReleased(MouseEvent mouseEvent) {
        joystick.setLayoutX(JoystickInitializedCenterX);
        joystick.setLayoutY(JoystickInitializedCenterY);
        aileron.set(0.0);
        elevator.set(0.0);
        viewModel.updateAileronAndElevator();
    }

    public void joystickDragged(MouseEvent mouseEvent) {
        double x1 = mouseEvent.getSceneX();
        double y1 = mouseEvent.getSceneY();
        double x2, y2;
        final int div = 2;
        double distance = dist(mouseEvent.getSceneX(), mouseEvent.getSceneY(), JoystickCenterX, JoystickCenterY);
        if (distance <= JoystickRadius / div) {
            joystick.setLayoutX(JoystickInitializedCenterX + x1 - JoystickCenterX);
            joystick.setLayoutY(JoystickInitializedCenterY + y1 - JoystickCenterY);
            x2 = x1;
            y2 = y1;
        } else {
            if (x1 > JoystickCenterX) {
                double alfa = Math.atan((y1-JoystickCenterY)/(x1-JoystickCenterX));
                double w = JoystickRadius * Math.cos(alfa);
                double z = JoystickRadius * Math.sin(alfa);

                x2 = JoystickCenterX + w/div;
                y2 = JoystickCenterY + z/div;
            } else {
                double alfa = Math.atan((JoystickCenterY - y1) / (JoystickCenterX - x1));
                double w = JoystickRadius * Math.cos(alfa);
                double z = JoystickRadius * Math.sin(alfa);
                x2 = JoystickCenterX - w/div;
                y2 = JoystickCenterY - z/div;
            }
            joystick.setLayoutX(JoystickInitializedCenterX + x2 - JoystickCenterX);
            joystick.setLayoutY(JoystickInitializedCenterY + y2 - JoystickCenterY);
        }
        aileron.set((x2 - JoystickCenterX) / JoystickRadius);
        elevator.set((JoystickCenterY - y2) / JoystickRadius);
        viewModel.updateAileronAndElevator();
    }

    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
    }

    public void throttleDragged(MouseEvent mouseEvent) {
        viewModel.updateThrottle();
    }

    public void rudderDragged(MouseEvent mouseEvent) {
        viewModel.updateRudder();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == viewModel) {
            if (arg.equals("done map calculate")) {
                String tmpPath = mapPath.get();

                if (mapCanvas.isMapLoaded)
                    mapCanvas.setPlaneOnMap(simPlaneX.get(), simPlaneY.get());
                mapCanvas.setPath(tmpPath);
                mapCanvas.drawPath(tmpPath);
            }
        }
    }

    public void startManualPilot(ActionEvent actionEvent) {
        setDisableManual(false);
    }

    public void startAutoPilot(ActionEvent actionEvent) {
        if (textArea.textProperty().get().equals("")) {
            autoPilot.setSelected(false);
            manualPilot.setSelected(true);
        } else {
            setDisableManual(true);
            viewModel.runScript();
        }
    }

    public void setDisableManual(boolean val) {
        joystick.setDisable(val);
        throttleSlider.setDisable(val);
        rudderSlider.setDisable(val);
    }

    public void markDestOnMap(MouseEvent mouseEvent) {
        double posX = mouseEvent.getX();
        double posY = mouseEvent.getY();
        mapCanvas.markDest(posX, posY);
        destCordX.set(mapCanvas.destY);
        destCordY.set(mapCanvas.destX);
        viewModel.calcMap(mapCanvas.matrix);
    }
}

package view;

import interpreter.Utilities;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.stage.FileChooser;
import viewmodel.ViewModel;

import java.io.File;
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

    ViewModel viewModel;
    private double JoystickRadius, JoystickCenterX, JoystickCenterY, JoystickInitializedCenterX, JoystickInitializedCenterY;
    public DoubleProperty aileron, elevator;

    public MainWindowController() {
        aileron = new SimpleDoubleProperty();
        elevator = new SimpleDoubleProperty();
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.aileron.bind(this.aileron);
        viewModel.elevator.bind(this.elevator);
        viewModel.throttle.bind(throttleSlider.valueProperty());
        viewModel.rudder.bind(rudderSlider.valueProperty());
        viewModel.scriptText.bind(textArea.textProperty());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JoystickInitializedCenterX = joystick.getLayoutX();
        JoystickInitializedCenterY = joystick.getLayoutY();
        JoystickRadius = joystickRadius.getRadius();
        JoystickCenterX = (joystick.localToScene(joystick.getBoundsInLocal()).getMinX() + joystick.localToScene(joystick.getBoundsInLocal()).getMaxX()) / 2;
        JoystickCenterY = (joystick.localToScene(joystick.getBoundsInLocal()).getMinY() + joystick.localToScene(joystick.getBoundsInLocal()).getMaxY()) / 2;

    }

    public void openFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select your script file");
        fc.setInitialDirectory(new File("resources/"));
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
}

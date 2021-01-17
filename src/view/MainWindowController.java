package view;

import interpreter.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;


public class MainWindowController implements Initializable {

    @FXML
    private Circle joystickRadius;
    @FXML
    private Sphere joystick;

    @FXML
    private TextArea textArea;
    private double JoystickRadius, JoystickCenterX, JoystickCenterY, JoystickInitializedCenterX, JoystickInitializedCenterY;

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
        Utilities.addMassage("set /controls/flight/aileron 0.0");
        Utilities.addMassage("set /controls/flight/elevator 0.0");
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
        Utilities.addMassage("set /controls/flight/aileron " + (x2 - JoystickCenterX) / JoystickRadius);
        Utilities.addMassage("set /controls/flight/elevator " + (JoystickCenterY - y2) / JoystickRadius);
    }

    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
    }
}

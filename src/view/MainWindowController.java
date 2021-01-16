package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;


public class MainWindowController implements Initializable {

    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void openFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select your script file");
        fc.setInitialDirectory(new File("resources/"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showOpenDialog(null);
        if (file != null){
            try {
                Scanner s = new Scanner(file);
                String content = s.useDelimiter("\\A").next();
                textArea.setText(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

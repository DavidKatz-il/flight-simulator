package view;

import interpreter.MyInterpreter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Model;
import viewmodel.ViewModel;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
//        MyInterpreter.parseScript("resources/script.txt");
        FXMLLoader fxl = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        AnchorPane root = (AnchorPane)fxl.load();
        primaryStage.setTitle("Flight Simulator");
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();

        Model m = new Model();
        ViewModel vm = new ViewModel(m);
        m.addObserver(vm);

        MainWindowController mwc = fxl.getController();
        mwc.setViewModel(vm);
        vm.addObserver(mwc);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

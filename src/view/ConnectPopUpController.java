package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import viewmodel.ViewModel;

import java.util.Observable;
import java.util.Observer;

public class ConnectPopUpController implements Observer {

    @FXML
    public TextField serverPort, serverSleep, clientIp, clientPort;

    ViewModel viewModel;

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.serverPort.bind(this.serverPort.textProperty());
        this.viewModel.serverSleep.bind(this.serverSleep.textProperty());
        this.viewModel.clientIp.bind(this.clientIp.textProperty());
        this.viewModel.clientPort.bind(this.clientPort.textProperty());
    }

    public void connectSimulator() {
        viewModel.connectSimulator();
    }

    @Override
    public void update(Observable o, Object arg) {
        {
            if(o == viewModel) {
                if(arg.equals("closePopUp")) {
                    Stage stage = (Stage) clientIp.getScene().getWindow();
                    if  (stage.isShowing())
                        stage.close();
                }
            }
        }
    }
}

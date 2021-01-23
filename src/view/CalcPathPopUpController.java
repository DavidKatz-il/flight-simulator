package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import viewmodel.ViewModel;

import java.util.Observable;
import java.util.Observer;

public class CalcPathPopUpController implements Observer {

    @FXML
    public TextField solverIp, solverPort;
    ViewModel viewModel;

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.solverIp.bind(this.solverIp.textProperty());
        this.viewModel.solverPort.bind(this.solverPort.textProperty());
    }

    public void connectSolver() {
        viewModel.connectSolver();
    }

    @Override
    public void update(Observable o, Object arg) {
        {
            if(o == viewModel) {
                if(arg.equals("closePopUp")) {
                    Stage stage = (Stage) solverIp.getScene().getWindow();
                    if  (stage.isShowing())
                        stage.close();
                }
            }
        }
    }
}

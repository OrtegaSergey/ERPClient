package DesktopClient.Controllers;

import DesktopClient.Services.Communicator;
import DesktopClient.Services.WindowManageHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class VerticalMenuController {

    private Communicator communicator;
    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    @FXML
    VBox leftMenuVBox;

    @FXML
    Label leftBottomLabel;

    @FXML
    void initialize(){
    }


    public void reports() {
    }

    public void getNotes() {
    }

    public void addNote() {
    }

    public void addClient() {
        try {
            Stage window = new Stage();
            FXMLLoader cardCreationLoader = new FXMLLoader(getClass().getResource("/AddingClientStyle/cardCreation.fxml"));
            Parent root = cardCreationLoader.load();
            CardCreationController ccc = cardCreationLoader.getController();
            ccc.window = window;
            window.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root, Color.TRANSPARENT);
            window.setTitle("А хз но работает");
            window.setScene(scene);
            window.initStyle(StageStyle.TRANSPARENT);
            window.setMinWidth(200);
            window.setMinHeight(400);
            window.setMaxWidth(600);
            window.setMaxHeight(800);
            window.setHeight(600);
            window.setWidth(400);
            new WindowManageHelper(window, scene);
            window.show();
        } catch (IOException e) {
            communicator.getMainController().showMsg("Ошибка создания модального окна!");
            e.printStackTrace();
        }
    }


    public void getActiveClients() {
        communicator.getMainController().mainBorderPane.setCenter(communicator.getTableAnchor());
        communicator.sendMsg("/getActiveClients");
    }

    public void getArchivedClients() {
        communicator.getMainController().mainBorderPane.setCenter(communicator.getTableAnchor());
        communicator.sendMsg("/getArchivedClients");
    }
}

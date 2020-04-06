package DesktopClient.Controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CardCreationController {
    //Логика не реализована ни клиентом, ни сервером!!!

    @FXML
    VBox creationVBox;

    Stage window;

    @FXML
    void initialize(){
//        window.setMaxHeight();
    }


    public void closeWindow() {
        window.close();
    }
}

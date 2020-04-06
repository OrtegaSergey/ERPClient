package DesktopClient;

import DesktopClient.Controllers.MainController;
import DesktopClient.Services.Communicator;
import DesktopClient.Services.WindowManageHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class StartClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Style/main.fxml"));
        Parent root = loader.load();
//        MainController controller = loader.getController();
//        controller.getCommunicator().setMainStage(primaryStage);
        primaryStage.setTitle("Custom Window");
        Scene scene = new Scene(root, (double)width/2, (double)height/2, Color.TRANSPARENT);
//        primaryStage.setMaxWidth(width);
//        primaryStage.setMaxHeight(height);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(500);
//        primaryStage.setWidth((double)width/2);
//        primaryStage.setHeight((double)height/2);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        //просто интересный параметр, разворачивает окно на весь экран (принудительно), выход на escape
        //primaryStage.setFullScreen(true);
        new WindowManageHelper(primaryStage, scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }

}
package DesktopClient.Controllers;

import DesktopClient.Services.Communicator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {

    private Communicator communicator;
    private int currentWindow = 1; //1 - окно авторизации, 2 - окно регистрации, 3 - рабочее окно
    private long timerStartPoint = System.currentTimeMillis();
    public String myName;

    public Communicator getCommunicator() {
        return communicator;
    }

    @FXML
    VBox mainWindow;

    @FXML
    BorderPane mainBorderPane;

    @FXML
    BorderPane authPanel;

    @FXML
    TextField authLoginField;

    @FXML
    PasswordField authPasswordField;

    @FXML
    Button authButton;

    @FXML
    Button authRegisterButton;

    @FXML
    BorderPane registrationPanel;

    @FXML
    TextField registrationLoginField;

    @FXML
    PasswordField registrationPasswordField;

    @FXML
    Button registrationButton;

    @FXML
    Button backRegistrationButton;

    @FXML
    Label alertAuthLabel;

    @FXML
    TextField registrationNameField;

    @FXML
    PasswordField registrationPasswordRepeatField;

    @FXML
    Label alertRegistrationLabel;


    @FXML
    void initialize() {
        communicator = new Communicator();
        communicator.setController(this);
        communicator.initElements();
        mainBorderPane.setLeft(communicator.getMenuAnchor());
        myCustomTimer();
    }


    public void closeProgram() {
        if (communicator.socket != null && !communicator.socket.isClosed()) {
                communicator.sendMsg("/end");
        }
        Platform.exit();
        System.exit(0);
    }

    private void checkConnection(){
        if (communicator.socket == null || communicator.socket.isClosed()) {
            communicator.connect();
        }
    }

    public void tryToAuth() {
        checkConnection();
        String login = authLoginField.getText();
        String password = authPasswordField.getText();
        if (login.equals("") || password.equals("")) {
            showMsg("Не введён логин/пароль!");
        } else {
            communicator.sendMsg("/auth " + login + " " + password);
            authLoginField.clear();
            authPasswordField.clear();
            alertAuthLabel.setText("");
        }
    }

    public void showMsg(String s) {
        Platform.runLater(() -> {
            timerStartPoint = System.currentTimeMillis();
            if (currentWindow == 1) {
                alertAuthLabel.setText(s);
            } else if (currentWindow == 2) {
                alertRegistrationLabel.setText(s);
            } else if (currentWindow == 3) communicator.getVerticalMenuController().leftBottomLabel.setText(s);
        });
    }

    public void registrationWindow() {
        setRegistrating(true);
    }

    public void registration() {
        checkConnection();
        String name = registrationNameField.getText();
        String login = registrationLoginField.getText();
        String password = registrationPasswordField.getText();
        String passwordRepeat = registrationPasswordRepeatField.getText();
        if (login.equals("") || password.equals("")) {
            showMsg("Не введён логин/пароль!");
        } else if (!password.equals(passwordRepeat)) {
            showMsg("Пароли не совпадают!");
        } else communicator.sendMsg("/reg " + name + " " + login + " " + password);
        registrationNameField.clear();
        registrationLoginField.clear();
        registrationPasswordField.clear();
        registrationPasswordRepeatField.clear();
        alertRegistrationLabel.setText("");
    }


    public void switchFocusToLoginField() {
        if (currentWindow == 2) registrationLoginField.requestFocus();
    }

    public void switchFocusToPassField() {
        if (currentWindow == 1) {
            authPasswordField.requestFocus();
        } else if (currentWindow == 2) registrationPasswordField.requestFocus();
    }

    public void switchFocusToPassRepeatField() {
        if (currentWindow == 2) registrationPasswordRepeatField.requestFocus();
    }

    public void authWindow() {
        setRegistrating(false);
    }

    public void setAuthorized() {
        currentWindow = 3;
        authPanel.setVisible(false);
        authPanel.setManaged(false);
        mainBorderPane.setVisible(true);
        mainBorderPane.setManaged(true);
    }

    public void setRegistrating(Boolean isRegistrating) {
        authPanel.setVisible(!isRegistrating);
        authPanel.setManaged(!isRegistrating);
        registrationPanel.setVisible(isRegistrating);
        registrationPanel.setManaged(isRegistrating);
        if (isRegistrating) {
            currentWindow = 2;
        } else {
            currentWindow = 1;
        }
    }

    private void myCustomTimer() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if ((System.currentTimeMillis() - 5000) > timerStartPoint) {
                    Platform.runLater(() -> {
                            clearAllAlertLabels();
                        timerStartPoint = System.currentTimeMillis();
                    });
                }
            }
        }).start();
    }

    private void clearAllAlertLabels() {
        alertAuthLabel.setText("");
        alertRegistrationLabel.setText("");
        communicator.getVerticalMenuController().leftBottomLabel.setText("");
    }
}

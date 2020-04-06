package DesktopClient.Controllers;

import DesktopClient.Secondary.Person;
import DesktopClient.Services.Communicator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class ClientCardController {

    private ObservableList<Integer> hourList;
    private ObservableList<Integer> minuteList;
   public ArrayList<String> branchesList;
    private ArrayList<String> chosenBranches = new ArrayList<>();

    @FXML
    VBox mainCardVBox;
    
    @FXML
    HBox topInfoHBox;
    
    @FXML
    Label idLabel;
    
    @FXML
    Label createdLabel;
    
    @FXML
    Label createdByLabel;
    
    @FXML
    Label archivedLabel;
    
    @FXML
    TextField nameTextField;
    
    @FXML
    TextField phoneTextField;
    
    @FXML
    TextField addressTextField;
    
    @FXML
    TextField emailTextField;
    
    @FXML
    MenuButton branchMenuButton;

    @FXML
    DatePicker agreedDatePicker;

    @FXML
    ChoiceBox<Integer> hourButton;

    @FXML
    ChoiceBox<Integer> minuteButton;

    @FXML
    Button clearDateTimeButton;

    @FXML
    ToggleButton alertToggleButton;
    
    @FXML
    ListView<Label> commentsListView;
    
    @FXML
    HBox commentLineHBox;
    
    @FXML
    TextField commentLineTextField;
    
    @FXML
    Button commentButton;
    
    @FXML
    Button toArchiveButton;

    @FXML
    Button fromArchiveButton;
    
    @FXML
    Button attachment;
    
    @FXML
    Button saveClientButton;

    private Communicator communicator;

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public void saveClientCard() {
    Person clientCard = new Person();
    clientCard.setNote("saveClientCard");
    clientCard.setID(Integer.parseInt(idLabel.getText()));
    clientCard.setName(nameTextField.getText());
    clientCard.setPhone(phoneTextField.getText());
    clientCard.setAddress(addressTextField.getText());
    clientCard.setEmail(emailTextField.getText());
//    branchMenuButton.getItems();
        for (MenuItem o:branchMenuButton.getItems()
             ) {
            CheckMenuItem temp = (CheckMenuItem)o;
            if (temp.isSelected()){

            }
        }
    //Добавить отделы в инит, хранить их в листе, при сохранении парсить лист, по IDшнику сохранять цифру отдела, общее число писать в int значение branches
    //реализовать clientCard.setBranch(getChosenBranches()), который будет возвращать числовое значение, соответствующее списку выбранных отделов (к примеру, 356 значит третий, пятый и шестой отдел)
        if (!agreedDatePicker.getEditor().getText().equals("")){
            clientCard.setAgreed(getChosenDate());
        }
        clientCard.setAlert(alertToggleButton.isSelected());
        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy HH:mm").create();
        communicator.sendMsg(gson.toJson(clientCard));
    }

    private Timestamp getChosenDate(){
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = df.parse(agreedDatePicker.getEditor().getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Timestamp.valueOf(df2.format(date) + " " + hourButton.getSelectionModel().getSelectedItem() + ":" +
                minuteButton.getSelectionModel().getSelectedItem() + ":00.0");
    }


    public void archiveClient() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Убрать клиента в архив?", ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Подтвердите действие:");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().getText().equals("OK")) {
            saveClientCard();
            communicator.sendMsg("/archiveClient " + idLabel.getText());
        }
    }

    public void unzipClient(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вытащить карту клиента из архива?", ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Подтвердите действие");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().getText().equals("OK")) {
            communicator.sendMsg("/unzipClient " + idLabel.getText());
        }
    }

    public void addComment() {
        //Проверка на то, что строка не пуста, имя не пусто (иначе оповещение), и строка не начинается со служебного символа
        //Не реализовано сервером
        communicator.sendMsg("/comment " + idLabel.getText() + " " + communicator.getMainController().myName + " " + commentLineTextField.getText());
        communicator.sendMsg("/getComments " + idLabel.getText());
        //Добавление коммента сразу пушится в базу, затем из базы обновляется окно
    }

    public void attachment() {
        //Тут будет реализация добавления к тикету файлов. В БД пишется id заявки, id файла, название файла и полный путь до него.
    }

    public void initClientCard(Person clientCard) {
        clearClientCard();
        Platform.runLater(() -> {
            idLabel.setText(Integer.toString(clientCard.getId()));
            String pattern = "dd.MM.yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            createdLabel.setText(df.format(clientCard.getCreated()));
            createdByLabel.setText(clientCard.getCreatedBy());
            if (clientCard.getArchived()!=null){
                archivedLabel.setText(df.format(clientCard.getArchived()));
                archived(true);
            } else {
                archivedLabel.setText("Нет");
                archived(false);
            }
            nameTextField.setText(clientCard.getName());
            phoneTextField.setText(clientCard.getPhone());
            addressTextField.setText(clientCard.getAddress());
            emailTextField.setText(clientCard.getEmail());
            //Инициализация кнопки выбора отдела
            for (String o : communicator.getBranchesList()) {
                branchMenuButton.getItems().add(new CheckMenuItem(o));
            }
            branchMenuButton.setText("Расшарить");
            initTimeList();
            hourButton.setItems(hourList);
            minuteButton.setItems(minuteList);
            if (clientCard.getAgreed()!=null){
                agreedDatePicker.getEditor().setText(df.format(clientCard.getAgreed()));
                hourButton.getSelectionModel().select(hourList.indexOf(clientCard.getAgreed().toLocalDateTime().getHour()));
                if (hourButton.getSelectionModel().getSelectedItem()!=null)
                    minuteButton.getSelectionModel().select(minuteList.indexOf(clientCard.getAgreed().toLocalDateTime().getMinute()));
            }
        });
    }

    private void initTimeList(){
        hourList = FXCollections.observableArrayList();
        minuteList = FXCollections.observableArrayList();
        for (int i = 8; i < 23; i++) {
            hourList.add(i);
        }
        for (int i = 0; i < 60; i+=15) {
            minuteList.add(i);
        }
    }

    private void clearClientCard() {
        Platform.runLater(() -> {
            idLabel.setText("");
            createdLabel.setText("");
            createdByLabel.setText("");
            archivedLabel.setText("");
            nameTextField.setText("");
            phoneTextField.setText("");
            addressTextField.setText("");
            emailTextField.setText("");
            branchMenuButton.getItems().clear();
            agreedDatePicker.getEditor().clear();
            hourButton.getItems().clear();
            minuteButton.getItems().clear();
        });
    }

    public void clearDateTime() {
        agreedDatePicker.getEditor().clear();
        hourButton.getItems().clear();
        minuteButton.getItems().clear();
    }

    public void archived(boolean status){
        toArchiveButton.setVisible(!status);
        toArchiveButton.setManaged(!status);
        fromArchiveButton.setVisible(status);
        fromArchiveButton.setManaged(status);
        nameTextField.setDisable(status);
        phoneTextField.setDisable(status);
        addressTextField.setDisable(status);
        emailTextField.setDisable(status);
        branchMenuButton.setDisable(status);
        agreedDatePicker.setDisable(status);
        hourButton.setDisable(status);
        minuteButton.setDisable(status);
        clearDateTimeButton.setDisable(status);
        saveClientButton.setDisable(status);
        alertToggleButton.setDisable(status);
        commentButton.setDisable(status);
    }
}

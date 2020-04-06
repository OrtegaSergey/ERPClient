package DesktopClient.Controllers;

import DesktopClient.Secondary.Person;
import DesktopClient.Services.Communicator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.util.Date;

public class TableController {

    private Communicator communicator;

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public ObservableList<Person> personsList = FXCollections.observableArrayList();
    private long currentTime;
    private long lastTime;
    private boolean isdblClicked = false;
    private TableRow<Person> trp;
    //Столбцы таблицы
    private TableColumn<Person, Integer> tcID;
    private TableColumn<Person, String> tcAddress;
    private TableColumn<Person, Date> tcAgreed;
    private TableColumn<Person, String> tcName;
    private TableColumn<Person, String> tcPhone;
    private TableColumn<Person, String> tcEmail;
    private TableColumn<Person, Date> tcCreated;
    private TableColumn<Person, Date> tcArchived;

    @FXML
    public TableView<Person> clientsTable;

    @FXML
    void initialize(){
        initializeClientsTable();
        initMouseDoubleClickListener();
    }


        private void initializeClientsTable() {
        tcID = new TableColumn<>("id");
        tcID.setCellValueFactory(new PropertyValueFactory<Person, Integer>("id"));
        tcID.prefWidthProperty().bind(clientsTable.widthProperty().multiply(0.05));

        tcAddress = new TableColumn<>("Адрес");
        tcAddress.setCellValueFactory(new PropertyValueFactory<Person, String>("address"));
        tcAddress.prefWidthProperty().bind(clientsTable.widthProperty().multiply(0.25));

        tcAgreed = new TableColumn<>("Согласовано");
        tcAgreed.setCellValueFactory(new PropertyValueFactory<Person, Date>("agreed"));
        tcAgreed.prefWidthProperty().bind(clientsTable.widthProperty().multiply(0.15));

        tcName = new TableColumn<>("ФИО");
        tcName.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        tcName.prefWidthProperty().bind(clientsTable.widthProperty().multiply(0.2));

        tcPhone = new TableColumn<>("Телефон");
        tcPhone.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));
        tcPhone.prefWidthProperty().bind(clientsTable.widthProperty().multiply(0.2));

        tcEmail = new TableColumn<>("E-Mail");
        tcEmail.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));
        tcEmail.prefWidthProperty().bind(clientsTable.widthProperty().multiply(0.14));

        tcCreated = new TableColumn<>("Создан");
        tcCreated.setCellValueFactory(new PropertyValueFactory<Person, Date>("created"));
        tcCreated.prefWidthProperty().bind(clientsTable.widthProperty().multiply(0.25));

        tcArchived = new TableColumn<>("Закрыт");
        tcArchived.setCellValueFactory(new PropertyValueFactory<Person, Date>("archived"));
        tcArchived.prefWidthProperty().bind(clientsTable.widthProperty().multiply(0.24));
    }


    public void initColumns(int type){
        switch (type){
            case 0:{
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        clientsTable.getColumns().clear();
                        clientsTable.getColumns().addAll(tcID, tcAddress, tcAgreed, tcName, tcPhone, tcEmail);
                        clientsTable.setItems(personsList);
                    }
                });
                break;
            }
            case 1:{
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        clientsTable.getColumns().clear();
                        clientsTable.getColumns().addAll(tcID, tcAddress, tcName, tcCreated, tcArchived);
                        clientsTable.setItems(personsList);
                    }
                });
                break;
            }
        }
    }




    private void initMouseDoubleClickListener() {
        clientsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                long diff;
                currentTime = System.currentTimeMillis();
                if (lastTime != 0 && currentTime != 0) {
                    diff = currentTime - lastTime;
                    isdblClicked = diff <= 200;
                }

                lastTime = currentTime;

                if (isdblClicked) {
                    Node node = ((Node) event.getTarget()).getParent();
                    if (node.getClass().getSimpleName().equals("TableRow")) {
                        trp = (TableRow<Person>) node;
                    } else if (node.getParent().getClass().getSimpleName().equals("TableRow")) {
                        trp = (TableRow<Person>) node.getParent();
                    }
                    if (!trp.isEmpty()) {
                        viewClientCard();
                    }
                }
            }
        });
    }

    private void viewClientCard() {
        if (clientsTable.getSelectionModel().getSelectedItem() != null) {
            Person selectedPerson = clientsTable.getSelectionModel().getSelectedItem();
            communicator.sendMsg("/getClientCard " + selectedPerson.getId());
        }
        communicator.getMainController().mainBorderPane.setCenter(communicator.getCardAnchor());
    }
}

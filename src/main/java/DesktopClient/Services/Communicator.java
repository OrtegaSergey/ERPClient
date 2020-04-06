package DesktopClient.Services;

import DesktopClient.Controllers.*;
import DesktopClient.Secondary.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;

public class Communicator {
    public Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8189;

    private Stage mainStage;
    private AnchorPane tableAnchor;
    private AnchorPane menuAnchor;
    private VBox cardAnchor;
    private ListView<Label> notesList;
    private CardCreationController cardCreationController;
    private VerticalMenuController verticalMenuController;
    private MainController mainController;
    private TableController tableController;
    private NotesController notesController;
    private Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy HH:mm").create();
    private ArrayList<String> branchesList = new ArrayList<>();

    public ArrayList<String> getBranchesList() {
        return branchesList;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public AnchorPane getTableAnchor() {
        return tableAnchor;
    }

    public void setTableAnchor(AnchorPane tableAnchor) {
        this.tableAnchor = tableAnchor;
    }

    public AnchorPane getMenuAnchor() {
        return menuAnchor;
    }

    public void setMenuAnchor(AnchorPane menuAnchor) {
        this.menuAnchor = menuAnchor;
    }

    public VBox getCardAnchor() {
        return cardAnchor;
    }

    public void setCardAnchor(VBox cardAnchor) {
        this.cardAnchor = cardAnchor;
    }

    public ListView<Label> getNotesList() {
        return notesList;
    }

    public void setNotesList(ListView<Label> notesList) {
        this.notesList = notesList;
    }

    public CardCreationController getCardCreationController() {
        return cardCreationController;
    }

    public void setCardCreationController(CardCreationController cardCreationController) {
        this.cardCreationController = cardCreationController;
    }

    public VerticalMenuController getVerticalMenuController() {
        return verticalMenuController;
    }

    public void setVerticalMenuController(VerticalMenuController verticalMenuController) {
        this.verticalMenuController = verticalMenuController;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public TableController getTableController() {
        return tableController;
    }

    public void setTableController(TableController tableController) {
        this.tableController = tableController;
    }

    public ClientCardController getClientCardController() {
        return clientCardController;
    }

    public void setClientCardController(ClientCardController clientCardController) {
        this.clientCardController = clientCardController;
    }

    private ClientCardController clientCardController;


    public void setController(MainController mainController) {
        this.mainController = mainController;
    }

    public void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.startsWith("/authok")) {
                                String[] searchName = str.split(" ");
                                mainController.myName = searchName[1];
                                mainController.setAuthorized();
                                break;
                            }
                            if (str.startsWith("/registered")) {
                                mainController.setRegistrating(false);
                                mainController.showMsg("Пользователь успешно создан, Вы можете авторизоваться!");
                            }
                        } else {
                            mainController.showMsg(str);
                        }
                    }
                    while (true) {
                        String str = in.readUTF();
                        if (str.equals("/ServerClosing")) {
                            break;
                        }
                        if (str.contains("отдел")){
                            System.out.println(str);
                            Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                            branchesList = gson.fromJson(str, listType);
                            continue;
                        }
                        if (str.contains("activeClient") || str.contains("archivedClient")) {
                            Type listType = new TypeToken<ArrayList<Person>>() {}.getType();
                            ArrayList<Person> clientList = gson.fromJson(str, listType);
                            if (clientList.get(0).getArchived() == null) {
                                tableController.initColumns(0);
                            } else tableController.initColumns(1);
                            tableController.personsList.clear();
                            tableController.personsList.addAll(clientList);
                            tableController.clientsTable.refresh();
                            continue;
                        }
                        if (str.contains("clientCard")) {
                            Person clientCard = gson.fromJson(str, Person.class);
                            clientCardController.initClientCard(clientCard);
                            continue;
                        }
                        if (str.contains("branchesList")) {
                            String[] tokens = str.split(" ", 2);
                            Type listType = new TypeToken<ArrayList<String>>() {
                            }.getType();
                            clientCardController.branchesList = gson.fromJson(tokens[1], listType);
                            continue;
                        }
                        if (str.startsWith("/archived")){
                            clientCardController.archived(true);
                            continue;
                        }
                        if (str.startsWith("/unzipped")){
                            clientCardController.archived(false);
                            continue;
                        }
                        mainController.showMsg(str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                        out.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initElements(){
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(getClass().getResource("/VerticalMenuStyle/verticalMenu.fxml"));
            menuAnchor = loader.load();
            verticalMenuController = loader.getController();
            verticalMenuController.setCommunicator(this);
            loader = new FXMLLoader(getClass().getResource("/TableStyle/table.fxml"));
            tableAnchor = loader.load();
            tableController = loader.getController();
            tableController.setCommunicator(this);
            loader = new FXMLLoader(getClass().getResource("/ClientCardStyle/clientCard.fxml"));
            cardAnchor = loader.load();
            clientCardController = loader.getController();
            clientCardController.setCommunicator(this);
            loader = new FXMLLoader(getClass().getResource("/NotesStyle/notes.fxml"));
            notesList = loader.load();
            notesController = loader.getController();
            notesController.setCommunicator(this);
        } catch (IOException e){
            mainController.showMsg("Ошибка инициализации окна");
            e.printStackTrace();
        }
    }
}

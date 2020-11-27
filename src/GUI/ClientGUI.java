package GUI;

import TCP.Client;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.application.Platform;

import java.io.IOException;
import java.util.LinkedList;

public class ClientGUI extends Application {
    private Stage stage;
    private Client client;
    private TextArea messages;
    private LinkedList<Pair<Pair<String,String>,String>>listMsg = new LinkedList<Pair<Pair<String,String>,String>>();

    @Override
    public void start(Stage stage){
        this.stage = stage;
        initUI(stage);
    }

    private void initUI(Stage stage){
        Pane root = new AnchorPane();
        Text fname = new Text("First name:");fname.setLayoutX(200);fname.setLayoutY(95);
        Text lname = new Text("Last name:");lname.setLayoutX(200);lname.setLayoutY(145);
        Text hostName = new Text("Host name:");hostName.setLayoutX(200);hostName.setLayoutY(195);
        Text port = new Text("Port:");port.setLayoutX(200);port.setLayoutY(245);
        BackgroundImage background = new BackgroundImage(new Image("files/background.jpg"),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
        TextField fnameField = new TextField("");fnameField.setPromptText("Jean");fnameField.setLayoutX(200);fnameField.setLayoutY(100);
        TextField lnameField = new TextField("");lnameField.setPromptText("DUPOND");lnameField.setLayoutX(200);lnameField.setLayoutY(150);
        TextField hostNameField = new TextField("localhost");hostNameField.setLayoutX(200);hostNameField.setLayoutY(200);
        TextField portField = new TextField("2000");portField.setLayoutX(200);portField.setLayoutY(250);
        Button login = new Button("Login as guest");login.setLayoutX(230);login.setLayoutY(300);
        stage.getIcons().add(new Image("files/login-rounded-right.png"));
        login.setOnAction(e->{
            String fN = fnameField.getText();
            String lN = lnameField.getText();
            String hN = hostNameField.getText();
            if(portField.getText() == null || portField.getText() == ""){
                displayAlert("Please enter a valid port");
            }else{
                Client client = new Client(fN, lN, hN, Integer.parseInt(portField.getText()), this);
                boolean connected = client.establishConnection(hN,portField.getText());
                if(connected) {
                    //chatClient cC = new chatClient(fN,lN,hN,p);
                    //launch(cC.getClass());
                    Pane chat = new AnchorPane();
                    BackgroundImage backgroundbis = new BackgroundImage(new Image("files/background.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                    messages = new TextArea();
                    messages.setEditable(false);
                    messages.setPrefSize(500, 400);
                    //messages.setStyle("text-area-background: blue;");
                    TextField writeMsg = new TextField("");
                    writeMsg.setPromptText("Write a message");
                    writeMsg.setLayoutX(40);
                    writeMsg.setLayoutY(350);
                    Button sendMsg = new Button("Send");
                    sendMsg.setLayoutX(250);
                    sendMsg.setLayoutY(350);
                    ScrollPane scrollerMessages = new ScrollPane();
                    scrollerMessages.setContent(messages);
                    scrollerMessages.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                    scrollerMessages.setPrefSize(500, 300);
                    scrollerMessages.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                    scrollerMessages.setStyle("-fx-control-inner-background: rgb(255,255,255);");
                    chat.getChildren().add(scrollerMessages);
                    chat.getChildren().addAll(writeMsg, sendMsg);
                    sendMsg.setOnMouseClicked(ev -> {
                        client.send(writeMsg.getText());
                        writeMsg.setText("");
                    });
                    chat.setStyle("-fx-color-background: rgb(255,255,255);");
                    Scene secondscene = new Scene(chat, 500, 400);
                    Stage newWindow = new Stage();
                    newWindow.setResizable(false);
                    newWindow.setTitle("Chat - " + client.getFname() + " " + client.getLname());
                    newWindow.setScene(secondscene);
                    newWindow.setX(stage.getX() + 200);
                    newWindow.setY(stage.getY() + 100);
                    newWindow.show();
                }else{
                    displayAlert("Connection Error: Either the hostname and port are invalid or the server is not connected.");
                }
            }
        });
        root.setBackground(new Background(background));
        root.getChildren().add(fnameField);root.getChildren().add(lnameField);
        root.getChildren().add(hostNameField);root.getChildren().add(portField);
        root.getChildren().add(login);
        root.getChildren().add(fname);root.getChildren().add(lname);
        root.getChildren().add(hostName);root.getChildren().add(port);
        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Ajoute un message dans le textArea messages
     * @param message message à ajouter.
     */
    public void addMessage(Pair<Pair<String,String>,String> message){
        messages.appendText(message.getKey().getKey()+" "+message.getKey().getValue()+" : "+message.getValue()+"\n");
    }

    /**
     * Affiche une alerte dont la cause est passée en paramètre.
     * @param cause Cause de l'alerte
     */
    public void displayAlert(String cause) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(cause);
        alert.showAndWait();
    }
}

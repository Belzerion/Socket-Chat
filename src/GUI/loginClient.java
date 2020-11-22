package GUI;

import TCP.Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.application.Platform;

import java.io.IOException;

public class loginClient extends Application {
    private Stage stage;
    private Client client;
    private VBox messages;

    @Override
    public void start(Stage stage){
        this.stage = stage;
        initUI(stage);
    }

    private void initUI(Stage stage){
        Pane root = new AnchorPane();
        Text fname = new Text("First name:");fname.setLayoutX(200);fname.setLayoutY(45);
        Text lname = new Text("Last name:");lname.setLayoutX(200);lname.setLayoutY(95);
        Text hostName = new Text("Host name:");hostName.setLayoutX(200);hostName.setLayoutY(145);
        Text port = new Text("Port:");port.setLayoutX(200);port.setLayoutY(195);
        TextField fnameField = new TextField("");fnameField.setPromptText("Jean");fnameField.setLayoutX(200);fnameField.setLayoutY(50);
        TextField lnameField = new TextField("");lnameField.setPromptText("DUPOND");lnameField.setLayoutX(200);lnameField.setLayoutY(100);
        TextField hostNameField = new TextField("localhost");hostNameField.setLayoutX(200);hostNameField.setLayoutY(150);
        TextField portField = new TextField("2000");portField.setLayoutX(200);portField.setLayoutY(200);
        Button login = new Button("Login as guest");login.setLayoutX(200);login.setLayoutY(250);
        login.setOnAction(e->{
            String fN = fnameField.getText();
            String lN = lnameField.getText();
            String hN = hostNameField.getText();
            if(portField.getText() == null || portField.getText() == ""){
                displayAlert("Please enter a valid port");
            }else{
                    Client client = new Client(fN,lN,hN,Integer.parseInt(portField.getText()), this);
                    //chatClient cC = new chatClient(fN,lN,hN,p);
                    //launch(cC.getClass());
                    client.establishConnection(hN,portField.getText());
                    Pane chat = new AnchorPane();
                    messages = new VBox();

                    TextField writeMsg = new TextField("");writeMsg.setPromptText("Write a message");
                    writeMsg.setLayoutX(40);writeMsg.setLayoutY(350);
                    Button sendMsg = new Button("Send");sendMsg.setLayoutX(250);sendMsg.setLayoutY(350);
                    ScrollPane scrollerMessages = new ScrollPane();
                    scrollerMessages.setContent(messages);
                    scrollerMessages.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, null, null)));
                    scrollerMessages.setPrefSize(500,300);
                    scrollerMessages.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                    chat.getChildren().add(scrollerMessages);
                    chat.getChildren().addAll(writeMsg,sendMsg);
                    sendMsg.setOnMouseClicked(ev ->{
                        client.send(writeMsg.getText());
                    });
                    Scene secondscene = new Scene(chat, 500, 400);
                    Stage newWindow = new Stage();
                    newWindow.setTitle("Chat");
                    newWindow.setScene(secondscene);newWindow.setX(stage.getX() + 200);
                    newWindow.setY(stage.getY() + 100);
                    newWindow.show();
            }
        });
        root.getChildren().add(fnameField);root.getChildren().add(lnameField);
        root.getChildren().add(hostNameField);root.getChildren().add(portField);
        root.getChildren().add(login);
        root.getChildren().add(fname);root.getChildren().add(lname);
        root.getChildren().add(hostName);root.getChildren().add(port);
        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

    }
    public void addMessage(Pair<Pair<String,String>,String> message){
        HBox Message = new HBox();
        Text metaInfMessage = new Text(message.getKey().getKey()+" "+message.getKey().getValue()+": ");
        Text messageContent = new Text(message.getValue());
        Message.getChildren().addAll(metaInfMessage, messageContent);
        messages.getChildren().add(Message);
    }
    public void displayAlert(String cause) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(cause);
        alert.showAndWait();
    }
}

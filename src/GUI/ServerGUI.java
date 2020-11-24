package GUI;

import TCP.ServerTCP;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ServerGUI extends Application {
    private Stage stage;
    private TextField portField;
    private int portUsed;
    private ServerTCP sT;

    @Override
    public void start(Stage stage){
        this.stage = stage;
        this.sT = new ServerTCP(this);
        initUI(stage);
    }

    private void initUI(Stage stage){
        Pane root = new AnchorPane();
        Text port = new Text("Port:");port.setLayoutX(200);port.setLayoutY(125);
        stage.getIcons().add(new Image("files/start-icon.png"));
        BackgroundImage background = new BackgroundImage(new Image("files/background.jpg"), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
        portField = new TextField("2000");portField.setLayoutX(200);portField.setLayoutY(130);
        Button start = new Button("Start Server");start.setLayoutX(230);start.setLayoutY(180);
        start.setOnAction(e ->{
            if(portField.getText() == null || portField.getText() == ""){
                displayAlert("Please enter a valid port");
            }else{
                sT.startServ(getPort());
                sT.createSockClient();
                displayInfo("Server connected");
            }
        });
        root.setBackground(new Background(background));
        root.getChildren().add(portField);
        root.getChildren().add(start);
        root.getChildren().add(port);
        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Start server");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public Integer getPort(){
        String port = portField.getText();
        int res = Integer.parseInt(port);
        return res;
    }
    public void displayAlert(String cause) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(cause);
        alert.showAndWait();
    }

    public void displayInfo(String info){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }

}

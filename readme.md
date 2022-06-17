Go to Run -> Edit Configurations -> VM Options -> add the following line: --module-path C:\Users\Hugo\Documents\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.fxml
In the same menu, Allow parallel run for loginClient.java, Client.java.
To run the application:
  -run startServer.java
  -run loginClient.java

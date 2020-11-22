package TCP;

import GUI.loginClient;
import com.sun.glass.ui.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {

    private String fname;
    private String lname;
    private String hostname;
    private int port;
    private ObjectInputStream socIn;
    private ObjectOutputStream socOut;
    private Socket clientSocket;
    private Scanner sc = new Scanner(System.in);//pour lire à partir du clavier
    private loginClient gui;


    public Client(String fname, String lname, String hostname, int port, loginClient gui){
        this.fname = fname;
        this.lname = lname;
        this.hostname = hostname;
        this.port = port;
        this.gui = gui;
    }

    public String getFname(){
        return fname;
    }
    public String getLname(){
        return lname;
    }

    public void establishConnection(String hostname, String port) {
        try {
            clientSocket = new Socket(hostname, Integer.parseInt(port));
            socOut = new ObjectOutputStream(clientSocket.getOutputStream());
            socIn = new ObjectInputStream(clientSocket.getInputStream());
            Thread receive = new Thread(new Runnable() {
                @Override
                public void run() {
                    receive();
                }
            });
            receive.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void send(String message){
            Pair<Pair<String,String>, String> msg;
            Pair<String, String> clientPair = new Pair<String, String>(fname, lname);
            try {
                msg = new Pair<Pair<String,String>, String>(clientPair, message);
                socOut.writeObject(msg);
                socOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public void receive(){
            Pair<Pair<String,String>, String> msg;
            try {
                msg = (Pair<Pair<String,String>, String>) socIn.readObject();
                while(msg!=null){
                    System.out.println(msg.getKey().getKey()+" "+msg.getKey().getValue()+": "+msg.getValue());
                    gui.addMessage(msg);
                    msg = (Pair<Pair<String,String>, String>) socIn.readObject();
                }
                System.out.println("Server disconnected");
                socOut.close();
                clientSocket.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

}


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




    /*public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }
        System.out.println("Veuillez entrer votre prénom: ");
        String fname = sc.nextLine();
        System.out.println("Veuillez entrer votre nom de famille: ");
        String lname = sc.nextLine();
        Client client = new Client(fname, lname, args[0], Integer.parseInt(args[1]));
            /*clientSocket = new Socket(hostname, port);
            socOut = new ObjectOutputStream(clientSocket.getOutputStream());
            socIn = new ObjectInputStream(clientSocket.getInputStream());*/
        /*establishConnection(args[0], args[1]);
        receive();
        send();*/
    //}
    public void establishConnection(String hostname, String port) {
        try {
            clientSocket = new Socket(hostname, Integer.parseInt(port));
            socOut = new ObjectOutputStream(clientSocket.getOutputStream());
            socIn = new ObjectInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void send(String message){
            Pair<Pair<String,String>, String> msg;
            Pair<String, String> clientPair = new Pair<String, String>(fname, lname);
            /*msg = sc.nextLine();
            socOut.println(msg);
            socOut.flush();*/
            try {
                //String line = sc.nextLine();
                msg = new Pair<Pair<String,String>, String>(clientPair, message);
                socOut.writeObject(msg);
                socOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public void receive(){
            // String msg;
            Pair<Pair<String,String>, String> msg;
            try {
                // msg = socIn.readLine();
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

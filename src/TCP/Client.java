package TCP;

import GUI.ClientGUI;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
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
    private ClientGUI gui;


    public Client(String fname, String lname, String hostname, int port, ClientGUI gui){
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

    /**
     * Etablit la connexion avec le serveur HTTP, création de la Socket côté client.
     * @param hostname adresse IP du serveur
     * @param port
     * @return
     */
    public boolean establishConnection(String hostname, String port) {
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
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Envoie un message dans le flux de sortie de la socket client.
     * @param message message à envoyer.
     */
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

    /**
     * Thread de Lecture du flux d'entrée de la socket client.
     */
    public void receive(){
        Pair<Pair<String,String>, String> msg;
        try {
            msg = (Pair<Pair<String,String>, String>) socIn.readObject();
            while(msg!=null){
                msg = (Pair<Pair<String, String>, String>) socIn.readObject();
                if(!msg.getKey().getKey().equals("") && !msg.getKey().getValue().equals("") && !msg.getValue().equals("") ) {
                    System.out.println(msg.getKey().getKey() + " " + msg.getKey().getValue() + ": " + msg.getValue());
                    gui.addMessage(msg);
                }
            }
            System.out.println("Server disconnected");
            socOut.close();
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}


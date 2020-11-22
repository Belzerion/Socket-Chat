package TCP;

import GUI.startServer;
import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javafx.application.Application;

public class ServerTCP extends Thread{
        private static ServerSocket listenSocket;
        private static LinkedList<Pair<Long, Socket>> listClient= new LinkedList<Pair<Long, Socket>>();
        private static Long clientIndice = 0L;
        private static LinkedList<Pair<Long, ObjectOutputStream>> listOOS = new LinkedList<Pair<Long, ObjectOutputStream>>();
        private static String historyFile = "history.txt";
        private static startServer gui;


        public ServerTCP(startServer sS){
            this.gui = sS;
        }

        public static void main(String [] args){
            startServ(Integer.parseInt(args[0]));
            createSockClient();

        }



    public static void createSockClient(){
            Thread socketCreation = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Socket clientSocket = null;
                        try {
                            clientSocket = listenSocket.accept();
                            clientIndice++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Connexion from:" + clientSocket.getInetAddress());
                        ClientThread ct = new ClientThread(clientSocket, listClient, clientIndice, listOOS, historyFile);
                        Pair<Long, Socket> newClient = new Pair<Long, Socket>(clientIndice, clientSocket);
                        listClient.add(newClient);
                        ct.start();

                    }
                }
            });
            socketCreation.start();
        }
        public static void startServ(int args) {
            try {
                listenSocket = new ServerSocket(args); //port
                System.out.println("Server ready...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}

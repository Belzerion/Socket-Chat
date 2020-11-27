/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package TCP;

import javafx.util.Pair;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.LinkedList;

public class ClientThread
        extends Thread {

    private Socket clientSocket;
    private LinkedList<Pair<Long, Socket>> socketList;
    private Long clientIndice;
    private LinkedList<Pair<Long, ObjectOutputStream>> oosList;
    private String historyFile;



    ClientThread(Socket s, LinkedList<Pair<Long,Socket>> socketList, Long clientIndice, LinkedList<Pair<Long, ObjectOutputStream>> oosList, String historyFile) {
        this.clientSocket = s;
        this.socketList = socketList;
        this.clientIndice = clientIndice;
        this.oosList = oosList;
        this.historyFile = historyFile;
    }

    /**
     * Envoie l'historique de la conversation dans le flux de sortie de la socket Client côté serveur.
     * @param socOut flux de sortie de la socket client côté serveur.
     * @throws IOException
     */
    public void history(ObjectOutputStream socOut) throws IOException {
        FileReader reader = new FileReader(historyFile);
        int a;
        String hist = "";
        String fname = "";
        String lname = "";
        int compt = 0;
        while( (a = reader.read()) != -1){
            int letterInt = a;
            char letter = (char) letterInt;
            if(letter == ' ' && compt == 0){
                fname = hist;
                compt++;
                hist = "";
                continue;
            }
            if(letter == ':' && compt ==1){
               lname = hist;
               compt ++;
               hist = "";
               continue;
            }
            if(letter != '\n') {
                hist += letter;
            }
            else{
                Pair<String,String> clientPair = new Pair(fname, lname);
                Pair<Pair<String,String>,String> msg = new Pair<Pair<String,String>,String>(clientPair, hist);
                socOut.writeObject(msg);
                hist = "";
                fname = "";
                lname = "";
                compt = 0;
            }
        }
    }

    /**
     * receives a request from client then sends an echo to the client
     * @param /*clientSocket the client socket
     **/
    public void run() {
        try {
            //BufferedReader socIn = null;
            /*socIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());*/
            FileWriter writer = new FileWriter(historyFile, true);
            ObjectInputStream socIn = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream socOut = new ObjectOutputStream(clientSocket.getOutputStream());
            Pair<Long, ObjectOutputStream> oos = new Pair<Long, ObjectOutputStream>(this.clientIndice, socOut);
            oosList.add(oos);
            history(socOut);
            while (true) {
                // String line = socIn.readLine();

                Pair<Pair<String,String>,String> msg = (Pair<Pair<String,String>,String>) socIn.readObject();

                //System.out.println(line);
                Iterator<Pair<Long, ObjectOutputStream>> it = oosList.iterator();
                while(it.hasNext()){
                    Pair<Long, ObjectOutputStream> pair = it.next();
                    // PrintStream out = new PrintStream(pair.getValue().getOutputStream());
                    // ObjectOutputStream out = new ObjectOutputStream(pair.getValue().getOutputStream());
                    Pair<Pair<String, String>, String> tmp = new Pair(msg.getKey(), msg.getValue());
                    // out.println(tmp.getValue());
                    pair.getValue().writeObject(tmp);

                }
                System.out.println("Received and broadcasted: "+msg.getValue());
                writer.write(msg.getKey().getKey()+" "+msg.getKey().getValue()+": "+msg.getValue()+"\n");
                writer.flush();

                //socOut.println(line);
            }
        } catch (Exception e) {
            System.err.println("(CT) Error in EchoServer:" + e);
        }
    }




}
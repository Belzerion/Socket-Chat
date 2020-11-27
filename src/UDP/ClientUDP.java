package UDP;
import javafx.util.Pair;

import java.io.*;
import java.net.*;

public class ClientUDP {

    private String fname;
    private String lname;

    public ClientUDP(String fname, String lname){
        this.fname = fname;
        this.lname = lname;
    }
    public String getFname(){
        return fname;
    }

    public String getLname(){
        return lname;
    }


    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Veuillez entrer votre prénom: ");
        String fname = in.readLine();
        System.out.println("Veuillez entrer votre nom de famille");
        String lname = in.readLine();
        ClientUDP client = new ClientUDP(fname, lname);
        if(args.length != 2){
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        String line;
        int serverPort = Integer.parseInt(args[1]);
        String ipGroup = args[0];
        Pair<String,String> pairClient = new Pair<String, String>(client.getFname(), client.getLname());
        System.out.println("port:"+serverPort+" ip:"+ipGroup);
        MulticastSocket socket = new MulticastSocket(serverPort);
        InetAddress serverAddr = InetAddress.getByName(ipGroup);
        socket.joinGroup(serverAddr);
        Thread send = new Thread(new Runnable() {
            @Override
            public void run(){
                String line;
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                byte[] bufS;
                while(true) {
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        line = in.readLine();
                        bufS = line.getBytes();
                        Pair<Pair<String,String>, String> toSend = new Pair<Pair<String,String>, String>(pairClient, line);
                        oos.writeObject(toSend);
                        byte[] data = baos.toByteArray();
                        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddr, serverPort);
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        send.start();

        Thread receive = new Thread(new Runnable() {
            @Override
            public void run(){
                byte[] buf = new byte[1024];
                while(true){
                    try {
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        socket.receive(packet);
                        byte[] data = packet.getData();
                        ByteArrayInputStream bais = new ByteArrayInputStream(data);
                        ObjectInputStream oin = new ObjectInputStream(bais);
                        Pair<Pair<String,String>, String> res = (Pair<Pair<String,String>, String>) oin.readObject();
                        System.out.println(res.getKey().getKey()+" "+res.getKey().getValue()+": "+res.getValue());
                        packet.setLength(buf.length);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        receive.start();


    }
}

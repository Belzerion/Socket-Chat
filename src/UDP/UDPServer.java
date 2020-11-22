package UDP;
import javafx.util.Pair;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
public class UDPServer {

    public static void main(String[] args) throws IOException {

        if(args.length != 2) {
            System.out.println("Usage: java EchoServer <EchoServer port>");
            System.exit(1);
        }
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        MulticastSocket serverSock = new MulticastSocket(port);
        InetAddress group = InetAddress.getByName(ip);
        serverSock.joinGroup(group);
        System.out.println("port:"+port+" ip:"+ip);
        FileWriter history = new FileWriter("history.txt");
        byte[] buf = new byte[1024];

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                serverSock.receive(packet);
                byte[] data = packet.getData();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream oin = new ObjectInputStream(bais);
                Pair<Pair<String,String>, String> res = (Pair<Pair<String,String>, String>) oin.readObject();
                history.write(res.getKey().getKey()+" "+res.getKey().getValue()+": "+res.getValue()+"\n");
                history.flush();
                packet.setLength(buf.length);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

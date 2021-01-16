package network;

import expressions.Number;
import interpreter.Utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.stream.Stream;


public class Server {
    List<String> nodes = Utilities.getNodes();

    public Server(int port, int sleep) {
        InitializeSimNodes();
        new Thread(()->runServer(port, sleep)).start();
    }

    private void runServer(int port, int sleep){
        try {
            ServerSocket server=new ServerSocket(port);
            Socket client;
            server.setSoTimeout(1000);
            while(!Utilities.stop){
                try{
                    synchronized (Utilities.clientStatus) {
                        Utilities.clientStatus.connected = false;
                        client=server.accept();
                        Utilities.clientStatus.connected = true;
                        Utilities.clientStatus.notify();
                    }
                    BufferedReader in=new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String line;
                    while (!Utilities.stop) {
                        try {
                            if ((line=in.readLine()) == null)
                                continue;
                            double[] simArr = Stream.of(line.split(","))
                                    .mapToDouble (Double::parseDouble)
                                    .toArray();

                            for (int i=0; i<simArr.length; i++) {
                                Utilities.getSimNumber(nodes.get(i)).setValue(simArr[i]);
                            }
                            Thread.sleep(sleep*10);
                        } catch (NumberFormatException | InterruptedException e) { e.printStackTrace(); }
                    }
                    in.close();
                    client.close();
                    Utilities.clientStatus.connected = false;
                } catch (SocketTimeoutException e){}//e.printStackTrace(); }
            }
            server.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void InitializeSimNodes() {
        for (String node: nodes) {
            Utilities.setSimNumber(node, new Number(0));
        }
    }

    public void close() {
        Utilities.stop=true;
    }
}

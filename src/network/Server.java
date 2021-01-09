package network;

import interpeter.Utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.stream.Stream;


public class Server {

    public Server(int port, int sleep) {
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
                            Utilities.getSimNumber("simX").setValue(simArr[0]);
                            Utilities.getSimNumber("simY").setValue(simArr[1]);
                            Utilities.getSimNumber("simZ").setValue(simArr[2]);
                            Thread.sleep(sleep*10);
                        } catch (NumberFormatException | InterruptedException e) { e.printStackTrace(); }
                    }
                    in.close();
                    client.close();
                    Utilities.clientStatus.connected = false;
                } catch (SocketTimeoutException e){ e.printStackTrace(); }
            }
            server.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void close() {
        Utilities.stop=true;
    }
}

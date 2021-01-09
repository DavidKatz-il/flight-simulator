package network;

import interpeter.Utilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {
    public Client(String host, int port) {
        synchronized (Utilities.clientStatus) {
            while (!Utilities.clientStatus.connected) {
                try { Utilities.clientStatus.wait();} catch (InterruptedException e) { e.printStackTrace();}
            }
        }
        new Thread(()->runClient(host, port)).start();
    }

    private void runClient(String host, int port) {
        while (!Utilities.stop) {
            try {
                Socket interpreter=new Socket(host, port);
                PrintWriter out=new PrintWriter(interpreter.getOutputStream(), true );
                while (!Utilities.stop) {
                    while (!Utilities.getMassages().isEmpty()){
                        out.println(Utilities.pollMassage());
                    }
                    try {Thread.sleep(100);} catch (InterruptedException e1) {e1.printStackTrace();}
                }
                out.close();
                interpreter.close();
            } catch (IOException e) {
                try { Thread.sleep(1000);} catch (InterruptedException e1) {e1.printStackTrace();}
            }
        }
    }

    public void close() {
        Utilities.stop=true;
    }
}

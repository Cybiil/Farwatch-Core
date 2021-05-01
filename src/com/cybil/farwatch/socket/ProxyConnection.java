package com.cybil.farwatch.socket;

import com.cybil.farwatch.Core;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeoutException;

public class ProxyConnection {

    private int port;
    private int hostPort;
    private ServerSocket so;

    public ArrayBlockingQueue<String> toSend = new ArrayBlockingQueue<>(25);
    public ArrayBlockingQueue<String> response = new ArrayBlockingQueue<>(25);

    public ProxyConnection(int spigotPort, int timeout) throws IOException {
        this.port = spigotPort + 5000;
        this.hostPort = spigotPort + 10000;
        so = new ServerSocket(hostPort);
        so.setSoTimeout(timeout);
    }

    public boolean run() throws IOException {
        Socket inp = null;
        boolean accepted = false;
        while (Core.running && !accepted) {
            try {
                inp = so.accept();
                accepted = true;
            } catch (SocketTimeoutException ignore) {
            }
        }
        if (!Core.running || inp == null)
            return false;
        DataInputStream din = new DataInputStream(inp.getInputStream());
        if (!din.readUTF().equals("READY"))
            return false;
        inp.close();

        Socket socket = new Socket("127.0.0.1", port);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        while (Core.running) {
            try {
                String send = toSend.take();
                out.writeUTF(send);

            } catch (SocketTimeoutException ignore) {
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

}

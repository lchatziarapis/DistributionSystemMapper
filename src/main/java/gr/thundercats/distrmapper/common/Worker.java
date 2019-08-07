package gr.thundercats.distrmapper.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker<T> {

    private int port;
    private Class<T> receivableClass;
    private ServerSocket serverSocket;
    private Thread serverThread;

    public Worker(Class<T> receivableClass, int port) {
        this.port = port;
        this.receivableClass = receivableClass;

        try {
            serverSocket = new ServerSocket(port);
            Runnable receiverLoop = () -> {while (true) {
                try {
                    receiverLoopable(serverSocket.accept());
                } catch (IOException e) {
                    return;
                }
            }};
            serverThread = new Thread(receiverLoop);

            serverThread.start();
        } catch (IOException e) {
            System.err.println("Failed to initialize worker at port " + port + ": " + e.getMessage());
        }
    }

    public void sendBind(String host, int port) {
        MasterBind masterBind = new MasterBind(getClass().getSimpleName(), getPort());
        dispatch(masterBind, host, port);
    }

    public void kill() {
        try {
            serverSocket.close();
            serverThread.interrupt();
        } catch (IOException ignored) {

        }
    }

    public void received(int port, T object) {}

    public void receiverLoopable(Socket socket) {
        try {
            ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
            Integer sourcePort = stream.readInt();
            Object object = stream.readObject();

            System.out.println("Received: " + object.getClass().getSimpleName());

            if (receivableClass == object.getClass()) {
                received(sourcePort, (T) object);
            } else {
                System.err.println(getClass().getName() + " received invalid object class [" + object.getClass() + "]");
            }
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void dispatch(ChainRequest object) {
        ChainRequest.Destination destination = object.chainNext();
        dispatch(object, destination.getHost(), destination.getPort());
    }

    public void dispatch(Object object, String host, int port) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 1000);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            objectOutputStream.writeInt(this.port);
            objectOutputStream.writeObject(object);
            socket.close();
        } catch (IOException e) {
            dispatchFail(host, port, object, e);
        }
    }

    public void dispatchFail(String host, int port, Object object, IOException ex) {}

    public Class<T> getReceivableClass() {
        return receivableClass;
    }

    public int getPort() {
        return port;
    }
}
package gr.thundercats.distrmapper.common;

import java.io.Serializable;


public class MasterBind implements Serializable {

    private String type;
    private String host;
    private int port;

    private boolean available;

    public boolean isAvailable() {
        return available;
    }

    public MasterBind(String type, int port) {
        this.type = type;
        this.port = port;
        this.available = true;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return type + ":" + host + ":" + port;
    }

    public ChainRequest.Destination asDestination() {
        return new ChainRequest.Destination(host, port);
    }
}

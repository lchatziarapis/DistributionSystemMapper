package gr.thundercats.distrmapper.common;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Queue;

public class ChainRequest implements Serializable {

    public void chainClear() {
        destinationChain.clear();
    }

    public static class Destination implements Serializable {
        private String host;
        private int port;

        public Destination(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        @Override
        public String toString() {
            return host + ":" + port;
        }
    }

    private Queue<Destination> destinationChain;
    private int poolSize;

    private String sourceString;

    public void setSourceString(String sourceString) {
        this.sourceString = sourceString;
    }

    public ChainRequest() {
        destinationChain = new ArrayDeque<>();
    }

    public void chainPush(String host, int port) {
        chainPush(new Destination(host, port));
    }

    public void chainPush(Destination destination) {
        destinationChain.add(destination);
    }

    public Destination chainNext() {
        return destinationChain.poll();
    }

    public Destination chainPeek() {
        return destinationChain.peek();
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    @Override
    public int hashCode() {
        return sourceString != null ? sourceString.hashCode() : 0;
    }
}

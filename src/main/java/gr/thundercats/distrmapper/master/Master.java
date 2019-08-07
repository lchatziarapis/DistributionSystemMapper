package gr.thundercats.distrmapper.master;

import gr.thundercats.distrmapper.Bootstrap;
import gr.thundercats.distrmapper.common.Directions;
import gr.thundercats.distrmapper.common.MasterBind;
import gr.thundercats.distrmapper.common.Worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;


public class Master extends Worker<Directions> {

    private Queue<Directions> queue = new ArrayDeque<>();
    private List<MasterBind> boundServices = new ArrayList<>();
    private MasterCache<Directions> directionsCache = new MasterCache<>(100);

    private boolean isWorking = false;

    public Master(int port) {
        super(Directions.class, port);
    }

    @Override
    public void received(int port, Directions object) {
        receiveDirections(object);
    }

    @Override
    public void receiverLoopable(Socket socket) {
        try {
            ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
            Integer sourcePort = stream.readInt();
            Object object = stream.readObject();

            System.out.println("Received: " + object.getClass().getSimpleName());

            if (MasterBind.class == object.getClass()) {

                MasterBind bind = (MasterBind) object;
                bind.setHost(socket.getRemoteSocketAddress().toString().substring(1).split(":")[0]);
                System.out.println("Bind with other worker: " + bind);

                boundServices.add(bind);

            } else if (Directions.class == object.getClass()) {
                received(sourcePort, (Directions) object);
            } else {
                System.err.println(getClass().getName() + " received invalid object class [" + object.getClass() + "]");
            }
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void requestDirections(Directions request) {
        Directions result = directionsCache.cacheRequest(request.requestHashCode());

        if (isWorking) {
            queue.add(request);
            return;
        }

        if (result != null) {
            System.out.println("This was cached!");
            receiveDirections(result);

        } else {
            MasterBind reducer = boundServices.stream().filter(bind -> bind.getType().equals("ReduceWorker") && bind.isAvailable()).findAny().get();
            int count = (int) boundServices.stream().filter(bind -> bind.getType().equals("MapWorker") && bind.isAvailable()).count();

            boundServices.stream().filter(bind -> bind.getType().equals("MapWorker") && bind.isAvailable()).forEach(bind -> {
                request.chainClear();

                request.chainPush(bind.asDestination());
                request.chainPush(reducer.asDestination());
                request.chainPush(Bootstrap.MASTER_HOST, getPort());

                request.setPoolSize(count);

                isWorking = true;
                dispatch(request);
            });
        }
    }

    @Override
    public void dispatchFail(String host, int port, Object object, IOException ex) {
        for (MasterBind bind : boundServices) {
            if (bind.getHost().equals(host) && bind.getPort() == port) {
                bind.setAvailable(false);

                if (object instanceof Directions) {
                    Directions directions = (Directions) object;
                    int count = (int) boundServices.stream().filter(bb -> bb.getType().equals("MapWorker") && bb.isAvailable()).count();

                    directions.setPoolSize(count);
                    requestDirections(directions);
                }

                System.out.println(host + ":" + port + " -> Host has become unavailable!");
                return;
            }
        }
    }

    private void receiveDirections(Directions request) {

        if (request.getPath() == null || request.getPath().size() == 0 && !request.sendAPIRequest()) { //No paths could be found.
            List<MasterBind> availableWorkers = boundServices.stream().filter(bind -> bind.getType().equals("MapWorker") && bind.isAvailable()).collect(Collectors.toList());

            int wantedWorker = Math.abs(request.requestHashCode()) % availableWorkers.size();

            Directions directions = new Directions(request.getStart(), request.getDestination(), true);

            directions.chainClear();
            directions.chainPush(availableWorkers.get(wantedWorker).getHost(), availableWorkers.get(wantedWorker).getPort());
            directions.chainPush(Bootstrap.MASTER_HOST, getPort());
            directions.setPoolSize(1);

            dispatch(directions);
        } else {

            directionsCache.cachePush(request, request.requestHashCode());
            System.out.println(request);

        }

        isWorking = false;
        if (!queue.isEmpty())
            requestDirections(queue.poll());
    }
}

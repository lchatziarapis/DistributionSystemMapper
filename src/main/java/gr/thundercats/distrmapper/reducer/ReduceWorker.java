package gr.thundercats.distrmapper.reducer;

import gr.thundercats.distrmapper.common.Directions;
import gr.thundercats.distrmapper.common.LatLng;
import gr.thundercats.distrmapper.common.Worker;

import java.util.*;

public class ReduceWorker extends Worker<Directions> {

    private HashMap<Integer, ReduceJob<Directions>> directionsStore = new HashMap<>();

    public ReduceWorker(int port) {
        super(Directions.class, port);
    }

    @Override
    public void received(int port, Directions object) {

        int hash = object.hashCode();

        if (!directionsStore.containsKey(hash))
            directionsStore.put(hash, new ReduceJob<>(hash, object.getPoolSize()));

        ReduceJob<Directions> reduceJob = directionsStore.get(hash);

        if (reduceJob.getExpectedSize() != object.getPoolSize()) {
            reduceJob.restartJob(object.getPoolSize());
        }

        reduceJob.dataPush(object);

        if (reduceJob.canProcess()) {
            Optional<List<LatLng>> result = reduceJob.getData().stream()
                    .filter(directions -> directions.getPath() != null)
                    .filter(directions -> directions.getPath().size() > 0)
                    .reduce((directions, directions2) -> DistanceCalculator.reduce(directions, directions2, object))
                    .map(Directions::getPath);

            if (result.isPresent())
                object.setPath(result.get());
            else
                object.setPath(Collections.emptyList());

            dispatch(object);
            directionsStore.remove(hash);
        }
    }
}

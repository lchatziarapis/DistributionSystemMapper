package gr.thundercats.distrmapper.mapper;

import com.google.gson.Gson;
import gr.thundercats.distrmapper.common.Directions;
import gr.thundercats.distrmapper.common.LatLng;
import gr.thundercats.distrmapper.common.Worker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MapWorker extends Worker<Directions> {

    private MapStorage mapStorage;

    public MapWorker(int port) {
        super(Directions.class, port);

        mapStorage = new MapStorage();
        loadStorage();
    }

    public void loadStorage() {
        String fileName = "WorkerStore." + getPort() + ".json";
        try {
            String file = FileUtils.readFileToString(new File(fileName), "UTF8");
            Gson gson = new Gson();

            mapStorage = gson.fromJson(file, MapStorage.class);
            return;
        } catch (FileNotFoundException ex) {
            System.out.println("No storage file found, creating...");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        mapStorage = new MapStorage();
    }

    public void saveStorage() {
        String fileName = "WorkerStore." + getPort() + ".json";
        try {
            Gson gson = new Gson();
            FileUtils.writeStringToFile(new File(fileName), gson.toJson(mapStorage), "UTF8");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void kill() {
        saveStorage();
        super.kill();
    }

    @Override
    public void received(int port, Directions object) {

        if (mapStorage.getStoredDirections().containsKey(object.requestHashCode())) {
            object.setPath(mapStorage.getStoredDirections().get(object.requestHashCode()).getPath());
            System.out.println("Found in memory.");

        } else if (object.sendAPIRequest()) {
            List<LatLng> direction = new GoogleMapsAPI().getDirections(object);
            object.setPath(direction);

            mapStorage.getStoredDirections().put(object.requestHashCode(), new StoredDirections(object.getStart(), object.getDestination(), direction));
            saveStorageAsync();
        } else {
            object.setPath(Collections.emptyList());
        }
        dispatch(object);
    }

    private void saveStorageAsync() {
        new Thread(this::saveStorage).start();
    }


}

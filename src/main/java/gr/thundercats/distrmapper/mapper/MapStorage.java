package gr.thundercats.distrmapper.mapper;

import java.util.HashMap;

public class MapStorage {

    private HashMap<Integer, StoredDirections> storedDirections = new HashMap<>();

    public HashMap<Integer, StoredDirections> getStoredDirections() {
        return storedDirections;
    }
}

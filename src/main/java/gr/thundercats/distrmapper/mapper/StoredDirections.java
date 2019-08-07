package gr.thundercats.distrmapper.mapper;

import gr.thundercats.distrmapper.common.LatLng;

import java.util.List;

public class StoredDirections {

    private LatLng start, destination;
    private List<LatLng> path;

    public StoredDirections(LatLng start, LatLng destination, List<LatLng> path) {
        this.start = start;
        this.destination = destination;
        this.path = path;
    }

    public LatLng getStart() {
        return start;
    }

    public LatLng getDestination() {
        return destination;
    }

    public List<LatLng> getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.roundUp(2).hashCode() : 0;
        result = 31 * result + (destination != null ? destination.roundUp(2).hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StoredDirections{" +
                "start=" + start +
                ", destination=" + destination +
                ", path=" + path +
                '}';
    }
}

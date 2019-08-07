package gr.thundercats.distrmapper.common;

import java.util.ArrayList;
import java.util.List;

public class Directions extends ChainRequest {

    private LatLng start, destination;
    private List<LatLng> path;
    private boolean sendAPIRequest;

    public Directions(LatLng start, LatLng destination, boolean sendAPIRequest) {
        this.start = start;
        this.destination = destination;
        this.sendAPIRequest = sendAPIRequest;
        this.path = new ArrayList<>();
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

    public boolean sendAPIRequest() {
        return sendAPIRequest;
    }

    public void setStart(LatLng start) {
        this.start = start;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public void setPath(List<LatLng> path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Directions{" +
                "start=" + start +
                ", destination=" + destination +
                ", path=" + path +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Directions that = (Directions) o;

        if (start != null ? !start.equals(that.start) : that.start != null) return false;
        if (destination != null ? !destination.equals(that.destination) : that.destination != null) return false;
        return path != null ? path.equals(that.path) : that.path == null;
    }

    public int requestHashCode() {
        int result = start != null ? start.roundUp(2).hashCode() : 0;
        result = 31 * result + (destination != null ? destination.roundUp(2).hashCode() : 0);
        return result;
    }

    @Override
    public int hashCode() {
        int result = requestHashCode();
        result = 31 * result + super.hashCode();
        return result;
    }
}

package gr.thundercats.distrmapper.common;

import java.io.Serializable;


public class Request implements Serializable
{
    private LatLng start;
    private LatLng destination;

    public Request(LatLng start,LatLng destination)
    {
        this.start = start;
        this.destination = destination;
    }

    public LatLng getStart()
    {
        return start;
    }

    public LatLng getDestination()
    {
        return destination;
    }

    @Override
    public String toString()
    {
        return "Start: " + start + " " + "Destination: " + destination;
    }

    @Override
    public boolean equals(Object o)
    {
        Request request = (Request)o;
        boolean a = start.equals(request.getStart());
        boolean b = destination.equals(request.getDestination());
        return a && b;
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        return result;
    }
}

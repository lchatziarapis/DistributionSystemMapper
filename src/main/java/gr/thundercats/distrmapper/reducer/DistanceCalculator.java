package gr.thundercats.distrmapper.reducer;

import gr.thundercats.distrmapper.common.Directions;
import gr.thundercats.distrmapper.common.LatLng;

public class DistanceCalculator {

    public static double distanceFrom(LatLng latLng1, LatLng latLng2) {
        return distanceFrom(latLng1.getLatitude(), latLng1.getLongitude(), latLng2.getLatitude(), latLng2.getLongitude());
    }

    public static double distanceFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }

    public static Directions reduce(Directions directions, Directions directions1, Directions request) {
        double distance1 = distanceFrom(directions.getStart(), request.getStart());
        double distance2 = distanceFrom(directions.getDestination(), request.getDestination());
        double distance3 = distanceFrom(directions1.getStart(), request.getStart());
        double distance4 = distanceFrom(directions1.getDestination(), request.getDestination());

        if (distance1 + distance2 < distance3 + distance4) {
            return directions;
        } else {
            return directions1;
        }
    }
}

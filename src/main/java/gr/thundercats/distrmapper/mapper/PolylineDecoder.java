package gr.thundercats.distrmapper.mapper;

import gr.thundercats.distrmapper.common.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PolylineDecoder {

    public static List<LatLng> decode(String encoded, double precision) {
        List<LatLng> track = new ArrayList<>();
        int index = 0;
        int lat = 0, lng = 0;

        while (index < encoded.length()) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / precision, (double) lng / precision);
            track.add(p);
        }
        return track;
    }

}

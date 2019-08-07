package gr.thundercats.distrmapper.mapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gr.thundercats.distrmapper.common.Directions;
import gr.thundercats.distrmapper.common.LatLng;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class GoogleMapsAPI {

    private static final String API_KEY = "AIzaSyBNK_8Ymg4nMspgW_R-s3UhqxWjF9ssUrA";
    private static final String API_METHOD = "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=%s";

    public List<LatLng> getDirections(LatLng start, LatLng destination) {

        try {
            String result = IOUtils.toString(new URL(String.format(API_METHOD, start.toString(), destination.toString(), API_KEY)), "UTF8");
            JsonParser parser = new JsonParser();

            JsonObject jsonObject = parser.parse(result).getAsJsonObject();
            JsonArray routes = jsonObject.getAsJsonArray("routes");

            if (routes.size() > 0) {
                JsonObject route = routes.get(0).getAsJsonObject();
                JsonObject overview_polyline = route.getAsJsonObject("overview_polyline");
                String polyline = overview_polyline.get("points").getAsString();

                return PolylineDecoder.decode(polyline, 1E5);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<LatLng> getDirections(Directions object) {
        return getDirections(object.getStart(), object.getDestination());
    }
}

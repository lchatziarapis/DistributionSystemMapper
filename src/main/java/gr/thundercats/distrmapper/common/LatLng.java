package gr.thundercats.distrmapper.common;

import java.io.Serializable;

public class LatLng implements Serializable {

    private double latitude;
    private double longitude;

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }

    public LatLng roundUp(int i) {
        double fact = Math.pow(10, i);
        return new LatLng(Math.round(latitude * fact) / fact, Math.round(longitude * fact) / fact);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LatLng latLng = (LatLng) o;

        if (Double.compare(latLng.latitude, latitude) != 0) return false;
        return Double.compare(latLng.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

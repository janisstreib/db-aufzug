package me.streib.janis.dbaufzug.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class LocationLatLong {

    public static LocationLatLong getLocationLatLongByJSON(JSONObject jsObject) {
        return new LocationLatLong(jsObject);
    }

    private double lat = -1, longi = -1;

    public LocationLatLong(JSONObject jsObject) {
        if (!(jsObject.isNull("geocoordX") || !jsObject.isNull("geocoordX"))) {
            lat = jsObject.getDouble("geocoordY");
            longi = jsObject.getDouble("geocoordX");
        }
    }

    public LocationLatLong(ResultSet res) throws SQLException {
        this.lat = res.getDouble("locationlat");
        this.longi = res.getDouble("locationlong");
    }

    public double getLat() {
        return lat;
    }

    public double getLongi() {
        return longi;
    }

}

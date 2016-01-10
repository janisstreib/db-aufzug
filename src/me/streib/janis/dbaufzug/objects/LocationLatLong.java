package me.streib.janis.dbaufzug.objects;

import org.json.JSONObject;

public class LocationLatLong {
	
	public static LocationLatLong getLocationLatLongByJSON(JSONObject jsObject) {
		return new LocationLatLong(jsObject);
	}
	
	private double lat, longi;

	public LocationLatLong(JSONObject jsObject) {
		lat = jsObject.getDouble("geocoordY");
		longi = jsObject.getDouble("geocoordX");
	}

	public double getLat() {
		return lat;
	}

	public double getLongi() {
		return longi;
	}

}

package me.streib.janis.dbaufzug.objects;

import java.util.LinkedList;

import org.json.JSONObject;

public class Station {
	
	public static Station getStationByJSON(JSONObject jsObject) {
		return new Station(jsObject);
	}
	
	private long stationNumber;
	private String name;
	private LinkedList<Facility> facilities = new LinkedList<Facility>();
	
	public Station(JSONObject jsObject) {
		stationNumber = jsObject.getLong("stationnumber");
		name = jsObject.getString("name");
	}
	
	public LinkedList<Facility> getFacilities() {
		return facilities;
	}
	public String getName() {
		return name;
	}
	public long getStationNumber() {
		return stationNumber;
	}
	
}

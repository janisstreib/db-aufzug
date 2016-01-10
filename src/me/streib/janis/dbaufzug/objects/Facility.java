package me.streib.janis.dbaufzug.objects;

import org.json.JSONObject;

public abstract class Facility {

	public static Facility getFacilityByJSON(JSONObject jsObject) {
		if (jsObject.getString("type").equals("ELEVATOR"))
			return new Elevator(jsObject);
		if (jsObject.getString("type").equals("ESCALATOR"))
			return new Escalator(jsObject);
		return null;
	}

	private long equipmentNumber;
	private String description = null;
	private LocationLatLong location;
	private Station station;
	private State state;

	public Facility(JSONObject jsObject) {
		equipmentNumber = jsObject.getLong("equipmentnumber");
		if (!jsObject.isNull("description"))
			description = jsObject.getString("description");
		location = new LocationLatLong(jsObject);
		state = State.valueOf(jsObject.getString("state"));
	}

	public String getDescription() {
		return description;
	}

	public long getEquipmentNumber() {
		return equipmentNumber;
	}

	public LocationLatLong getLocation() {
		return location;
	}

	public State getState() {
		return state;
	}

	public Station getStation() {
		return station;
	}
}

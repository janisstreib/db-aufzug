package me.streib.janis.dbaufzug.objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import me.streib.janis.dbaufzug.DatabaseConnection;

import org.json.JSONObject;

public abstract class Facility {

	public static LinkedList<Facility> getAllFacilities() throws SQLException {
		LinkedList<Facility> res = new LinkedList<Facility>();
		PreparedStatement prep = DatabaseConnection.getInstance().prepare(
				"SELECT * FROM facilities");
		ResultSet resSet = prep.executeQuery();
		resSet.beforeFirst();
		while (resSet.next()) {
			if (resSet.getString("type").equals("ELEVATOR"))
				res.add(new Elevator(resSet));
			if (resSet.getString("type").equals("ESCALATOR"))
				res.add(new Escalator(resSet));
		}
		return res;
	}

	public static Facility getFacilityByJSON(JSONObject jsObject)
			throws SQLException {
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

	public Facility(JSONObject jsObject) throws SQLException {
		equipmentNumber = jsObject.getLong("equipmentnumber");
		if (!jsObject.isNull("description"))
			description = jsObject.getString("description");
		location = new LocationLatLong(jsObject);
		state = State.valueOf(jsObject.getString("state"));
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"INSERT INTO facilities SET id=?, `type`=?, description=?, locationlat=?, locationlong=?, station=? ON DUPLICATE KEY UPDATE description=?"); // We
																																										// assume,
																																										// facilities
																																										// are
																																										// stationary...
		prep.setLong(1, equipmentNumber);
		prep.setString(2, getClass().getSimpleName().toUpperCase());
		prep.setString(3, description);
		prep.setString(7, description);
		prep.setDouble(4, location.getLat());
		prep.setDouble(5, location.getLongi());
		prep.setLong(6, jsObject.getLong("stationnumber")); // TODO abstraction
		prep.execute();
		prep = DatabaseConnection.getInstance().prepare(
				"INSERT INTO stats SET time=?, facility=?, state=?");
		prep.setLong(1, System.currentTimeMillis());
		prep.setLong(2, equipmentNumber);
		prep.setString(3, state.name());
		prep.execute();

	}

	public Facility(ResultSet res) throws SQLException {
		this.description = res.getString("description");
		this.equipmentNumber = res.getLong("id");
		this.location = new LocationLatLong(res);
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT state FROM stats WHERE facility=? ORDER BY time DESC LIMIT 1");
		prep.setLong(1, equipmentNumber);
		ResultSet stat = prep.executeQuery();
		stat.first();
		this.state = State.valueOf(stat.getString("state"));
		stat.close();
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

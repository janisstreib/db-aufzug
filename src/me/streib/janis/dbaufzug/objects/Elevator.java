package me.streib.janis.dbaufzug.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class Elevator extends Facility {

	public Elevator(JSONObject jsObject, long time) throws SQLException {
		super(jsObject, time);
	}

	public Elevator(ResultSet resultSet) throws SQLException {
		super(resultSet);
	}
}

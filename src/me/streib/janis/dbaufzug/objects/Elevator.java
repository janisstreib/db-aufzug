package me.streib.janis.dbaufzug.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class Elevator extends Facility {

	public Elevator(JSONObject jsObject) throws SQLException {
		super(jsObject);
	}

	public Elevator(ResultSet resultSet) throws SQLException {
		super(resultSet);
	}
}

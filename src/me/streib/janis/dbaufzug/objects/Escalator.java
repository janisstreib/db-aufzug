package me.streib.janis.dbaufzug.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class Escalator extends Facility {

	public Escalator(JSONObject jsObject) throws SQLException {
		super(jsObject);
	}

	public Escalator(ResultSet resultSet) throws SQLException {
		super(resultSet);
	}

}

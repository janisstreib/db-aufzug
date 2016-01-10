package me.streib.janis.dbaufzug.objects;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import me.streib.janis.dbaufzug.DatabaseConnection;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Station {

	public static Station getStationByJSON(JSONObject jsObject) {
		return new Station(jsObject);
	}

	private long stationNumber;
	private String name;
	private String bl;
	private boolean fern;
	private boolean nah;
	private int facilityCount;

	public Station(JSONObject jsObject) {
		stationNumber = jsObject.getLong("stationnumber");
		name = jsObject.getString("name");
	}

	public Station(long stationNumber, String name, String bundesland,
			boolean fern, boolean nah) throws SQLException,
			MalformedURLException, IOException {
		this.stationNumber = stationNumber;
		this.nah = nah;
		this.fern = fern;
		this.bl = bundesland;
		this.name = name;
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"INSERT INTO stations SET id=?, name=?, bl=?, fern=?, nah=?, facilityCount=? ON DUPLICATE KEY UPDATE facilityCount=?");
		prep.setLong(1, stationNumber);
		prep.setString(2, name);
		prep.setString(3, bundesland);
		prep.setBoolean(4, fern);
		prep.setBoolean(5, nah);
		try {
			HttpURLConnection stationCount = (HttpURLConnection) new URL(
					"https://adam.noncd.db.de/api/v1.0/stations/"
							+ stationNumber).openConnection();
			this.facilityCount = new JSONObject(new JSONTokener(
					stationCount.getInputStream())).getJSONArray("facilities")
					.length();
			prep.setInt(7, facilityCount);
			prep.setInt(6, facilityCount);
		} catch (IOException e) {
			System.out.println("Wait at " + stationNumber + "... "
					+ e.getMessage());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		prep.execute();
	}

	public String getName() {
		return name;
	}

	public long getStationNumber() {
		return stationNumber;
	}

	public boolean isFern() {
		return fern;
	}

	public boolean isNah() {
		return nah;
	}

}

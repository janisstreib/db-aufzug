package me.streib.janis.dbaufzug;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import me.streib.janis.dbaufzug.objects.Facility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

public class Updater implements Runnable {
	private long interval;

	public Updater(long updateInterval) {
		this.interval = updateInterval;
	}

	public void run() {
		while (true) {
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(
						"https://adam.noncd.db.de/api/v1.0/facilities?type[]=ESCALATOR&type[]=ELEVATOR&state[]=ACTIVE&state[]=INACTIVE&state[]=UNKNOWN")
						.openConnection();
				conn.setDoInput(true);
				JSONArray ar = new JSONArray(new JSONTokener(
						conn.getInputStream()));
				for (int i = 0; i < ar.length(); i++) {
					Facility.getFacilityByJSON(ar.getJSONObject(i));
				}
				System.out.println("Database update completed sccuessfully.");
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

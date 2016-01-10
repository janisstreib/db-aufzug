package me.streib.janis.dbaufzug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import me.streib.janis.dbaufzug.objects.Facility;
import me.streib.janis.dbaufzug.objects.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

public class Updater implements Runnable {
	private long interval;
	private long lastImport = 0;
	private static final String stationFile = "stations.csv";

	public Updater(long updateInterval) {
		this.interval = updateInterval;
	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("Updating db...");
				if (new File(stationFile).lastModified() > lastImport) {
					lastImport = System.currentTimeMillis();
					try (BufferedReader read = new BufferedReader(
							new InputStreamReader(new FileInputStream(
									stationFile)));) {
						String tmp;
						read.readLine(); // Skip first line
						while ((tmp = read.readLine()) != null) {
							String[] elements = tmp.split(";");
							boolean done = false;
							while (!done) {
								try {
									new Station(Long.parseLong(elements[2]),
											elements[3], elements[0],
											elements[11].equals("ja"),
											elements[12].equals("ja"));
									done = true;
									Thread.sleep(1000);
								} catch (IOException e) {
									System.out.println("Wait..."
											+ e.getMessage());

									Thread.sleep(1000);
								}
							}
						}
						System.out.println("Station update finished.");
					}
				}
				Thread.sleep(1000);
				HttpURLConnection conn = (HttpURLConnection) new URL(
						"https://adam.noncd.db.de/api/v1.0/facilities?type[]=ESCALATOR&type[]=ELEVATOR&state[]=ACTIVE&state[]=INACTIVE&state[]=UNKNOWN")
						.openConnection();
				conn.setDoInput(true);
				JSONArray ar = new JSONArray(new JSONTokener(
						conn.getInputStream()));
				long time = System.currentTimeMillis();
				for (int i = 0; i < ar.length(); i++) {
					Facility.getFacilityByJSON(ar.getJSONObject(i), time);
				}
				System.out.println("Database update completed sccuessfully.");
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

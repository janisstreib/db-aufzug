package me.streib.janis.dbaufzug;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.dbaufzug.objects.Facility;
import me.streib.janis.dbaufzug.objects.LocationLatLong;
import me.streib.janis.dbaufzug.objects.State;

import org.cacert.gigi.output.template.IterableDataset;
import org.json.JSONArray;
import org.json.JSONTokener;

public class MainPage extends Page {

	public MainPage() {
		super("DB elevator map");
	}
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(
				"https://adam.noncd.db.de/api/v1.0/facilities?type[]=ESCALATOR&type[]=ELEVATOR&state[]=ACTIVE&state[]=INACTIVE&state[]=UNKNOWN")
				.openConnection();
		conn.setDoInput(true);
		JSONArray ar = new JSONArray(new JSONTokener(conn.getInputStream()));
		LinkedList<Facility> facilities = new LinkedList<Facility>();
		int up = 0;
		for (int i = 0; i < ar.length(); i++) {
			Facility fac = Facility.getFacilityByJSON(ar.getJSONObject(i));
			facilities.add(fac);
			if(fac.getState() == State.ACTIVE)
				up++;
		}
		vars.put("facilities", new IterableDataset() {
			int i = 0;

			@Override
			public boolean next(Map<String, Object> vars) {
				if (i + 1 >= ar.length())
					return false;
				Facility fac = facilities.removeFirst();
				vars.put("type", fac.getClass().getSimpleName());
				LocationLatLong loc = fac.getLocation();
				vars.put("lat", loc.getLat());
				vars.put("long", loc.getLongi());
				vars.put("state", fac.getState().name().toLowerCase());
				vars.put("descr", fac.getDescription());
				i++;
				return true;
			}
		});
		
		vars.put("percents", (ar.length()/100f)*up);
		vars.put("amount", ar.length());
		getDefaultTemplate().output(resp.getWriter(), vars);
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

}

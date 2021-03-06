package me.streib.janis.dbaufzug.pages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.dbaufzug.Page;
import me.streib.janis.dbaufzug.objects.Facility;
import me.streib.janis.dbaufzug.objects.LocationLatLong;
import me.streib.janis.dbaufzug.objects.State;

import org.cacert.gigi.output.template.IterableDataset;
import org.json.JSONException;

public class MainPage extends Page {

	public MainPage() {
		super("Karte");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException, JSONException,
			SQLException {
		final LinkedList<Facility> facilities = Facility.getAllFacilities();
		int up = 0;
		int facs = facilities.size();
		Iterator<Facility> iter = facilities.iterator();
		while (iter.hasNext()) {
			Facility facility = iter.next();
			if (facility.getLocation().getLat() == -1
					|| facility.getLocation().getLongi() == -1) {
				iter.remove();
			}
			if (facility.getState() == State.ACTIVE) {
				up++;
			}
		}
		vars.put("facilities", new IterableDataset() {

			@Override
			public boolean next(Map<String, Object> vars) {
				if (facilities.isEmpty()) {
					return false;
				}
				Facility fac = facilities.removeFirst();
				vars.put("type", fac.getClass().getSimpleName());
				LocationLatLong loc = fac.getLocation();
				vars.put("lat", loc.getLat());
				vars.put("long", loc.getLongi());
				vars.put("state", fac.getState().name().toLowerCase());
				vars.put("descr", fac.getDescription());
				return true;
			}
		});

		vars.put("percents", (facs / 100f) * up);
		vars.put("amount", facs);
		getDefaultTemplate().output(resp.getWriter(), vars);
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

}

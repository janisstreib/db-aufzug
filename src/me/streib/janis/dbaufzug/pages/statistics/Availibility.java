package me.streib.janis.dbaufzug.pages.statistics;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import me.streib.janis.dbaufzug.DatabaseConnection;
import me.streib.janis.dbaufzug.objects.State;
import me.streib.janis.dbaufzug.pages.StatsPage;

import org.json.JSONArray;

public class Availibility extends StatisticOutputable {

	@Override
	public void output(PrintWriter out, Map<String, Object> vars) {
		try {
			JSONArray categories = new JSONArray();

			JSONArray elevDataActive = new JSONArray();
			JSONArray elevDataInactive = new JSONArray();
			JSONArray elevDataUnknown = new JSONArray();

			JSONArray escDataActive = new JSONArray();
			JSONArray escDataInactive = new JSONArray();
			JSONArray escDataUnknown = new JSONArray();

			PreparedStatement prep = DatabaseConnection
					.getInstance()
					.prepare(
							"SELECT time FROM stats GROUP by time ORDER BY time DESC LIMIT 24");
			ResultSet times = prep.executeQuery();
			times.beforeFirst();
			while (times.next()) {
				long time = times.getLong("time");
				Date date = new Date(time);
				categories.put(StatsPage.DE_FROMAT_TIME.format(date));
				prep = DatabaseConnection
						.getInstance()
						.prepare(
								"SELECT state, COUNT(*) as cnt FROM stats LEFT JOIN facilities ON stats.facility=facilities.id WHERE time=? AND type=? GROUP BY state");
				prep.setLong(1, time);
				prep.setString(2, "ELEVATOR");
				ResultSet elev = prep.executeQuery();
				int activElev = 0;
				int inactiveElev = 0;
				int unknownElev = 0;
				elev.beforeFirst();
				while (elev.next()) {
					if (State.ACTIVE.name().equals(elev.getString("state"))) {
						activElev = elev.getInt(2);
					} else if (State.INACTIVE.name().equals(
							elev.getString("state"))) {
						inactiveElev = elev.getInt(2);
					} else {
						unknownElev = elev.getInt(2);
					}
				}
				elevDataActive.put(activElev);
				elevDataInactive.put(inactiveElev);
				elevDataUnknown.put(unknownElev);

				elev.close();

				prep.setLong(1, time);
				prep.setString(2, "ELEVATOR");
				ResultSet esc = prep.executeQuery();
				int activEsc = 0;
				int inactiveEsc = 0;
				int unknownEsc = 0;
				esc.beforeFirst();
				while (esc.next()) {
					if (State.ACTIVE.name().equals(esc.getString("state"))) {
						activElev = esc.getInt(2);
					} else if (State.INACTIVE.name().equals(
							esc.getString("state"))) {
						inactiveElev = esc.getInt(2);
					} else {
						unknownElev = esc.getInt(2);
					}
				}
				escDataActive.put(activEsc);
				escDataInactive.put(inactiveEsc);
				escDataUnknown.put(unknownEsc);

				esc.close();

			}
			times.close();
			vars.put("avail_time", flipArray(categories).toString());
			vars.put("elev", flipArray(elevDataActive).toString());
			vars.put("esc", flipArray(escDataActive).toString());
			vars.put("elev_inact", flipArray(elevDataInactive).toString());
			vars.put("esc_inact", flipArray(escDataInactive).toString());
			vars.put("elev_unknwon", flipArray(elevDataUnknown).toString());
			vars.put("esc_unknown", flipArray(escDataUnknown).toString());
			getDefaultTemplate().output(out, vars);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static JSONArray flipArray(JSONArray in) {
		JSONArray res = new JSONArray();
		for (int i = in.length() - 1; i >= 0; i--) {
			res.put(in.get(i));
		}
		return res;
	}

}

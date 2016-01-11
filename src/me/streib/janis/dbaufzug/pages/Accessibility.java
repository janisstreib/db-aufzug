package me.streib.janis.dbaufzug.pages;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.dbaufzug.DatabaseConnection;
import me.streib.janis.dbaufzug.Page;

import org.cacert.gigi.output.template.IterableDataset;
import org.json.JSONException;

public class Accessibility extends Page {

	public Accessibility() {
		super("Barrierefreiheit");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException, JSONException,
			SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT stations.name, stations.bl, facilities.locationlat as lat, facilities.locationlong as `long` FROM stations LEFT JOIN facilities on stations.id=facilities.station LEFT JOIN stats on facilities.id=stats.facility WHERE stats.state='INACTIVE' AND stats.time=(SELECT MAX(time) FROM stats) ORDER BY stations.bl, stations.name");
		final ResultSet res = prep.executeQuery();
		res.beforeFirst();
		vars.put("brokens", new IterableDataset() {
			String currentBl;

			@Override
			public boolean next(Map<String, Object> vars) {
				try {
					if (res.next()) {
						currentBl = res.getString("bl");
						vars.put("bl", currentBl);
						res.previous();
						vars.put("bllist", new IterableDataset() {

							@Override
							public boolean next(Map<String, Object> vars) {
								try {
									if (res.next()
											&& currentBl.equals(res
													.getString("bl"))) {
										vars.put("name", res.getString("name"));
										vars.put("lat", res.getDouble("lat"));
										vars.put("long", res.getDouble("long"));
										return true;
									}
									res.previous();
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return false;
							}
						});
						return true;
					}
					res.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		getDefaultTemplate().output(resp.getWriter(), vars);
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

}

package me.streib.janis.dbaufzug.pages.statistics;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import me.streib.janis.dbaufzug.DatabaseConnection;

public class APIAvailibility extends StatisticOutputable {

	@Override
	public void output(PrintWriter out, Map<String, Object> vars) {
		try {
			PreparedStatement prep = DatabaseConnection.getInstance().prepare(
					"SELECT COUNT(1) FROM stations WHERE facilityCount > 0");
			ResultSet res = prep.executeQuery();
			res.first();
			int withAPI = res.getInt(1);
			res.close();
			prep = DatabaseConnection.getInstance().prepare(
					"SELECT COUNT(1) FROM stations WHERE facilityCount = 0");
			res = prep.executeQuery();
			res.first();
			int withOutAPI = res.getInt(1);
			vars.put("withapi", withAPI);
			vars.put("withoutapi", withOutAPI);

			getDefaultTemplate().output(out, vars);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

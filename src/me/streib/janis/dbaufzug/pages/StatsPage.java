package me.streib.janis.dbaufzug.pages;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.dbaufzug.Page;
import me.streib.janis.dbaufzug.pages.statistics.Availibility;

import org.json.JSONException;

public class StatsPage extends Page {
	private Availibility availStat = new Availibility();

	public static final SimpleDateFormat DE_FROMAT_DATE = new SimpleDateFormat(
			"dd.MM.yyyy");
	public static final SimpleDateFormat DE_FROMAT_TIME = new SimpleDateFormat(
			"HH:mm");

	public StatsPage() {
		super("Statistiken");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException, JSONException,
			SQLException {
		vars.put("availibility", availStat);
		getDefaultTemplate().output(resp.getWriter(), vars);
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

}

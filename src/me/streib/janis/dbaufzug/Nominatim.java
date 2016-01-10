package me.streib.janis.dbaufzug;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONTokener;

public class Nominatim extends Page {

	public Nominatim() {
		super("nom");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		String query = req.getParameter("query");
		HttpURLConnection conn = (HttpURLConnection) new URL("http://nominatim.openstreetmap.org/search/"+URLEncoder.encode(query, "UTF-8")+"?format=json&countrycodes=de").openConnection();
		JSONArray res = new JSONArray(new JSONTokener(conn.getInputStream()));
		resp.getWriter().println(res.toString());
	}

	@Override
	public boolean needsTemplate() {
		return false;
	}

}

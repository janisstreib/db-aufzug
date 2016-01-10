package me.streib.janis.dbaufzug.pages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.dbaufzug.Page;

import org.json.JSONException;

public class Accessibility extends Page {

	public Accessibility() {
		super("Barrierefreiheit");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException, JSONException,
			SQLException {
		getDefaultTemplate().output(resp.getWriter(), vars);
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

}

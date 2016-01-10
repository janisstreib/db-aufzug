package me.streib.janis.dbaufzug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class DBAufzugConfiguration {
	private Properties p;
	private static DBAufzugConfiguration instance;

	protected DBAufzugConfiguration(InputStream in) throws IOException,
			SQLException {
		this.p = new Properties();
		p.load(in);
		instance = this;
	}

	public int getPort() {
		return Integer.parseInt(p.getProperty("dbaufzug.port"));
	}

	public String getHostName() {
		return p.getProperty("dbaufzug.name");
	}

	protected String getDB() {
		return p.getProperty("dbaufzug.db");
	}

	protected String getDBUser() {
		return p.getProperty("dbaufzug.db.user");
	}

	protected String getDBPW() {
		return p.getProperty("dbaufzug.db.pw");
	}

	protected String getJDBCDriver() {
		return p.getProperty("dbaufzug.db.driver");
	}

	public static DBAufzugConfiguration getInstance() {
		return instance;
	}

	private void store() {
		File f = new File("conf/");
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File("conf/dbaufzug.properties");
		try {
			p.store(new FileOutputStream(f), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getMapSource() {
		return p.getProperty("dbaufzug.mapsource");
	}

	public boolean isHSTSEnabled() {
		return Boolean.getBoolean(p.getProperty("dbaufzug.hsts", "true"));
	}
}

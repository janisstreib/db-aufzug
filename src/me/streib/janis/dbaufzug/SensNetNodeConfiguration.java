package me.streib.janis.dbaufzug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class SensNetNodeConfiguration {
	private Properties p;
	private static SensNetNodeConfiguration instance;

	protected SensNetNodeConfiguration(InputStream in) throws IOException,
			SQLException {
		this.p = new Properties();
		p.load(in);
		instance = this;
	}

	public int getPort() {
		return Integer.parseInt(p.getProperty("node.port"));
	}

	public String getHostName() {
		return p.getProperty("node.name");
	}

	protected String getDB() {
		return p.getProperty("node.db");
	}

	protected String getDBUser() {
		return p.getProperty("node.db.user");
	}

	protected String getDBPW() {
		return p.getProperty("node.db.pw");
	}

	protected String getJDBCDriver() {
		return p.getProperty("node.db.driver");
	}


	public static SensNetNodeConfiguration getInstance() {
		return instance;
	}


	private void store() {
		File f = new File("conf/");
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File("conf/node.properties");
		try {
			p.store(new FileOutputStream(f), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getMapSource() {
		return p.getProperty("node.mapsource");
	}

	public boolean isHSTSEnabled() {
		return Boolean.getBoolean(p.getProperty("node.hsts", "true"));
	}
}

package me.streib.janis.dbaufzug.pages.statistics;

import java.net.URL;

import org.cacert.gigi.output.template.Outputable;
import org.cacert.gigi.output.template.Template;

public abstract class StatisticOutputable implements Outputable {
	private Template defaultTemplate;

	public StatisticOutputable() {
		URL resource = getClass().getResource(
				getClass().getSimpleName() + ".templ");
		if (resource != null) {
			defaultTemplate = new Template(resource);
		}
	}

	protected Template getDefaultTemplate() {
		return defaultTemplate;
	}
}

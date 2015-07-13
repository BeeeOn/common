import data.Devices;
import data.Language;
import generator.DevicesGenerator;
import generator.LanguagesGenerator;
import generator.formatter.AndroidFormatter;
import parser.DevicesParser;
import parser.LanguagesParser;

import java.util.List;

public class Main {

	public static final String DEFAULT_LANG_CODE = "en";

	public static void main(String[] args) {
		// Parsing and generating devices
		Devices devices = DevicesParser.parseDevices("xml/devices.xml");
		if (devices != null) {
			DevicesGenerator generator = new DevicesGenerator(devices);
			generator.generateDevices("export/objects/", "devices.java", new AndroidFormatter());
			// generator.printDevices(System.out);
		} else {
			System.err.println("Error when loading devices.");
			return;
		}

		// Parsing and generating languages
		List<Language> languages = LanguagesParser.parseLanguages("xml/languages/", "language_", ".xml");
		if (!languages.isEmpty()) {
			LanguagesGenerator generator = new LanguagesGenerator(languages);
			generator.generateLanguages("export/values/", "export/values-%s/", "generated_strings_devices.xml", DEFAULT_LANG_CODE);
			// generator.printLanguages(System.out);
		} else {
			System.err.println("Error when loading languages - nothing loaded.");
			return;
		}
	}

}

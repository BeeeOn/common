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

	public static final String SPECIFICATIONS_DIR = "../specifications/";

	public static void main(String[] args) {
		// Parsing and generating devices
		Devices devices = DevicesParser.parseDevices(SPECIFICATIONS_DIR + "devices.xml");
		if (devices != null) {
			DevicesGenerator generator = new DevicesGenerator(devices);
			generator.generateDevices(new AndroidFormatter(), "export/objects/", "devices.java");
			// generator.printDevices(System.out);
		} else {
			System.err.println("Error when loading devices.");
			return;
		}

		// Parsing and generating languages
		List<Language> languages = LanguagesParser.parseLanguages(SPECIFICATIONS_DIR + "languages/", "language_", ".xml");
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

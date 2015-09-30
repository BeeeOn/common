import data.Devices;
import data.Language;
import data.Translation;
import data.Types;
import generator.DevicesGenerator;
import generator.LanguagesGenerator;
import generator.formatter.AndroidFormatter;
import generator.formatter.GateFormatter;
import generator.formatter.WebFormatter;
import parser.DevicesParser;
import parser.LanguagesParser;
import parser.TypesParser;

import java.util.List;

public class Main {

	public static final String DEFAULT_LANG_CODE = "en";

	public static final String SPECIFICATIONS_DIR = "../specifications/";

	public static void main(String[] args) {
        // Parsing and generating devices and types
		Devices devices = DevicesParser.parseDevices(SPECIFICATIONS_DIR + "devices.xml");
        Types types = TypesParser.parseTypes(SPECIFICATIONS_DIR + "types.xml");
		if (devices != null && types != null) {
			DevicesGenerator generator = new DevicesGenerator(devices, types);
			// Generate Android data
			generator.generateDevices(new AndroidFormatter(), "export/android/", "devices.java");
			// Generate Gate data
			generator.generateDevices(new GateFormatter(), "export/gate/", "device_table.h");
			// Print data just to log
			// generator.printDevices(System.out);
		} else {
			System.err.println("Error when loading devices or types.");
			return;
		}

		// Parsing and generating languages
		List<Language> languages = LanguagesParser.parseLanguages(SPECIFICATIONS_DIR + "languages/", "language_", ".xml");
		if (!languages.isEmpty()) {
			LanguagesGenerator generator = new LanguagesGenerator(languages);
			generator.generateLanguages(new AndroidFormatter(), "export/android/res/values/", "export/android/res/values-%s/", "generated_strings_devices.xml", DEFAULT_LANG_CODE,"Saving Android's strings XML to ");
			generator.generateLanguages(new WebFormatter(), "export/webapp/res/values/", "export/webapp/res/values-%s/", "generated_strings_devices.json", DEFAULT_LANG_CODE,"Saving Webapp's strings JSON to ");
			// generator.printLanguages(System.out);
		} else {
			System.err.println("Error when loading languages - nothing loaded.");
			return;
		}

		// Check for (not) existence of translations
		for (Translation translation : Translation.translations) {
			String key = Language.TRANSLATION_PREFIX + translation.getTranslationId().toLowerCase();

			for (Language language : languages) {
				if (language.getItem(key) == null) {
					System.err.println(String.format("Translation '%s' was not found in '%s'.", translation.getTranslationId(), language.getCode()));
				}
			}
		}
	}

}

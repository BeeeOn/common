import data.Devices;
import data.Language;
import generator.AndroidGenerator;
import org.xml.sax.SAXException;
import parser.DevicesParser;
import parser.LanguageParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static final String DEFAULT_LANG_CODE = "en";

	public static void main(String[] args) {
		Devices devices = parseDevices("xml/devices.xml");
		List<Language> languages = processLanguages("xml/languages/", "language_", ".xml");

		if (devices == null || languages.isEmpty()) {
			System.err.println("Error when loading devices or languages.");
			return;
		}

		AndroidGenerator generator = new AndroidGenerator(devices, languages);
		generator.generateDevices("export/objects/", "devices.java");
		generator.generateLanguages("export/values/", "export/values-%s/", "generated_strings_devices.xml", DEFAULT_LANG_CODE);
	}

	/**
	 *
	 * @param path Path to XML file with devices definition.
	 * @return Devices object with parsed data, or null on failure
	 */
	private static Devices parseDevices(String path) {
		Devices devices = null;

		File file = new File(path);
		System.out.println(String.format("Loading devices specification from '%s'", file.getAbsolutePath()));

		try {
			devices = DevicesParser.parse(file);
		} catch (ParserConfigurationException | IOException | SAXException e) {
			e.printStackTrace();
		}

		return devices;
	}

	private static List<Language> processLanguages(String dirPath, String filenameStarts, String filenameEnds) {
		List<Language> languages = new ArrayList<>();

		File[] files = new File(dirPath).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(filenameStarts) && name.endsWith(filenameEnds);
			}
		});

		for (File file : files) {
			try {
				System.out.println(String.format("Loading translation from '%s'", file.getAbsolutePath()));

				Language language = LanguageParser.parse(file);
				if (language == null) {
					System.err.println(String.format("Error when loading language from '%s'.", file));
				}

				languages.add(language);
			} catch (ParserConfigurationException | IOException | SAXException e) {
				e.printStackTrace();
			}
		}

		return languages;
	}

}

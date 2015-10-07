package generator;

import data.Language;

import java.io.*;
import java.util.List;

/**
 * Created by Robert on 12. 7. 2015.
 */
public class LanguagesGenerator {

	private List<Language> mLanguages;

	public LanguagesGenerator(List<Language> languages) {
		mLanguages = languages;
	}

	/**
	 * Generate files with translations in different languages.
	 *
	 * @param formatter Object implementing IDevicesFormatter interface for formatting content of output file
	 * @param dirDefault Path to output folder for default language, e.g. "/values/"
	 * @param dirCodeParam Path to output folder for other languages, must contain one %s parameter for language code, e.g. "/values-%s/"
	 * @param filename Name of generated language file
	 * @param defaultLangCode Code of default language
	 * @return
	 */
	public boolean generateLanguages(ILanguagesFormatter formatter, String dirDefault, String dirCodeParam, String filename, String defaultLangCode,String message) {
		boolean result = true;

		for (Language language : mLanguages) {
			String path = language.getCode().equalsIgnoreCase(defaultLangCode) ? dirDefault : String.format(dirCodeParam, language.getCode());

			File dir = new File(path);
			dir.mkdirs();

			File output = new File(dir, filename);

			System.out.println(String.format(message + "'%s'", output.getAbsolutePath()));

			try (PrintWriter writer = new PrintWriter(output, "UTF-8")) {
				formatter.formatLanguages(writer, language);
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
				result = false;
			}
		}

		return result;
	}

	public void printLanguages(PrintStream stream) {
		for (Language language : mLanguages) {
			printLanguage(stream, language);
		}
	}

	public void printLanguage(PrintStream stream, Language language) {
		stream.println(String.format("--- LISTING OF LANGUAGE '%s' ---", language.getCode()));

		for (Language.Item item : language.getItems()) {
			String name = item.key;
			String value = item.value;

			System.out.println(String.format("%s = \"%s\"", name, value));
		}
	}

	public interface ILanguagesFormatter {
		void formatLanguages(PrintWriter writer, Language language);
	}
}

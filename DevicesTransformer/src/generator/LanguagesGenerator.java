package generator;

import data.Language;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Robert on 12. 7. 2015.
 */
public class LanguagesGenerator {

	private List<Language> mLanguages;

	public LanguagesGenerator(List<Language> languages) {
		mLanguages = languages;
	}

	public boolean generateLanguages(String dirDefault, String dirCodeParam, String filename, String defaultLangCode) {
		boolean result = true;

		for (Language language : mLanguages) {
			String path = language.getCode().equalsIgnoreCase(defaultLangCode) ? dirDefault : String.format(dirCodeParam, language.getCode());

			File dir = new File(path);
			dir.mkdirs();

			File output = new File(dir, filename);

			System.out.println(String.format("Saving Android's strings XML to '%s'", output.getAbsolutePath()));

			try (PrintWriter writer = new PrintWriter(output, "UTF-8")) {
				language.printAndroidXml(writer);
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
				result = false;
			}
		}

		return result;
	}
}

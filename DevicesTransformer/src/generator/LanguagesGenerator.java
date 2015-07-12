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
		for (Language language : mLanguages) {
			String path = language.getCode().equalsIgnoreCase(defaultLangCode) ? dirDefault : String.format(dirCodeParam, language.getCode());

			File dir = new File(path);
			dir.mkdirs();

			File output = new File(dir, filename);

			System.out.println(String.format("Saving Android's strings XML to '%s'", output.getAbsolutePath()));
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(output, "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
				// return false;
			} finally {
				if (writer != null)
					writer.close();
			}

			language.printAndroidXml(writer);
		}

		return true;
	}
}

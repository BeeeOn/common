package generator;

import data.Devices;
import data.Language;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Robert on 12. 7. 2015.
 */
public class AndroidGenerator {

	private Devices mDevices;
	private List<Language> mLanguages;

	public AndroidGenerator(Devices devices, List<Language> languages) {
		mDevices = devices;
		mLanguages = languages;
	}

	/**
	 * Generate file with devices specifications to use in BeeeOn Android application source codes.
	 *
	 * @param path Path to output folder where to place the generated file.
	 * @param filename Name of generated file.
	 * @return true on success, false when some error occured
	 */
	public boolean generateDevices(String path, String filename) {
		File dir = new File(path);
		if (!dir.mkdirs()) {
			// When directory already exists then mkdirs return false. So we just ignore it here
			// System.err.println(String.format("Can't create directory '%s'", path));
		}

		File output = new File(dir, filename);

		System.out.println(String.format("Saving devices objects to '%s'", output.getAbsolutePath()));
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(output, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}

		mDevices.printDevicesJava(writer);

		return false;
	}


	public boolean generateLanguages(String dirDefault, String dirCodeParam, String filename, String defaultLangCode) {
		for (Language language : mLanguages) {
			String path = language.getCode().equalsIgnoreCase(defaultLangCode) ? "export/values/" : String.format("export/values-%s/", language.getCode());

			File dir = new File(path);
			dir.mkdirs();

			String name = "generated_strings_devices.xml";
			File output = new File(dir, name);

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

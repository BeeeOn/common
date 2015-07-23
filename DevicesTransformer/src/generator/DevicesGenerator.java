package generator;

import data.Device;
import data.Devices;
import data.Module;
import data.Types;

import java.io.*;

/**
 * Created by Robert on 12. 7. 2015.
 */
public class DevicesGenerator {

	private Devices mDevices;

    private Types mTypes;

	public DevicesGenerator(Devices devices, Types types) {
		mDevices = devices;
        mTypes = types;
	}

	/**
	 * Generate file with devices specifications.
	 *
	 * @param formatter Object implementing IDevicesFormatter interface for formatting content of output file
	 * @param path      Path to output folder where to place the generated file.
	 * @param filename  Name of generated file.
	 * @return true on success, false when some error occured
	 */
	public boolean generateDevices(IDevicesFormatter formatter, String path, String filename) {
		File dir = new File(path);
		if (!dir.mkdirs()) {
			// When directory already exists then mkdirs return false. So we just ignore it here
			// System.err.println(String.format("Can't create directory '%s'", path));
		}

		File output = new File(dir, filename);

		System.out.println(String.format("Saving devices objects to '%s'", output.getAbsolutePath()));

		try (PrintWriter writer = new PrintWriter(output, "UTF-8")) {
			formatter.formatDevices(writer, mDevices, mTypes);
			return true;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return false;
	}

	private String getFeaturesString(Device.Features features) {
		String res = "";
		if (features != null) {
			if (features.hasRefresh())
				res += ", refresh(" + features.getDefaultRefresh() + ")";
			if (features.hasBattery())
				res += ", battery";
			if (features.hasRssi())
				res += ", rssi";
			if (features.hasLed())
				res += ", led";
			if (!res.isEmpty())
				res = res.substring(2);
		}
		return String.format("[%s]", res);
	}

	public void printDevices(PrintStream stream) {
		stream.println(String.format("--- LISTING OF DEVICES (version %s) ---", mDevices.getVersion()));

		for (Device device : mDevices.getDevices()) {
			stream.println(String.format("[%d] %s",
					device.getTypeId(),
					device.getTypeName()));

			stream.println(String.format("\tManufacturer: %s\n\tName: %s\n\tFeatures: %s",
					device.getManufacturer().getTranslationId(),
					device.getName().getTranslationId(),
					getFeaturesString(device.getFeatures())
			));

			stream.println("\tDevices:");
			for (Module module : device.getModules()) {
				stream.println(String.format("\t\t[%d] \tType: %s%s",
						module.getId(),
						module.getType(),
						module.getOrder() != null ? "\tOrder: " + module.getOrder() : ""
				));

				stream.println(String.format("\t\t\t\tName: %s%s\t",
						module.getGroup() != null ? module.getGroup().getTranslationId() : "",
						module.getName().getTranslationId()
				));
			}

			stream.println("------");
		}
	}

	public interface IDevicesFormatter {
		void formatDevices(PrintWriter writer, Devices devices, Types types);
	}
}

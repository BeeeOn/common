package generator;

import data.Device;
import data.Devices;
import data.Module;
import data.Translation;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Robert on 12. 7. 2015.
 */
public class DevicesGenerator {

	private Devices mDevices;

	public DevicesGenerator(Devices devices) {
		mDevices = devices;
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

		printAndroidFormat(writer);

		return false;
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
				stream.println(String.format("\t\t[%d] \tType: %s\tOffset: %d%s",
						module.getId(),
						module.getType(),
						module.getOffset(),
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

	private String getFeaturesString(Device.Features features) {
		String res = "";
		if (features != null) {
			if (features.hasRefresh())
				res += ", refresh(" + features.getDefaultRefresh() + ")";
			if (features.hasBattery())
				res += ", battery";
			if (features.hasLed())
				res += ", led";
			if (!res.isEmpty())
				res = res.substring(2);
		}
		return String.format("[%s]", res);
	}

	public void printAndroidFormat(PrintWriter writer) {
		writer.println("/** BEGIN OF GENERATED CONTENT **/");

		Iterator<Device> it = mDevices.getDevices().iterator();
		while (it.hasNext()) {
			Device device = it.next();

			Device.Features features = device.getFeatures();
			if (features == null) {
				features = new Device.Features();
			}

			// Begin of type definition
			writer.println(String.format("TYPE_%d(\"%d\", \"%s\", %s, %s, new DeviceFeatures(%s, %s, %s)) {",
					device.getTypeId(),
					device.getTypeId(),
					device.getTypeName(),
					device.getName().getResourceId(),
					device.getManufacturer().getResourceId(),
					features.hasRefresh() ? features.getDefaultRefresh().toString() : "null",
					features.hasLed() ? "true" : "false",
					features.hasBattery() ? "true" : "false"
			));

			// Begin of createModules() method
			writer.println("\t@Override\n\tpublic List<Module> createModules(Device device) {");

			// Begin of modules array
			writer.println("\t\treturn Arrays.asList(");

			Iterator<Module> itModule = device.getModules().iterator();
			while (itModule.hasNext()) {
				Module module = itModule.next();
				Translation tgroup = module.getGroup();
				String group = tgroup != null ? tgroup.getResourceId() : "null";

				Translation tname = module.getName();
				String name = tname != null ? tname.getResourceId() : "null";

				writer.print(String.format("\t\t\t\tnew Module(device, \"%d\", %s, %d, %s, %s, %s, %b",
								module.getId(),
								module.getType(),
								module.getOffset(),
								module.getOrder(),
								group,
								name,
								module.isActuator())
				);

				if (!module.getRules().isEmpty()) {
					writer.println(", Arrays.asList(");

					Iterator<Module.Rule> itRule = module.getRules().iterator();
					while (itRule.hasNext()) {
						Module.Rule rule = itRule.next();

						String ids = Arrays.toString(rule.hideModulesIds);
						ids = ids.substring(1, ids.length() - 1);

						writer.print(String.format("\t\t\t\t\t\tnew Module.Rule(%d, new int[] {%s})",
								rule.value,
								ids
						));

						writer.println(itRule.hasNext() ? "," : "");
					}

					writer.print("\t\t\t\t)");
				} else {
					writer.print(", null");
				}

				if (!module.getValues().isEmpty()) {
					writer.println(", Arrays.asList(");

					Iterator<Module.Value> itValue = module.getValues().iterator();
					while (itValue.hasNext()) {
						Module.Value value = itValue.next();

						writer.print(String.format("\t\t\t\t\t\tnew EnumValue.Item(%d, \"%d\", %s)",
								value.id,
								value.id,
								value.translation.getResourceId() // value name resource
						));

						writer.println(itValue.hasNext() ? "," : "");
					}

					writer.print("\t\t\t\t)");
				} else if (module.getConstraints() != null) {
					Module.Constraints constraints = module.getConstraints();

					writer.print(String.format(", new BaseValue.Constraints(%s, %s, %s)",
							constraints.getMin(),
							constraints.getMax(),
							constraints.getGranularity()
					));
				}

				writer.println(itModule.hasNext() ? ")," : ")");
			}

			writer.println("\t\t);");
			// End of modules array


			writer.println("\t}");
			// End of createModules() method

			writer.println(it.hasNext() ? "}," : "};");
			// End of type definition
		}

		writer.println(String.format("\n/** Version from specification of this devices list */\npublic static final String DEVICES_VERSION = \"%s\";", mDevices.getVersion()));
		writer.println(String.format("\n/** Generation time (GMT) of this devices list */\npublic static final long DEVICES_DATE = %sl;", new Date().getTime()));

		writer.println("\n/** END OF GENERATED CONTENT **/");
	}

}

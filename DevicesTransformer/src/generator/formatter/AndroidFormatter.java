package generator.formatter;

import data.*;
import generator.DevicesGenerator;
import generator.LanguagesGenerator;
import parser.TypesParser;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Robert on 13. 7. 2015.
 */
public class AndroidFormatter implements DevicesGenerator.IDevicesFormatter, LanguagesGenerator.ILanguagesFormatter {

	private Features determineFeatures(Device device) {
		Features features = new Features();

		for (Module module : device.getModules()) {
			String type = module.getType();
			if (type.equalsIgnoreCase(TypesParser.TYPE_BATTERY)) {
				features.battery = true;
			} else if (type.equalsIgnoreCase(TypesParser.TYPE_RSSI)) {
				features.rssi = true;
			} else if (type.equalsIgnoreCase(TypesParser.TYPE_REFRESH)) {
				features.refresh = module.getDefaultValue();
			}
		}

		return features;
	}

	@Override
	public void formatDevices(PrintWriter writer, Devices devices, Types types) {
		writer.println("/** BEGIN OF GENERATED CONTENT **/");

		Iterator<Device> it = devices.getDevices().iterator();
		while (it.hasNext()) {
			Device device = it.next();

			Features features = determineFeatures(device);

			// Begin of type definition
			writer.println(String.format("TYPE_%d(\"%d\", \"%s\", %s, %s, new DeviceFeatures(%s, %s, %s, %s)) {",
					device.getTypeId(),
					device.getTypeId(),
					device.getTypeName(),
					device.getName().getResourceId(),
					device.getManufacturer().getResourceId(),
					features.refresh != null ? features.refresh : "null",
					features.led ? "true" : "false",
					features.battery ? "true" : "false",
					features.rssi ? "true" : "false"
			));

			// Begin of createModules() method
			writer.println("\t@Override\n\tpublic List<Module> createModules(Device device) {");

			// Begin of modules array
			writer.println("\t\treturn Arrays.asList(");

			List<Module> filteredModules = new ArrayList<>();
			for (Module module : device.getModules()) {
				// Ignore special modules (battery, rssi, refresh, led) to generate same output as before
				String type = module.getType();
				if (type.equalsIgnoreCase(TypesParser.TYPE_BATTERY)
						|| type.equalsIgnoreCase(TypesParser.TYPE_RSSI)
						|| type.equalsIgnoreCase(TypesParser.TYPE_REFRESH)) {
					continue;
				}

				filteredModules.add(module);
			}

			Iterator<Module> itModule = filteredModules.iterator();
			while (itModule.hasNext()) {
				Module module = itModule.next();

				Translation tgroup = module.getGroup();
				String group = tgroup != null ? tgroup.getResourceId() : "null";

				Translation tname = module.getName();
				String name = tname != null ? tname.getResourceId() : "null";

				writer.print(String.format("\t\t\t\tnew Module(device, \"%d\", %s, %s, %s, %s, %b",
								module.getId(),
								module.getType(),
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

		writer.println(String.format("\n/** Version from specification of this devices list */\npublic static final String DEVICES_VERSION = \"%s\";", devices.getVersion()));
		writer.println(String.format("\n/** Generation time (GMT) of this devices list */\npublic static final long DEVICES_DATE = %sl;", new Date().getTime()));

		writer.println("\n/** END OF GENERATED CONTENT **/");
	}

	@Override
	public void formatLanguages(PrintWriter writer, Language language) {
		writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		writer.println("<resources>");

		for (Language.Item item : language.getItems()) {
			String name = item.key;
			String value = item.value;

			// FIXME: Use better way than this hardcoded check
			// Ignore unwanted battery/rssi/refresh/led modules
			if (name.equalsIgnoreCase("devices__type_battery")
					|| name.equalsIgnoreCase("devices__type_rssi")
					|| name.equalsIgnoreCase("devices__type_refresh")
					|| name.equalsIgnoreCase("devices__type_led"))
				continue;

			writer.println(String.format("\t<string name=\"%s\">%s</string>", name, value));
		}

		writer.println("</resources>");
	}

	private class Features {
		public String refresh;
		public boolean battery;
		public boolean led;
		public boolean rssi;
	}
}

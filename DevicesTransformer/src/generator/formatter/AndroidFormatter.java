package generator.formatter;

import data.*;
import generator.DevicesGenerator;
import generator.LanguagesGenerator;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Robert on 13. 7. 2015.
 */
public class AndroidFormatter implements DevicesGenerator.IDevicesFormatter, LanguagesGenerator.ILanguagesFormatter {

	@Override
	public void formatDevices(PrintWriter writer, Devices devices, Types types) {
		writer.println("/** BEGIN OF GENERATED CONTENT **/");

		Iterator<Device> it = devices.getDevices().iterator();
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

			writer.println(String.format("\t<string name=\"%s\">%s</string>", name, value));
		}

		writer.println("</resources>");
	}
}

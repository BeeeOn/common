package generator.formatter;

import data.*;
import generator.DevicesGenerator;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Robert on 13. 7. 2015.
 */
public class GateFormatter implements DevicesGenerator.IDevicesFormatter {

	@Override
	public void formatDevices(PrintWriter writer, Devices devices, Types types) {

		writer.print("\n" +
				"#ifndef DEVICE_TABLE_H\n" +
				"#define DEVICE_TABLE_H\n" +
				"\n" +
				"#include \"utils.h\"\n" +
				"\n" +
				"using TT_Table = std::map<int, TT_Device>;\n" +
				"\n" +
				"/**\n" +
				" * @brief Generovany soubor s funkci pro vraceni nove tabulky typu, ktera je slozena z devices (multisenzory) a modules (fyzicke senzory na device).\n" +
				" * @return Vraci mapu devices, ktere obsahuji jejich moduly.\n" +
				" */" +
				"\ninline TT_Table fillDeviceTable() {\n" +
				"\t\n" +
				"\t//  list of defined TT_Devices and theirs modules\n" +
				"\tstd::map<int, TT_Device> devices;\n" +
				"\tstd::map<int, TT_Module> modules;\n" +
				"\t\n");

		Iterator<Device> it = devices.getDevices().iterator();
		while (it.hasNext()) {
			Device device = it.next();

			// List modules of this device
			Iterator<Module> itModule = device.getModules().iterator();
			while (itModule.hasNext()) {
				Module module = itModule.next();

				String minConstraint = "{false, 0.0}";
				String maxConstraint = "{false, 0.0}";
				String values = "{}";

				if (!module.getValues().isEmpty()) {
					values = "{";

					Iterator<Module.Value> itValue = module.getValues().iterator();
					while (itValue.hasNext()) {
						Module.Value value = itValue.next();
						values += String.valueOf(value.id);
						if (itValue.hasNext())
							values += ", ";
					}

					values += "}";
				} else if (module.getConstraints() != null) {
					Module.Constraints constraints = module.getConstraints();

					minConstraint = String.format("{true, %s}", constraints.getMin());
					maxConstraint = String.format("{true, %s}", constraints.getMax());
				}

                // Find type of this module
                Type type = null;
                for (Type t : types.getTypes()) {
                    if (t.getId().equals(module.getType())) {
                        type = t;
                        break;
                    }
                }

				writer.println(String.format("\tmodules.insert( {%d, TT_Module(%d, %s, %d, %b, \"%s\", \"%s\", %s, %s, %s) } );",
						module.getId(),
						module.getId(),
						module.getType(),
                        type != null ? type.getVarSize() : 0,
						module.isActuator(),
                        type != null ? type.getTransformFrom() : "",
                        type != null ? type.getTransformTo() : "",
						minConstraint,
						maxConstraint,
						values
				));
			}

			Device.Features features = device.getFeatures();

			writer.println(String.format("\tdevices.insert( {%d, TT_Device(%d, modules, %b, %b)} );\t// insert device to map of devices",
					device.getTypeId(),
					device.getTypeId(),
					features != null && features.hasBattery(),
					features != null && features.hasLed()
			));
			writer.println("\tmodules.clear();");
			writer.println("");
		}

		writer.println("\treturn devices;\n" +
				"}\n" +
				"\n" +
				"#endif /* DEVICE_TABLE_H */");
	}
}

package generator.formatter;

import data.*;
import generator.DevicesGenerator;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Robert on 13. 7. 2015.
 */
public class GateFormatter implements DevicesGenerator.IDevicesFormatter {

	public static final String TYPE_BATTERY_ID = "0x08";
	public static final String TYPE_RSSI_ID = "0x09";
	public static final String TYPE_REFRESH_ID = "0x0A";

	public static final Module.Constraints CONSTRAINTS_REFRESH;
	static {
		CONSTRAINTS_REFRESH = new Module.Constraints();
		CONSTRAINTS_REFRESH.setMin(5);
		CONSTRAINTS_REFRESH.setMax(3600);
		CONSTRAINTS_REFRESH.setGranularity(1);
	};

	private List<Module> getModulesIncludingFeatures(Device device) {
		List<Module> modules = new ArrayList<>();

		// Add all defined modules
		modules.addAll(device.getModules());

		// Then add special "features" modules
		Device.Features features = device.getFeatures();

		if (features.hasBattery()) {
			Module battery = new Module(features.getBatteryId(), TYPE_BATTERY_ID);
			modules.add(battery);
		}

		if (features.hasRefresh()) {
			Module refresh = new Module(features.getRefreshId(), TYPE_REFRESH_ID);
			refresh.setActuator(true);
			refresh.setConstraints(CONSTRAINTS_REFRESH);
			modules.add(refresh);
		}

		if (features.hasRssi()) {
			Module rssi = new Module(features.getRssiId(), TYPE_RSSI_ID);
			modules.add(rssi);
		}

		return modules;
	}

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
				"\t// list of defined TT_Devices and theirs modules\n" +
				"\tstd::map<int, TT_Device> devices;\n" +
				"\tstd::map<int, TT_Module> modules;\n" +
				"\t\n");

		Iterator<Device> it = devices.getDevices().iterator();
		while (it.hasNext()) {
			Device device = it.next();

			// List modules of this device
			Iterator<Module> itModule = getModulesIncludingFeatures(device).iterator();
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

			writer.println(String.format("\tdevices.insert( {%d, TT_Device(%d, modules, %b, %b)} ); // insert device to map of devices",
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

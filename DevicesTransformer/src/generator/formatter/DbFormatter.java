package generator.formatter;

import data.Device;
import data.Devices;
import data.Types;
import data.module.Module;
import data.module.Refresh;
import generator.DevicesGenerator;

import java.io.PrintWriter;

public class DbFormatter implements DevicesGenerator.IDevicesFormatter {

	@Override
	public void formatDevices(PrintWriter writer, Devices devices, Types types) {

		/*get_signal_module_id*/
		writer.print("CREATE OR REPLACE FUNCTION get_signal_module_id(device_type integer) RETURNS integer AS $$\n" +
						"\tBEGIN\n" +
						"\t\tCASE device_type\n"
		);

		for (Device device : devices.getDevices()) {
			// List modules of this device
			for (Module module : device.getModules()) {
				//if module is signal (rssi)
				if (module instanceof Refresh) {
					writer.println(String.format("\t\t\tWHEN %d THEN RETURN %d;",
							device.getTypeId(),
							module.getId()
					));
				}
			}
		}
		writer.print("\t\t\tELSE RETURN null;\n" +
						"\t\tEND CASE;\n" +
						"\t\tRETURN null;\n" +
						"\tEND;\n" +
						"$$ LANGUAGE plpgsql;"
		);

		writer.print("\n\n");

		/*new_device function*/

		writer.println("CREATE OR REPLACE FUNCTION insert_modules() returns trigger\n" +
				"AS\n" +
				"$$\n" +
				"\tBEGIN\n" +
				"\t\tCASE NEW.device_type\n"
		);

		for (Device device : devices.getDevices()) {

			writer.println(String.format("\t\t\tWHEN %d THEN\n",
					device.getTypeId()
			));
			// List modules of this device
			for (Module module : device.getModules()) {
				writer.println(String.format("\t\t\t\tINSERT INTO module(device_euid, module_id, measured_value, gateway_id)\n" +
							 "\t\t\t\tVALUES(NEW.device_euid, %d, 'NaN', NEW.gateway_id);\n",
						module.getId()
				));
			}
		}

		writer.print("\t\tEND CASE;\n" +
				"\tRETURN NEW;\n" +
				"END;\n" +
				"$$\n" +
				"LANGUAGE plpgsql;\n");
		writer.print("\n\n");

		/*new_device trigger*/

		writer.print("DROP TRIGGER IF EXISTS new_device_trigger ON device;");

		writer.print("\n\n");

		writer.print("CREATE TRIGGER new_device_trigger\n" +
				"AFTER INSERT ON device\n" +
				"FOR EACH ROW\n" +
				"EXECUTE PROCEDURE insert_modules();\n");
	}
}
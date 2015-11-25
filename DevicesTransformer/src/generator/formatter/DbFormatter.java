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
	}
}

  /*     EXAMPLEE:
CREATE OR REPLACE FUNCTION get_signal_module_id(device_type integer) RETURNS integer AS $$
        BEGIN
          CASE device_type
              WHEN 1 THEN
                  RETURN 1;
              WHEN 3 THEN
                  RETURN 1;
              ELSE
                  RETURN null;
          END CASE;
          
          RETURN null;
          
        END;
$$ LANGUAGE plpgsql;
*/
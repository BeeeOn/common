package generator.formatter;

import data.Device;
import data.Devices;
import data.Type;
import data.Types;
import data.module.Module;
import generator.DevicesGenerator;

import java.io.PrintWriter;
import java.util.Iterator;

public class GateFormatter implements DevicesGenerator.IDevicesFormatter {

	@Override
	public void formatDevices(PrintWriter writer, Devices devices, Types types) {

		writer.print("CREATE OR REPLACE FUNCTION get_signal_module_id(device_type integer) RETURNS integer AS $$\n" +
      "\tBEGIN" +
      "\t\tCASE device_type"
    );

		Iterator<Device> it = devices.getDevices().iterator();
		while (it.hasNext()) {
			Device device = it.next();

			// List modules of this device
			Iterator<Module> itModule = device.getModules().iterator();
			while (itModule.hasNext()) {
				Module module = itModule.next();

        //if module is signal (rssi)
        if(module.getType == "0x09")
  				writer.println(String.format("WHEN %d THEN\n" +
            "\tRETURN %d;",
  						device.getTypeId(),
  						module.getId(),
  				));
			}
			writer.println("");
		}
    writer.print("\t\t\tELSE" +
      "\t\t\t\tRETURN null;" +
      "\t\tEND CASE;" +
      "\t\tRETURN null;" +
      "\tEND;" +
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
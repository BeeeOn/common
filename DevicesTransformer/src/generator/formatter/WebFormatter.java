package generator.formatter;

import data.*;
import data.module.Module;
import generator.DevicesGenerator;
import generator.LanguagesGenerator;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Filip Šutovský <xsutov00@stud.fit.vutbr.cz> on 20. 9. 2015.
 */
public class WebFormatter implements DevicesGenerator.IDevicesFormatter, LanguagesGenerator.ILanguagesFormatter {

	@Override
	public void formatDevices(PrintWriter writer, Devices devices, Types types) {
		writer.println(String.format("var devicesTable = {"));

		Iterator<Device> it = devices.getDevices().iterator();
		while (it.hasNext()) {
			Device device = it.next();

			// Begin of type definition
			writer.println(String.format("\t\"%d\": {",device.getTypeId()));
			writer.println(String.format("\t\t\"id\": \"%d\",",device.getTypeId()));
			writer.println(String.format("\t\t\"typeName\": \"%s\",",device.getTypeName()));
			writer.println(String.format("\t\t\"name\": \"%s\",",device.getName().getWebappResourceId()));
			writer.println(String.format("\t\t\"manufacturer\": \"%s\",",device.getManufacturer().getWebappResourceId()));
			// Begin modules definition
			writer.println(String.format("\t\t\"modules\": ["));
			Iterator<Module> itModule = device.getModules().iterator();
			while (itModule.hasNext()) {
				writer.println(String.format("\t\t\t{"));

				Module module = itModule.next();
				writer.println(String.format("\t\t\t\t\"id\": \"%d\",",module.getId()));
				writer.println(String.format("\t\t\t\t\"type\": \"%s\",",module.getType()));
				if(module.getOrder() != null){
					writer.println(String.format("\t\t\t\t\"order\": \"%s\",",module.getOrder()));
				}
				if(module.getGroup() != null){
					writer.println(String.format("\t\t\t\t\"group\": \"%s\",",module.getGroup().getWebappResourceId()));
				}
				if(module.getName() != null){
					writer.println(String.format("\t\t\t\t\"name\": \"%s\",",module.getName().getWebappResourceId()));
				}
				if(!module.getRules().isEmpty() || !module.getValues().isEmpty() || module.getConstraints() != null || (module.getDefaultValue() != null && !module.getDefaultValue().isEmpty())){
					writer.println(String.format("\t\t\t\t\"isActuator\": %b,",module.isActuator()));
				}else {
					writer.println(String.format("\t\t\t\t\"isActuator\": %b",module.isActuator()));
				}

				if (!module.getRules().isEmpty()) {
					writer.println(String.format("\t\t\t\t\"rules\": ["));

					Iterator<Module.Rule> itRule = module.getRules().iterator();
					while (itRule.hasNext()) {
						Module.Rule rule = itRule.next();
						writer.println(String.format("\t\t\t\t\t{"));

						String ids = Arrays.toString(rule.hideModulesIds);
						ids = ids.substring(1, ids.length() - 1);

						writer.println(String.format("\t\t\t\t\t\t\"value\": \"%d\",",rule.value));
						writer.println(String.format("\t\t\t\t\t\t\"hiddenIds\": [%s]",ids));

						if(itRule.hasNext()){
							writer.println(String.format("\t\t\t\t\t},"));
						}else {
							writer.println(String.format("\t\t\t\t\t}"));
						}
					}
					if(!module.getValues().isEmpty() || module.getConstraints() != null || (module.getDefaultValue() != null && !module.getDefaultValue().isEmpty())){
						writer.println(String.format("\t\t\t\t],"));
					}else {
						writer.println(String.format("\t\t\t\t]"));
					}
				}

				if (!module.getValues().isEmpty()) {
					writer.println(String.format("\t\t\t\t\"values\": {"));
					Iterator<Module.Value> itValue = module.getValues().iterator();

					while (itValue.hasNext()) {
						Module.Value value = itValue.next();
						writer.println(String.format("\t\t\t\t\t\"%d\" : {",value.id));

						writer.println(String.format("\t\t\t\t\t\t\"id\": \"%d\",",value.id));
						writer.println(String.format("\t\t\t\t\t\t\"name\": \"%s\"",value.translation.getWebappResourceId()));

						if(itValue.hasNext()){
							writer.println(String.format("\t\t\t\t\t},"));
						}else {
							writer.println(String.format("\t\t\t\t\t}"));
						}
					}
					if(module.getConstraints() != null || (module.getDefaultValue() != null && !module.getDefaultValue().isEmpty())){
						writer.println(String.format("\t\t\t\t},"));
					}else {
						writer.println(String.format("\t\t\t\t}"));
					}
				}else if(module.getConstraints() != null){
					Module.Constraints constraints = module.getConstraints();
					writer.println(String.format("\t\t\t\t\"constraints\": {"));

					writer.println(String.format("\t\t\t\t\t\"min\" : \"%s\",",constraints.getMin()));
					writer.println(String.format("\t\t\t\t\t\"max\" : \"%s\",",constraints.getMax()));
					writer.println(String.format("\t\t\t\t\t\"granularity\" : \"%s\"",constraints.getGranularity()));
					if(module.getDefaultValue() != null && !module.getDefaultValue().isEmpty()){
						writer.println(String.format("\t\t\t\t},"));
					}else {
						writer.println(String.format("\t\t\t\t}"));
					}
				}

				String defaultValue = module.getDefaultValue();
				if (defaultValue != null && !defaultValue.isEmpty()) {
					writer.println(String.format("\t\t\t\t\"defaultValue\" : \"%s\"", module.getDefaultValue()));
				}

				if(itModule.hasNext()){
					writer.println(String.format("\t\t\t},"));
				}else {
					writer.println(String.format("\t\t\t}"));
				}
			}
			writer.println(String.format("\t\t]"));

			if(it.hasNext()){
				writer.println(String.format("\t},"));
			}else {
				writer.println(String.format("\t}"));
			}
		}

		writer.println(String.format("}"));
	}

	@Override
	public void formatLanguages(PrintWriter writer, Language language) {
		writer.println("{");
		writer.println(String.format("\t\"DEVICES\" : {"));

		for (Language.Item item : language.getItems()) {
			String name = item.key;
			String value = item.value;
			writer.println(String.format("\t\t\"%s\" : \"%s\",",name.toUpperCase(),value));
		}
		writer.println(String.format("\t}"));
		writer.println("}");
	}

}

package parser;

import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
import data.Device;
import data.Devices;
import data.Translation;
import data.module.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 23. 5. 2015.
 */
public class DevicesParser {

	/**
	 * @param path Path to XML file with devices definition.
	 * @return Devices object with parsed data, or null on failure
	 */
	public static Devices parseDevices(String path) {
		Devices devices = null;

		File file = new File(path);
		System.out.println(String.format("Loading devices specification from '%s'", file.getAbsolutePath()));

		try {
			devices = DevicesParser.parse(file);
		} catch (ParserConfigurationException | IOException | SAXException e) {
			e.printStackTrace();
		}

		return devices;
	}

	private static Devices parse(File file) throws ParserConfigurationException, IOException, SAXException {
		//Get the DOM Builder Factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);

		//Get the DOM Builder
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new DefaultErrorHandler());

		//Load and Parse the XML document
		//document contains the complete XML as a Tree.
		Document document = builder.parse(file);

		//Iterating through the nodes and extracting the data.
		Node devicesNode = document.getDocumentElement();
		if (!(devicesNode instanceof Element)) {
			return null;
		}

		String version = ((Element) devicesNode).getAttribute("version");

		Devices devices = new Devices(version);

		NodeList itemsNodes = devicesNode.getChildNodes();
		for (int i = 0; i < itemsNodes.getLength(); i++) {
			Node node = itemsNodes.item(i);

			if (node instanceof Element) {
				String tag = node.getNodeName();
				if (tag.equals("device")) {
					Device device = parseDevice((Element) node);
					devices.addDevice(device);
				} else {
					throw new IllegalStateException(String.format("Unexpected element '%s' (expected 'device')", tag));
				}
			}
		}

		return devices;
	}

	private static Device parseDevice(Element element) {
		int typeId = Integer.valueOf(element.getAttribute("id"));
		String typeName = element.getAttribute("name");

		Device device = new Device(typeId, typeName);

		NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node instanceof Element) {
				String tag = node.getNodeName();
				if (tag.equals("name")) {
					device.setName(new Translation(node.getTextContent()));
				} else if (tag.equals("manufacturer")) {
					device.setManufacturer(new Translation(node.getTextContent()));
				} else if (tag.equals("modules")) {
					List<Module> modules = parseModules((Element) node);
					device.setModules(modules);
				} else {
					throw new IllegalStateException(String.format("Unexpected element '%s' (expected 'name|manufacturer|modules')", tag));
				}
			}
		}

		return device;
	}

	public static int getIntAttribute(Element node, String attribute) {
		String value = node.getAttribute(attribute);
		return Integer.parseInt(value);
	}

	private static List<Module> parseModules(Element element) {
		List<Module> modules = new ArrayList<>();

		NodeList modulesNodes = element.getChildNodes();
		for (int i = 0; i < modulesNodes.getLength(); i++) {
			Node node = modulesNodes.item(i);

			if (node instanceof Element) {
				Element nodeEl = (Element) node;

				int id = getIntAttribute(nodeEl, "id");
				String type = nodeEl.getAttribute("type");

				String tag = node.getNodeName();
				if (tag.equals("sensor")) {
					Module module = parseModule(nodeEl, new Sensor(id, type));
					modules.add(module);
				} else if (tag.equals("actuator")) {
					Module module = parseModule(nodeEl, new Actuator(id, type));
					modules.add(module);
				} else if (tag.equals("refresh")) {
					Module module = parseModule(nodeEl, new Refresh(id, type));
					modules.add(module);
				} else if (tag.equals("battery")) {
					Module module = parseModule(nodeEl, new Battery(id, type));
					modules.add(module);
				} else if (tag.equals("rssi")) {
					Module module = parseModule(nodeEl, new Rssi(id, type));
					modules.add(module);
				} else if (tag.equals("led")) {
					Module module = parseModule(nodeEl, new Led(id, type));
					modules.add(module);
				} else {
					throw new IllegalStateException(String.format("Unexpected element '%s' (expected 'sensor|actuator|refresh|battery|rssi|led')", tag));
				}
			}
		}

		return modules;
	}

	/**
	 * Parse element data and properly fill given module object.
	 *
	 * @param element
	 * @param module
	 * @return same given module object.
	 */
	private static Module parseModule(Element element, Module module) {
		NodeList moduleNodes = element.getChildNodes();
		for (int i = 0; i < moduleNodes.getLength(); i++) {
			Node node = moduleNodes.item(i);

			if (node instanceof Element) {
				String tag = node.getNodeName();
				if (tag.equals("order")) {
					module.setOrder(Integer.parseInt(node.getTextContent()));
				} else if (tag.equals("group")) {
					module.setGroup(new Translation(node.getTextContent()));
				} else if (tag.equals("name")) {
					module.setName(new Translation(node.getTextContent()));
				} else if (tag.equals("default")) {
					module.setDefaultValue(node.getTextContent());
				} else if (tag.equals("constraints")) {
					Module.Constraints constraints = parseConstraints((Element) node);
					module.setConstraints(constraints);
				} else if (tag.equals("values")) {
					Translation name = new Translation(((Element) node).getAttribute("name"), false);
					List<Module.Value> values = parseValues((Element) node);
					module.setValues(name, values);
				} else if (tag.equals("rules")) {
					List<Module.Rule> rules = parseRules((Element) node);
					module.setRules(rules);
				} else {
					throw new IllegalStateException(String.format("Unexpected element '%s' (expected 'order|group|name|constraints|values|rules|default')", tag));
				}
			}
		}

		return module;
	}

	private static Module.Constraints parseConstraints(Element element) {
		Module.Constraints constraints = new Module.Constraints();

		NodeList constraintsNodes = element.getChildNodes();
		for (int i = 0; i < constraintsNodes.getLength(); i++) {
			Node node = constraintsNodes.item(i);

			if (node instanceof Element) {
				String tag = node.getNodeName();
				if (tag.equals("min")) {
					constraints.setMin(Double.parseDouble(node.getTextContent()));
				} else if (tag.equals("max")) {
					constraints.setMax(Double.parseDouble(node.getTextContent()));
				} else if (tag.equals("step")) {
					constraints.setGranularity(Double.parseDouble(node.getTextContent()));
				} else {
					throw new IllegalStateException(String.format("Unexpected element '%s' (expected 'min|max|step')", tag));
				}
			}
		}

		return constraints;
	}

	private static List<Module.Value> parseValues(Element element) {
		List<Module.Value> values = new ArrayList<>();

		NodeList valuesNodes = element.getChildNodes();
		for (int i = 0; i < valuesNodes.getLength(); i++) {
			Node node = valuesNodes.item(i);

			if (node instanceof Element) {
				String tag = node.getNodeName();
				if (tag.equals("value")) {
					int id = getIntAttribute((Element) node, "id");
					Translation translation = new Translation(node.getTextContent(), false);
					Module.Value value = new Module.Value(id, translation);
					values.add(value);
				} else {
					throw new IllegalStateException(String.format("Unexpected element '%s' (expected 'value')", tag));
				}
			}
		}

		return values;
	}

	private static List<Module.Rule> parseRules(Element element) {
		List<Module.Rule> rules = new ArrayList<>();

		NodeList rulesNodes = element.getChildNodes();
		for (int i = 0; i < rulesNodes.getLength(); i++) {
			Node node = rulesNodes.item(i);

			if (node instanceof Element) {
				String tag = node.getNodeName();
				if (tag.equals("if")) {
					int value = getIntAttribute((Element) node, "value");

					List<Integer> hideModulesIdsList = new ArrayList<>();
					NodeList hideModuleNodes = ((Element) node).getElementsByTagName("hide-module");
					for (int j = 0; j < hideModuleNodes.getLength(); j++) {
						Node hideModuleNode = hideModuleNodes.item(j);
						if (hideModuleNode instanceof Element) {
							Integer id = new Integer(((Element) hideModuleNode).getAttribute("id"));
							hideModulesIdsList.add(id);
						}
					}

					Module.Rule rule = new Module.Rule(value, hideModulesIdsList.toArray(new Integer[hideModulesIdsList.size()]));
					rules.add(rule);
				} else {
					throw new IllegalStateException(String.format("Unexpected element '%s' (expected 'if')", tag));
				}
			}
		}

		return rules;
	}

}

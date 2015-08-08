package parser;

import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
import data.Translation;
import data.Type;
import data.Types;
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

/**
 * Created by Robert on 23. 5. 2015.
 */
public class TypesParser {

	public static final String TYPE_BATTERY = "0x08";
	public static final String TYPE_RSSI = "0x09";
	public static final String TYPE_REFRESH = "0x0A";

	/**
	 * @param path Path to XML file with types definition.
	 * @return Types object with parsed data, or null on failure
	 */
	public static Types parseTypes(String path) {
		Types types = null;

		File file = new File(path);
		System.out.println(String.format("Loading types specification from '%s'", file.getAbsolutePath()));

		try {
			types = TypesParser.parse(file);
		} catch (ParserConfigurationException | IOException | SAXException e) {
			e.printStackTrace();
		}

		return types;
	}

	private static Types parse(File file) throws ParserConfigurationException, IOException, SAXException {
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
		Node typesNode = document.getDocumentElement();
		if (!(typesNode instanceof Element)) {
			return null;
		}

		String version = ((Element) typesNode).getAttribute("version");

		Types types = new Types(version);

		NodeList itemsNodes = typesNode.getChildNodes();
		for (int i = 0; i < itemsNodes.getLength(); i++) {
			Node node = itemsNodes.item(i);

			if (node instanceof Element) {
				String tag = node.getNodeName();
				if (tag.equals("type")) {
					Type type = parseType((Element) node);
					types.addType(type);
				} else {
					throw new IllegalStateException(String.format("Unexpected element '%s' (expected 'type')", tag));
				}
			}
		}

		return types;
	}

	private static Type parseType(Element element) {
		String id = element.getAttribute("id");

		Type type = new Type(id);

		NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node instanceof Element) {
				String tag = node.getNodeName();
				if (tag.equals("name")) {
					type.setName(new Translation(node.getTextContent()));
				} else if (tag.equals("var")) {
                    type.setVarSize(DevicesParser.getIntAttribute((Element) node, "size"));
					type.setVar(node.getTextContent());
				} else if (tag.equals("unit")) {
	                type.setUnit(node.getTextContent());
				} else if (tag.equals("transform")) {

                    NodeList transformNodes = node.getChildNodes();
                    for (int j = 0; j < transformNodes.getLength(); j++) {
                        Node transformNode = transformNodes.item(j);
                        if (transformNode instanceof Element) {
                            String transformTag = transformNode.getNodeName();
                            if (transformTag.equals("from")) {
                                type.setTransformFrom(transformNode.getTextContent());
                            } else if (transformTag.equals("to")) {
                                type.setTransformTo(transformNode.getTextContent());
                            } else {
                                throw new IllegalStateException(String.format("Unexpected element '%s' (expected 'from|to')", transformTag));
                            }
                        }
                    }

				} else {
					throw new IllegalStateException(String.format("Unexpected element '%s' (expected 'name|var|unit|transform')", tag));
				}
			}
		}

		return type;
	}

}

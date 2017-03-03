package application;
/**
 * First step - processing fodg xml file with graph into ArrayLists
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FodgGraphParse {
	private static final String NS_DC = "http://purl.org/dc/elements/1.1/";
	private static final String NS_OFFICE = "urn:oasis:names:tc:opendocument:xmlns:office:1.0";
	private static final String NS_DRAW = "urn:oasis:names:tc:opendocument:xmlns:drawing:1.0";
	private static final String NS_STYLE = "urn:oasis:names:tc:opendocument:xmlns:style:1.0";
	private static final String NS_META = "urn:oasis:names:tc:opendocument:xmlns:meta:1.0";
	private static final String NS_SVG = "urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0";
	private static final String XPATH_SHAPES = "//office:document/office:body/office:drawing/draw:page/draw:custom-shape";
	private static final String XPATH_CONNECTORS = "//office:document/office:body/office:drawing/draw:page/draw:connector";
	private static final String XPATH_ASTYLES = "//office:document/office:automatic-styles/style:style[@style:family='graphic']";
	
	private Map<String, Byte> arrowGroups;
	//private Map<Integer, KpnPoint> kpnPoints;
	private Map<String,KpnPoint> kpnPoints;
	private List<KpnPoint> kpnPointsList;
	private List<KpnRay> kpnRays;
	private KpnGraph kpnGraph;

	public KpnGraph getGraph() {
		return kpnGraph;
	}

	public FodgGraphParse(File fodgGraphFile) throws Exception {
		//kpnPoints = new TreeMap<>();		
		kpnPoints = new TreeMap<>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				int thisId = Integer.parseInt(o1.replaceAll("id", ""));
				int oId = Integer.parseInt(o2.replaceAll("id", ""));
				return thisId - oId;
			}
		});
		
		kpnRays = new LinkedList<KpnRay>();		
		arrowGroups = new HashMap<>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(fodgGraphFile);
		//System.out.println(fodgGraphFile.getAbsolutePath());
		//System.out.println(fodgGraphFile.length());
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new NamespaceContext() {
			@Override
			public String getNamespaceURI(String prefix) {
				if ("dc".equals(prefix))
					return FodgGraphParse.NS_DC;
				else if ("office".equals(prefix))
					return FodgGraphParse.NS_OFFICE;
				else if ("draw".equals(prefix))
					return FodgGraphParse.NS_DRAW;
				else if ("style".equals(prefix))
					return FodgGraphParse.NS_STYLE;
				else if ("meta".equals(prefix))
					return FodgGraphParse.NS_META;
				else if ("svg".equals(prefix))
					return FodgGraphParse.NS_SVG;
				else
					return null;
			}

			@Override
			public Iterator<?> getPrefixes(String val) {
				return null;
			}

			@Override
			public String getPrefix(String uri) {
				return null;
			}
		});

		XPathExpression exprCShapes = xpath.compile(XPATH_SHAPES);
		XPathExpression exprCConnectors = xpath.compile(XPATH_CONNECTORS);
		XPathExpression exprAStyles = xpath.compile(XPATH_ASTYLES);
		NodeList shapes = (NodeList) exprCShapes.evaluate(document, XPathConstants.NODESET);
		NodeList connectors = (NodeList) exprCConnectors.evaluate(document, XPathConstants.NODESET);
		NodeList astyles = (NodeList) exprAStyles.evaluate(document, XPathConstants.NODESET);

		Node currentItem;

		for (int i = 0; i < shapes.getLength(); i++) {
			currentItem = shapes.item(i);
			Node id = currentItem.getAttributes().getNamedItemNS(NS_DRAW, "id");
			Node descName = currentItem.getAttributes().getNamedItemNS(NS_DRAW, "name");
			if (id != null) {
				//kpnPoints.put(Integer.parseInt(id.getNodeValue().replaceAll("id", "")),
				//		new KpnPoint(id.getNodeValue(), (currentItem.getTextContent()).trim()));
				kpnPoints.put(id.getNodeValue(),new KpnPoint((currentItem.getTextContent()).trim(),(descName!=null ? descName.getNodeValue() : null)));
			} else {
				System.out.println("ERROR SHAPE AT {X: "
						+ currentItem.getAttributes().getNamedItemNS(NS_SVG, "x").getNodeValue() + "; Y: "
						+ currentItem.getAttributes().getNamedItemNS(NS_SVG, "y").getNodeValue() + "}");
			}
		}

		//debug
		kpnPointsList = new ArrayList<KpnPoint>();
		
		for(Map.Entry<String, KpnPoint> it : kpnPoints.entrySet())
		{
			//kpnPointsList.get(Integer.parseInt(it.getKey(),""));
			//System.out.println(it.getKey() + ": " + it.getValue().getOooName());
			it.getValue().setId(Integer.parseInt(it.getKey().replaceAll("id", "")));
			kpnPointsList.add(it.getValue());			
			
		}
		for(int i = 0; i < kpnPointsList.size(); i++)
		{
			System.out.println("id: " + i + "; pointId: " + kpnPointsList.get(i).getId());
		}
		
		//--debug
		for (int i = 0; i < astyles.getLength(); i++) {
			currentItem = astyles.item(i);

			Attr styleName = (Attr) currentItem.getAttributes().getNamedItemNS(NS_STYLE, "name");

			String grExpr = "./style:graphic-properties";
			Node grProps = (Node) xpath.evaluate(grExpr, currentItem, XPathConstants.NODE);
			if (grProps != null) {

				Attr startMarker = (Attr) grProps.getAttributes().getNamedItemNS(NS_DRAW, "marker-start");
				Attr endMarker = (Attr) grProps.getAttributes().getNamedItemNS(NS_DRAW, "marker-end");
				if (startMarker != null && endMarker != null) {
					arrowGroups.put(styleName.getValue(), (byte) 1); // Двунаправленная
																		// стрелка
																		// старт<->конец
				} else if (startMarker == null && endMarker != null) {
					arrowGroups.put(styleName.getValue(), (byte) 2); // Однонаправленная
																		// стрелка
																		// старт->конец
				} else if (startMarker != null && endMarker == null) {
					arrowGroups.put(styleName.getValue(), (byte) 3); // Однонаправленная
																		// стрелка
																		// старт<-конец
				}
			}			
		}

		for (int i = 0; i < connectors.getLength(); i++) {
			currentItem = connectors.item(i);
			Attr styleName = (Attr) currentItem.getAttributes().getNamedItemNS(NS_DRAW, "style-name");

			String start = currentItem.getAttributes().getNamedItemNS(NS_DRAW, "start-shape").getNodeValue();
			String end = currentItem.getAttributes().getNamedItemNS(NS_DRAW, "end-shape").getNodeValue();
			switch (arrowGroups.get(styleName.getValue())) {
			case 1:
				kpnRays.add(new KpnRay(kpnPoints.get(start),kpnPoints.get(end)));
				kpnRays.add(new KpnRay(kpnPoints.get(end),kpnPoints.get(start)));
				break;
			case 2:
				kpnRays.add(new KpnRay(kpnPoints.get(start),kpnPoints.get(end)));
				break;
			case 3:
				kpnRays.add(new KpnRay(kpnPoints.get(end),kpnPoints.get(start)));
				break;
			}
		}

		for (int i = 0; i < kpnRays.size(); i++) {
			System.out
					.println(kpnRays.get(i).getStartId().getOooName() + " - " + kpnRays.get(i).getEndId().getOooName());
		}
		kpnGraph = new KpnGraph(kpnPointsList, kpnRays);
	}
}

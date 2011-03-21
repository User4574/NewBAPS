package uk.org.ury.backend.config;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import uk.org.ury.backend.database.exceptions.MissingCredentialsException;

/**
 * Reads in an XML config file and creates config objects
 * 
 * @author Nathan Lasseter
 */
public class ConfigReader {

	private Database database = null;
	private Auth roAuth = null;
	private Auth rwAuth = null;
	
	/**
	 * Get the database configuration
	 * 
	 * @return Database database
	 */
	public Database getDatabase() { return database; }
	/**
	 * Get the read only login auth configuration
	 * 
	 * @return Auth roAauth
	 */
	public Auth getRoAuth() { return roAuth; }
	/**
	 * Get the read write login auth configuration
	 * 
	 * @return Auth rwAauth
	 */
	public Auth getRwAuth() { return rwAuth; }
	
	/**
	 * Read in the config file and create the Database and Auth configuration objects.
	 * Specify a config file.
	 * @throws MissingCredentialsException 
	 */
	public ConfigReader(String configFile) throws MissingCredentialsException {
		
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (configFile);
			doc.getDocumentElement().normalize();
						
			NodeList nList = doc.getElementsByTagName("auth");
			for(int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String user = getTagValue("user", eElement);
					String pass = getTagValue("pass", eElement);
					String sType = getTagValue("type", eElement);
					if(sType.equalsIgnoreCase("read_only")) {
						if(roAuth != null) continue;
						roAuth = new Auth(user, pass);
					} else if(sType.equalsIgnoreCase("read_write")) {
						if(rwAuth != null) continue;
						rwAuth = new Auth(user, pass);
					} else {
						throw new IllegalArgumentException("Unused user class.");
					}
				}
			}
			
			nList = doc.getElementsByTagName("database");
			for(int i = 0; i < nList.getLength(); i++) {
				if(database != null) break;
				Node nNode = nList.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String host = getTagValue("host", eElement);
					String port = getTagValue("port", eElement);
					String db = getTagValue("db", eElement);
					database = new Database(host, Integer.parseInt(port.trim()), db);
				}
			}
		}
		
		catch(NullPointerException n) {
			throw new MissingCredentialsException("An element node is empty.");
		}
		
		catch (SAXParseException err) {
			System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
	        System.out.println(" " + err.getMessage ());
		}
		catch (SAXException e) {
	        Exception x = e.getException ();
	        ((x == null) ? e : x).printStackTrace ();
		}
		catch (Throwable t) {
	        t.printStackTrace ();
		}
	}
	
	private static String getTagValue(String sTag, Element eElement){
		NodeList nlList= eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		
		return nValue.getNodeValue();
	}
}

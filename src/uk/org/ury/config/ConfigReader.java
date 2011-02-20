package uk.org.ury.config;

import java.io.File;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import uk.org.ury.database.UserClass;

public class ConfigReader {

	private Database database;
	private Auth auth;
	
	public Database getDatabase() { return database; }
	public Auth getAuth() { return auth; }
	
	public ConfigReader() {
		
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new File("res/conf.xml"));
			doc.getDocumentElement().normalize();
			
			System.out.println(doc.getDocumentElement().getNodeName());

			String user = doc.getElementsByTagName("user").item(0).getTextContent();
			String pass = doc.getElementsByTagName("pass").item(0).getTextContent();
			UserClass type;
			if(doc.getElementsByTagName("type").item(0).getTextContent().toLowerCase().equals("read_only")) {
				type = UserClass.READ_ONLY;
			} else if(doc.getElementsByTagName("type").item(0).getTextContent().toLowerCase().equals("read_write")) {
				type = UserClass.READ_WRITE;
			} else {
				throw new IllegalArgumentException("Unused user class.");
			}
			auth = new Auth(user, pass, type);
			
			String host = doc.getElementsByTagName("host").item(0).getTextContent();
			int port = Integer.parseInt(doc.getElementsByTagName("port").item(0).getTextContent().trim());
			String db = doc.getElementsByTagName("db").item(0).getTextContent();
			database = new Database(host, port, db);
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
}

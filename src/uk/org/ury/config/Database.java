package uk.org.ury.config;

/**
 * A Database Server configuration
 * 
 * @author Nathan Lasseter
 */
public class Database {

	private String host;
	private int port;
	private String db;
	
	/**
	 * Get the hostname of the database server
	 * 
	 * @return String hostname
	 */
	public String getHost() { return host; }
	/**
	 * Get the port the database server is running on
	 * 
	 * @return int port
	 */
	public int getPort() { return port; }
	/**
	 * Get the name of the database
	 *  
	 * @return String database name
	 */
	public String getDb() { return db; }
	
	/**
	 * Create a database object
	 * @param host The hostname of the database server
	 * @param port The port that the database server listens on
	 * @param db The name of the database on the server 
	 */
	public Database(String host, int port, String db) {
		this.host = host;
		this.port = port;
		this.db = db;
	}
	
}
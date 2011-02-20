package uk.org.ury.config;

public class Database {

	private String host;
	private int port;
	private String db;
	
	public String getHost() { return host; }
	public int getPort() { return port; }
	public String getDb() { return db; }
	
	public Database(String host, int port, String db) {
		this.host = host;
		this.port = port;
		this.db = db;
	}
	
}
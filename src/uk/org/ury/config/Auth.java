package uk.org.ury.config;

/**
 * A login authorisation configuration
 * 
 * @author Nathan Lasseter
 */
public class Auth {
	
	private String user;
	private String pass;
	
	/**
	 * Get the username of the login
	 * 
	 * @return String username
	 */
	public String getUser() { return user; }
	/**
	 * Get the password of the login
	 * 
	 * @return String password
	 */
	public String getPass() { return pass; }
	
	/**
	 * Create a login auth object
	 * @param user The username for the login
	 * @param pass The password for the login
	 */
	public Auth(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}
	
}
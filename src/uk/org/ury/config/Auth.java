package uk.org.ury.config;

import uk.org.ury.database.UserClass;

/**
 * A login authorisation configuration
 * 
 * @author Nathan
 */
public class Auth {
	
	private String user;
	private String pass;
	private UserClass type;
	
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
	 * Get the access level of the login
	 * 
	 * @return UserClass type
	 */
	public UserClass getType() { return type; }
	
	/**
	 * Create a login auth object
	 * @param user The username for the login
	 * @param pass The password for the login
	 * @param type The access type for the login
	 */
	public Auth(String user, String pass, UserClass type) {
		this.user = user;
		this.pass = pass;
		this.type = type;
	}
	
}
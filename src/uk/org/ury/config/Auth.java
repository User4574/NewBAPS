package uk.org.ury.config;

import uk.org.ury.database.UserClass;

public class Auth {
	
	private String user;
	private String pass;
	private UserClass type;
	
	public String getUser() { return user; }
	public String getPass() { return pass; }
	public UserClass getType() { return type; }
	
	public Auth(String user, String pass, UserClass type) {
		this.user = user;
		this.pass = pass;
		this.type = type;
	}
	
}
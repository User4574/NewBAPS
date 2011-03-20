/*
 * Server.java
 * -----------
 * 
 * Part of the URY Server Platform
 * 
 * V0.00  2011/03/20
 * 
 * (C) 2011 URY Computing
 */

package uk.org.ury.server;

import java.io.IOException;

import uk.org.ury.config.ConfigReader;
import uk.org.ury.database.DatabaseDriver;
import uk.org.ury.database.UserClass;
import uk.org.ury.database.exceptions.ConnectionFailureException;
import uk.org.ury.database.exceptions.MissingCredentialsException;

/**
 * The unified URY server, accepting requests over HTTP.
 * 
 * @author Matt Windsor
 */
public class Server {
    public static final String SERVER_VERSION = "SLUT 0.0";
    public static final String DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\""
	    + "\"http://www.w3.org/TR/html4/strict.dtd\">";
    public static final String INDEX_HTML = "\n<html>" + "\n  <head>"
	    + "\n    <title>" + SERVER_VERSION + "</title>" + "\n  </head>"
	    + "\n  <body>" + "\n    <h1>Welcome to the " + SERVER_VERSION
	    + " server</h1>"
	    + "\n    <p>This server exposes a class-based API for accessing"
	    + "\n    the internals of the " + SERVER_VERSION + " system.</p>"
	    + "\n    <p>See the documentation for details.</p>" + "\n  </body>"
	    + "\n</html>";

    /**
     * The main method, which serves to create a server.
     * 
     * @param args
     *            The argument vector.
     */
    public static void main(String[] args) {
	Server srv = new Server();
	srv.run();
    }

    /**
     * Run the server.
     */
    private void run() {
	Thread thread = null;

	try {
	    thread = new HttpListenerThread(8000, this);
	} catch (IOException e) {
	    e.printStackTrace();
	    System.exit(-1);
	}

	thread.setDaemon(false);
	thread.run();
    }

    /**
     * Get a database connection using the given user class.
     * 
     * @param userClass
     *            The user class to get a connection for.
     * 
     * @return a database connection, which may or may not have been created on
     *         this call.
     * 
     * @throws MissingCredentialsException
     *             if the credentials for the given userclass are missing.
     * 
     * @throws ConnectionFailureException
     *             if the connection failed.
     */
    public DatabaseDriver getDatabaseConnection(UserClass userClass)
	    throws MissingCredentialsException, ConnectionFailureException {
	// TODO: Singleton

	ConfigReader config = new ConfigReader("res/conf.xml");

	return new DatabaseDriver(config, UserClass.READ_ONLY);
    }

    /**
     * @return the version string of the server.
     */
    public String getVersion() {
	return SERVER_VERSION;
    }
}

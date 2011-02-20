package uk.org.ury.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uk.org.ury.config.ConfigReader;
import uk.org.ury.database.exceptions.ConnectionFailureException;
import uk.org.ury.database.exceptions.MissingCredentialsException;


/**
 * A database connection manager that connects to the URY databases 
 * using suitably privileged accounts, and handles the processing 
 * of SQL queries.
 *
 * @author Matt Windsor
 * @author Nathan Lasseter
 *
 */

public class DatabaseDriver
{ 
  /* The JDBC path used to connect to the URY database. */
  private String DATABASE_PATH = "jdbc:postgresql://";
  
  /* The database connection. */
  private Connection conn;
  
  
  /**
   * Construct a new DatabaseDriver with the given user class.
   * 
   * @param config	   The config with login details.
   * @param userclass  The user class to log in to the database with.
   * 
   * @throws           IllegalArgumentException if the user class is 
   *                   not supported (this should not happen).
   *                   
   * @throws           MissingCredentialsException if the user class 
   *                   login credentials could not be loaded.
   *                   
   * @throws           ConnectionFailureException if the database 
   *                   backend failed to connect to the database 
   *                   server.
   */
  
  public
  DatabaseDriver (ConfigReader config, UserClass type) throws MissingCredentialsException, ConnectionFailureException
  { 
    try
      {
        connect (config, type);
      }
    catch (SQLException e)
      {
        throw new ConnectionFailureException (e.getMessage ());
      }

  }
  
  
  /**
   * Connect to the URY database.
   * 
   * @param config  The config to use for the connection.
   * @param type	  The access level of the connection   
   * 
   * @throws        SQLException if the database connection failed.
   */
  
  private void
  connect (ConfigReader config, UserClass type) throws SQLException
  {
    if (config == null)
      throw new IllegalArgumentException ("Supplied null config.");
    
    if (config.getDatabase().getHost() == null)
      throw new IllegalArgumentException ("config has no associated host.");

    if (config.getDatabase().getDb() == null)
      throw new IllegalArgumentException ("config has no associated database.");
    
    DATABASE_PATH = DATABASE_PATH + config.getDatabase().getHost() + "/" + config.getDatabase().getDb();
    
    if(type == UserClass.READ_ONLY) {
    	if(config.getRoAuth().getUser() == null)
    		throw new IllegalArgumentException ("config has no associated username.");
    	if(config.getRoAuth().getPass() == null)
  	    	throw new IllegalArgumentException ("config has no associated password.");
    	conn = DriverManager.getConnection (DATABASE_PATH,
    										config.getRoAuth().getUser(),
    										config.getRoAuth().getPass());
    } else if(type == UserClass.READ_WRITE) {
    	if(config.getRwAuth().getUser() == null)
    		throw new IllegalArgumentException ("config has no associated username.");
    	if(config.getRwAuth().getPass() == null)
    		throw new IllegalArgumentException ("config has no associated password.");
    	conn = DriverManager.getConnection (DATABASE_PATH,
  											config.getRwAuth().getUser(),
  											config.getRwAuth().getPass());
    }
  }
  
  
  /**
   * Execute an unprepared SQL statement with no arguments.
   * 
   * @param sql        The SQL statement to execute.
   * @param fetchSize  The maximum number of query rows to return.
   * 
   * @return           the JDBC results set.
   */
  
  public ResultSet
  executeQuery (String sql, int fetchSize)
  {
    try 
      {
        Statement st = conn.createStatement ();
        st.setFetchSize (fetchSize);
        
        return st.executeQuery (sql);
      }
    catch (SQLException e)
      {
        e.printStackTrace ();
        return null;
      }
  }

  
  /**
   * Perform a SQL statement with arguments.
   * 
   * This accepts an array of parameter objects, which must each 
   * either be String or Integer objects.  The objects will be used 
   * sequentially to fill in '?' placeholders in the query text.
   * 
   * @param sql        The SQL statement to execute.
   * @param params     A list of parameter objects.
   * @param fetchSize  The maximum number of query rows to return.
   *                
   * @return           the set of results from the query.
   * 
   * @throws           IllegalArgumentException if any of the
   *                   parameters is unsupported by the database as 
   *                   a statement parameter.
   *                   
   * @throws           SQLException if a SQL error occurs.
   */

  public ResultSet
  executeQuery (String sql, Object[] params, int fetchSize) throws SQLException
  {
    PreparedStatement st = conn.prepareStatement (sql);
    
    st.setFetchSize (50);
    
    for (int i = 0; i < params.length; i++)
      if (params[i] instanceof String)
        st.setString (i + 1, (String) params[i]);
      else if (params[i] instanceof Integer)
        st.setInt (i + 1, (Integer) params[i]);
      else
        throw new IllegalArgumentException ("Unsupported parameter #" + (i + 1));
    
    return st.executeQuery ();
  }
}

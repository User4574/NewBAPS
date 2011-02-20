package uk.org.ury.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uk.org.ury.database.exceptions.ConnectionFailureException;
import uk.org.ury.database.exceptions.MissingCredentialsException;


/**
 * A database connection manager that connects to the URY databases 
 * using suitably privileged accounts, and handles the processing 
 * of SQL queries.
 *
 * @author Matt Windsor
 *
 */

public class DatabaseDriver
{ 
  /* The JDBC path used to connect to the URY database. */
  private String DATABASE_PATH = "jdbc:postgresql://4574.co.uk/membership";
  
  /* The database connection. */
  private Connection conn;
  
  
  /**
   * Construct a new DatabaseDriver with the given user class.
   * 
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
  DatabaseDriver (UserClass userclass) 
    throws MissingCredentialsException, ConnectionFailureException
  { 
    DatabaseLogin login = null;
    
    login = DatabaseLogin.getLoginFromFile (userclass.configName + ".txt");
    
    try
      {
        connect (login);
      }
    catch (SQLException e)
      {
        throw new ConnectionFailureException (e.getMessage ());
      }

  }
  
  
  /**
   * Connect to the URY database.
   * 
   * @param login  The login tuple to use for the connection.   
   * 
   * @throws       SQLException if the database connection failed.
   */
  
  private void
  connect (DatabaseLogin login) throws SQLException
  {
    if (login == null)
      throw new IllegalArgumentException ("Supplied null login.");
    
    if (login.getUsername () == null)
      throw new IllegalArgumentException ("Login has no associated username.");
    
    if (login.getPassword () == null)
      throw new IllegalArgumentException ("Login has no associated password.");
    
    
    conn = DriverManager.getConnection (DATABASE_PATH,
                                        login.getUsername (),
                                        login.getPassword ());
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

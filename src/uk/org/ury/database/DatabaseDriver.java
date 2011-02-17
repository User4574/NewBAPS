package uk.org.ury.database;

import java.sql.Connection;
import java.sql.DriverManager;
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
  private String DATABASE_PATH = "jdbc:postgresql://localhost/membership";
  
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
  DatabaseDriver (UserClass userclass) throws MissingCredentialsException, ConnectionFailureException
  { 
    DatabaseLogin login = null;
    
    switch (userclass)
    {
    case READ_ONLY:
      login = DatabaseLogin.getLoginFromFile ("read_only.txt");
      break;
    case READ_WRITE:
      login = DatabaseLogin.getLoginFromFile ("read_write.txt");
    default:
      throw new IllegalArgumentException ("Unused user class.");
    }


    try
      {
        System.out.println ("Trying to acquire connection...");
        
        connect (login);
        
        System.out.println ("...connection succeeded.");
      }
    catch (SQLException e)
      {
        throw new ConnectionFailureException (e.getMessage ());
      }

  }
  
  
  public void
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
   * Execute a SQL statement.
   * 
   * @param sql  The SQL statement to execute.
   * 
   * @return     the JDBC results set.
   */
  
  public ResultSet
  executeQuery (String sql)
  {
    try 
      {
        Statement st = conn.createStatement ();
        st.setFetchSize (50);
        
        return st.executeQuery (sql);
      }
    catch (SQLException e)
      {
        e.printStackTrace ();
        return null;
      }
  }
}

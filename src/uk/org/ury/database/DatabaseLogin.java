/**
 * 
 */
package uk.org.ury.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import uk.org.ury.database.exceptions.MissingCredentialsException;


/**
 * A login username/password pair.
 * 
 * @author  Matt Windsor
 */

public class DatabaseLogin
{
  private String username;
  private String password;
  
  
  /**
   * Create a new DatabaseLogin.
   * 
   * This constructor is intentionally left private to prevent 
   * coders from hard-coding passwords.  Please read database 
   * credentials from a source external to the program code.
   * 
   * @param username  The database username.
   * @param password  The database password.
   * 
   * @see             getLoginFromFile
   * @see             getLoginFromConfig
   */
  
  private
  DatabaseLogin (String username, String password)
  {
    this.username = username;
    this.password = password;
  }
  
  
  /**
   * Retrieve login credentials from a plaintext file.
   * 
   * The credentials should be listed in the following format:
   * 
   * username <newline>
   * password
   * 
   * @param file  The filename of the file
   * 
   * @return      a new DatabaseLogin containing the information 
   *              retrieved from the file.
   * 
   * @throws      IllegalArgumentException if the filename is null.
   * 
   * @throws      MissingCredentialsException if the file does not exist 
   *              or is of the wrong format.
   */
  
  public static DatabaseLogin
  getLoginFromFile (String file) throws MissingCredentialsException
  {
    // Find the credentials file.
    
    if (file == null)
      throw new IllegalArgumentException ("Supplied null credentials filename.");

    URL fileURL = DatabaseLogin.class.getResource ("credentials/" + file);
    
    if (fileURL == null)
      throw new MissingCredentialsException ("Credentials file "
                                             + file
                                             + " not found.");
    
    
    String username = null;
    String password = null;
    BufferedReader reader = null;

    
    try
      {
        reader = new BufferedReader (new FileReader (fileURL.getFile ()));
      }
    catch (FileNotFoundException e)
      {
        throw new MissingCredentialsException ("Credentials file "
                                               + file 
                                               + " not found.");
      }
    
    try
      {
        username = reader.readLine ();
        password = reader.readLine ();
      }
    catch (IOException e)
      {
        throw new MissingCredentialsException ("Credentials file "
                                               + file
                                               + "is invalid.");
      }
    finally
      {
        try
          {
            reader.close ();
          }
        catch (IOException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace ();
          }
      }

    
    DatabaseLogin login = new DatabaseLogin (username, password);
    
    return login;
  }

  
  /**
   * Retrieve login credentials from the configuration file.
   * 
   * @param configName  The name of the tag in the configuration 
   *                    file containing the credentials.
   *                    
   * @return            a new DatabaseLogin containing the information 
   *                    retrieved from the file.
   * 
   * @throws            IllegalArgumentException if the filename is 
   *                    null.
   * 
   * @throws            MissingCredentialsException if the tag or 
   *                    configuration file does not exist.
   */
  
  public static DatabaseLogin
  getLoginFromConfig (String configName)
  {
    // TODO Auto-generated method stub
    return null;
  }
  

  /**
   * @return  the username.
   */
  
  public String
  getUsername ()
  {
    return username;
  }


  /**
   * @return  the password.
   */
  
  public String
  getPassword ()
  {
    return password;
  }
}

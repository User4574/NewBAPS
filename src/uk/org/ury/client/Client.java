package uk.org.ury.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Client
{
  /**
   * Get a raw response from the server.
   * 
   * @param file  The "file", including path and query string, to 
   *              fetch from the server.
   * 
   * @return      The response from the server, as a List of lines. 
   */
  
  public List<String>
  get (String file)
  {
    URL url = null;
    URLConnection uc = null;
    List<String> result = new ArrayList<String> ();
    
    try
      {
        url = new URL ("http://localhost:8000" + file);
      }
    catch (MalformedURLException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    
    try
      {
        uc = url.openConnection ();
        
        BufferedReader in = new BufferedReader (new InputStreamReader 
                                               (uc.getInputStream ()));
        
        
        String inputLine;

        for (inputLine = in.readLine();
             inputLine != null;
             inputLine = in.readLine())
          {
            result.add (inputLine);
          }
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    
    return result;
  }
}

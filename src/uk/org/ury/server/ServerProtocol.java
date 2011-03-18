package uk.org.ury.server;

import java.util.ArrayList;
import java.util.List;

/**
 * The BAPS server protocol (a minimal implementation of HTTP 1.1) handler.
 * 
 * @author  Matt Windsor
 *
 */

public class ServerProtocol
{
  public static final String GET_HEADER = "HTTP/1.1 200 OK\n";

  public List<String> buffer;
  
  
  public 
  ServerProtocol ()
  {
    buffer = new ArrayList<String> ();
  }
  
  public String
  processInput (String string)
  {
    if (string.equals (""))
      {
        System.out.println ("Bingo!");
        return "HTTP/1.1 200 OK\nConnection: Close\n\r\n<html><head></head><body>poo</body></html>\n\r\n";
      }
    return "";
  }

}

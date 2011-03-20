/**
 * 
 */
package uk.org.ury.protocol;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import uk.org.ury.protocol.exceptions.DecodeFailureException;
import uk.org.ury.protocol.exceptions.InvalidMessageException;


/**
 * Utilities for converting between strings encoded in the response 
 * protocol and collections of items, as well as validating and 
 * unpicking protocol messages.
 * 
 * @author  Matt Windsor
 *
 */

public class ProtocolUtils
{
  /**
   * Encode a key-value map into a protocol string.
   * 
   * The map can contain strings, lists and other maps.  Other 
   * types may be accepted by the underlying encoding engine, 
   * but the above types are the only ones explicitly accepted.
   * 
   * @param items  The key-value map of items, which should contain 
   *               strings, lists and maps.  The keys of any map 
   *               should be protocol directives.
   *               
   * @return       A string containing the encoded representation of 
   *               the map.
   */
  
  public static String
  encode (Map<String, Object> items)
  {
    return JSONValue.toJSONString (items);
  }
  
  
  /**
   * Decode a protocol string into a key-value map.
   * 
   * @param string  The string to decode.
   * 
   * @return        A key-value map mapping directives to strings,
   *                lists and maps.
   *                
   * @throws        DecodeFailureException if the decoding engine 
   *                returns something other than a map.
   */
  
  public static Map<?, ?>
  decode (String string) throws DecodeFailureException
  {
    Object result = JSONValue.parse (string);
    
    if (result instanceof JSONObject)
      return (JSONObject) result;
    else
      throw new DecodeFailureException ("Result not a map.");
  }
  
  
  /**
   * Check if a response is flagged as having OK status.
   * 
   * @param response  The response message, as a key-value map
   *                  (eg in decoded format).  
   * 
   * @return          true if the response is flagged with OK status, 
   *                  false if not (eg ERROR status).
   *                  
   * @throws          InvalidMessageException if the response is 
   *                  invalid (eg the status is missing).
   */
  
  public static boolean
  responseIsOK (Map<?, ?> response)
  throws InvalidMessageException
  {
    if (response.containsKey (Directive.STATUS.toString ()) == false)
      throw new InvalidMessageException ("No status line in response.");
    
    if ((response.get (Directive.STATUS.toString ()) instanceof String) == false)
      throw new InvalidMessageException ("Status is not a string.");
      
    return (((String) response.get (Directive.STATUS.toString ()))
            .equals (Status.OK.toString ()));
  } 
}

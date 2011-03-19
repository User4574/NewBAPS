/**
 * 
 */
package uk.org.ury.server.protocol;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 * Utilities for converting between strings encoded in the response 
 * protocol and collections of items.
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
  
}

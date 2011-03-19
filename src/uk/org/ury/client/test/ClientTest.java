/**
 * 
 */
package uk.org.ury.client.test;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.org.ury.client.Client;


/**
 * JUnit test for the low-level client logic.
 * 
 * @author Matt Windsor
 */

public class ClientTest
{

  /**
   * @throws  java.lang.Exception
   */
  
  @Before
  public void
  setUp () throws Exception
  {
  }

  
  /**
   * @throws  java.lang.Exception
   */
  
  @After
  public void
  tearDown () throws Exception
  {
  }

  
  /**
   * Test method for  {@link uk.org.ury.client.Client#get(java.lang.String)}.
   */
  
  @Test
  public void
  testGet ()
  {
    Client client = new Client ();
    
    List<String> response = client.get ("/server/ServerRequestHandler?function=test");
    
    Assert.assertEquals ("INFO: Test succeeded.", response.get (2));
  }

}

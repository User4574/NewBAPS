/**
 * 
 */
package uk.org.ury.protocol;


/**
 * Statuses that can follow the STATUS directory.
 * 
 * @author  Matt Windsor
 */

public enum Status
  {
    OK,    // The request was processed OK; response should be valid
    ERROR  // An error occurred; message provided as REASON directive
  }

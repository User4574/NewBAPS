/**
 * 
 */
package uk.org.ury.database;


/**
 * The various user classes of the database driver.
 * 
 * These refer to various users in the database proper, and thus 
 * grant various levels of permission to the program.
 * 
 * Please use the least privileged user class that works.  For most 
 * cases, READ_ONLY should work perfectly.
 * 
 * @author  Matt Windsor
 *
 */

public enum UserClass
  {
    READ_ONLY,
    READ_WRITE
  }

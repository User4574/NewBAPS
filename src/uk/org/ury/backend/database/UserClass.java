/**
 * 
 */
package uk.org.ury.backend.database;

/**
 * The various user classes of the database driver.
 * 
 * These refer to various users in the database proper, and thus grant various
 * levels of permission to the program.
 * 
 * Please use the least privileged user class that works. For most cases,
 * READ_ONLY should work perfectly.
 * 
 * @author Matt Windsor
 * 
 */

public enum UserClass {
    // Constant configName
    READ_ONLY   ("read_only"), 
    READ_WRITE  ("read_write");

    /**
     * The name of the tag in the configuration file that contains the
     * credentials for this user class.
     */
    public String configName;

    /**
     * Constructs a new UserClass.
     * 
     * @param configName The name of the user class in the config.
     */
    private UserClass(String configName) {
	this.configName = configName;
    }
}

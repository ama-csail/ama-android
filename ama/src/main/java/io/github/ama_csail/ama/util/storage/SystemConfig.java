package io.github.ama_csail.ama.util.storage;

/**
 * A collection of properties regarding the functioning of the AMA library on different devices.
 * For example, this class provides functionality for determining if a given device will run AMA
 * @author Aaron Vontell
 */
public class SystemConfig {

    private static final String VERSION = "0.0.1";
    private static final String HOMEPAGE = "http://ama-csail.github.io";


    /**
     * Returns a string representing the version of this AMA library.
     * @return a string representing the version of this AMA library.
     */
    public static String getVersion() {
        return VERSION;
    }

    /**
     * Returns a url to a page which can be visited for more information about the library.
     * @return a url to a page which can be visited for more information about the library.
     */
    public static String getHomepage() {
        return HOMEPAGE;
    }

}

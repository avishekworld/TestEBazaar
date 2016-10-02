package alltests;

import java.io.File;
import java.util.logging.Logger;

import business.externalinterfaces.RulesConfigProperties;

import middleware.DbConfigProperties;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTests extends TestSuite {
    static Logger log = Logger.getLogger(AllTests.class.getName());
    //using git and mac os so path is different than windows os
    private static final String LOC_DB_PROPS = "/ebazaar/EBazaar/resources/dbconfig.properties";
    private static final String LOC_RULES_PROPS = "/ebazaar/EBazaar/resources/rulesconfig.properties";
    private static final String context = computeDir();
    static {
    	initializeProperties();
	}
    
    private static String computeDir() {
    	File f = new File(System.getProperty("user.dir"));
    	if(f.exists() && f.isDirectory()) {
    		File parent =new File(f.getParent());
    		String parentOfParaent = parent.getParent();
    		System.out.println( "User Dir " + parentOfParaent);
    		return parentOfParaent;
    	}
    	return null;
    	
    }
    
    @SuppressWarnings("unused")
	private static boolean initialized = false;
    
    public static synchronized void initializeProperties() {
    	// Need to specify full path to dbconfig.properties
		// when accessing from outside the project.
    	if (!initialized) {
    		DbConfigProperties.readProps(context + LOC_DB_PROPS);
    		RulesConfigProperties.readProps(context + LOC_RULES_PROPS);
    		initialized = true;
    	}
    }
	
	public static Test suite() 
	{
		TestSuite suite = new TestSuite();
		//$JUnit-BEGIN$ -- put fully qualified classnames of all tests here
		suite.addTest(new TestSuite(integrationtests.BrowseAndSelectTest.class));
		suite.addTest(new TestSuite(performancetests.RulesPerformanceTests.class));
		suite.addTest(new TestSuite(unittests.business.StringParseTest.class));
		suite.addTest(new TestSuite(unittests.middleware.dataaccess.SimpleConnectionPoolTest.class));
		
		//$JUnit-END$
		return suite;
	}
	
	
	
	
}


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
    //if not using git and mac plz assign false to isUsingGit flag and isUsingMac flag
    static boolean isUsingGit = false;
    static boolean isUsingMac = true;
    private static  String LOC_DB_PROPS = "/ebazaar/EBazaar/resources/dbconfig.properties";
    private static  String LOC_RULES_PROPS = "/ebazaar/EBazaar/resources/rulesconfig.properties";
    private static String context = "";
    static {
    	if(isUsingMac && isUsingGit){
    		LOC_DB_PROPS = "/ebazaar/EBazaar/resources/dbconfig.properties";
    	    LOC_RULES_PROPS = "/ebazaar/EBazaar/resources/rulesconfig.properties";
    	}if(isUsingMac && !isUsingGit){
    		LOC_DB_PROPS = "/EBazaar/resources/dbconfig.properties";
    	    LOC_RULES_PROPS = "/EBazaar/resources/rulesconfig.properties";
    	}else if(!isUsingMac && isUsingGit){
    		LOC_DB_PROPS = "\\ebazaar\\EBazaar\\resources\\dbconfig.properties";
    	    LOC_RULES_PROPS = "\\ebazaar\\EBazaar\\resources\\rulesconfig.properties";
    	}else if(!isUsingMac && !isUsingGit){
    		LOC_DB_PROPS = "\\EBazaar\\resources\\dbconfig.properties";
    	    LOC_RULES_PROPS = "\\EBazaar\\resources\\rulesconfig.properties";
    	}
    	context = computeDir();
    	initializeProperties();
	}
    
    private static String computeDir() {
    	File f = new File(System.getProperty("user.dir"));
    	System.out.println("Test project dir " + f.toString());
    	if(f.exists() && f.isDirectory()) {
    		String userDir ="";
    		File parent =new File(f.getParent());
    		if(isUsingGit){
    			userDir = parent.getParent();
    		}else{
    			userDir = parent.toString();
    		}
    		System.out.println("Root dir for all projects " + userDir);
    		return userDir;
    	}
    	return null;
    	
    }
    
    @SuppressWarnings("unused")
	private static boolean initialized = false;
    
    public static synchronized void initializeProperties() {
    	// Need to specify full path to dbconfig.properties
		// when accessing from outside the project.
    	if (!initialized) {
    		System.out.println("AllTest Reading Properties");
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


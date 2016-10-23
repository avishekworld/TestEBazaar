
package unittests.middleware.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import alltests.AllTests;
import business.externalinterfaces.RulesConfigProperties;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.*;
import middleware.externalinterfaces.DbConfigKey;
import middleware.externalinterfaces.IDataAccessTest;
//import alltests.AllTests;
import junit.framework.TestCase;
import dbsetup.DbQueries;

/**
 * @author pcorazza
 * @since Nov 13, 2004
 * Class Description:
 * 
 * 
 */
public class SimpleConnectionPoolTest extends TestCase {
	static Logger log = Logger.getLogger(SimpleConnectionPoolTest.class.getName());
	static String name = "middleware.dataaccess.SimpleConnectionPool";
	static DbConfigProperties dbProb;
	static RulesConfigProperties rulesProb;
	static {
		/*As actual implementation of DbConfigProperties and RulesConfigProperties class load props in static code block
		 * so we need to first let that go with invalid file path and than AllTest can override 
		 * those prob on its initialProperties method
		*/
		dbProb = new DbConfigProperties();
		rulesProb = new RulesConfigProperties();
		AllTests.initializeProperties();
	}
	final String ACCOUNT_DBURL =dbProb.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
    final String PRODUCT_DBURL =dbProb.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
    final int MAX_CONN = 3;
	
	public SimpleConnectionPoolTest(String s) {
		super(s);
	}
	
	public void setUp() {
		log.info("Running "+ getName());		
 	}
	
	//tests
	public void testMultipleDbConnections() {
		// Add rows to two tables on different dbs
		String[] custVals = DbQueries.insertCustomerRow();
		String[] prodVals = DbQueries.insertProductRow();
		// Prepare queries for each
		String[] queries = {"select productid,productname from product",
							"select custid,fname,lname from customer"};	
		String[] dburls = {PRODUCT_DBURL,ACCOUNT_DBURL};	
	    IDataAccessTest test = new DataAccessSubsystemFacade();
	    String expectedProdName = "";
	    String prodNameFound = null;
	    String expectedCustName = "";
	    String custNameFound = null;
	    try {
	    	ResultSet[] results = test.multipleInstanceQueries(queries, dburls);
	    	// gather up product results
	    	int prodId = Integer.parseInt(prodVals[1]);
	    	expectedProdName = prodVals[2];
	    	
	        while(results[0].next()){
	        	int idFound = Integer.parseInt(results[0].getString("productid"));
	        	if (idFound == prodId) {
	        		prodNameFound = results[0].getString("productname");
	        	}
	        }
	        //gather up cust results
	        int custId = Integer.parseInt(custVals[1]);
	    	expectedCustName = custVals[2]+custVals[3];
	    	
	        while(results[1].next()){
	        	int idFound = Integer.parseInt(results[1].getString("custid"));
	        	if (idFound == custId) {
	        		custNameFound = results[1].getString("fname");
	        		custNameFound += results[1].getString("lname");
	        	}
	        }
	        

	    }
   		catch(DatabaseException ex){
   			fail("ERROR: Error occurred trying to read table: "+ex.getClass().getName()+" Message: "+ex.getMessage());
   		}
   		catch(SQLException ex){
			fail("ERROR: Error occurred trying to read table: "+ex.getClass().getName()+" Message: "+ex.getMessage());
		}
   		finally {
   			assertTrue(expectedProdName.equals(prodNameFound));
   			assertTrue(expectedCustName.equals(custNameFound));
   			//Now delete the rows that were added for testing
   			DbQueries.deleteProductRow(Integer.parseInt(prodVals[1]));
   			DbQueries.deleteCustomerRow(Integer.parseInt(custVals[1]));
   		}
		    
	}
	
}

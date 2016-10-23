package unittests.middleware.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import alltests.AllTests;
import business.externalinterfaces.RulesConfigProperties;
import dbsetup.DbQueries;
import junit.framework.TestCase;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.dataaccess.DataAccessUtil;
import middleware.externalinterfaces.DbConfigKey;
import middleware.externalinterfaces.IDataAccessTest;

public class DataAccessUtilTest extends TestCase {
	static Logger log = Logger.getLogger(DataAccessUtilTest.class.getName());
	static String name = "middleware.dataaccess.DataAccessUtil";
	static DbConfigProperties dbProb;
	static RulesConfigProperties rulesProb;
	static {
		/*As actual implementation of DbConfigProperties and RulesConfigProperties class load props in static code block
		 * so we need to first let that go with invalid file path and than AllTest can override 
		 * those prob on its initialProperties method
		*/
		dbProb = new DbConfigProperties();
		AllTests.initializeProperties();
	}
	DbConfigProperties props = new DbConfigProperties();
    final String PRODUCT_DBURL =dbProb.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
    
    public void testRunQuery(){
    	String[] prodVals = DbQueries.insertProductRow();
		String query = "select productid,productname from product";	
	    IDataAccessTest test = new DataAccessSubsystemFacade();
	    String expectedProdName = "";
	    String prodNameFound = null;
	    
	    try {
	    	ResultSet resultSet = test.runTestQuery(query, PRODUCT_DBURL);
	    	// gather up product results
	    	int prodId = Integer.parseInt(prodVals[1]);
	    	expectedProdName = prodVals[2];
	    	
	        while(resultSet.next()){
	        	int idFound = Integer.parseInt(resultSet.getString("productid"));
	        	if (idFound == prodId) {
	        		prodNameFound = resultSet.getString("productname");
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
   			//Now delete the rows that were added for testing
   			DbQueries.deleteProductRow(Integer.parseInt(prodVals[1]));
   		}
    }
    
    public void testRunUpdate(){
    	String[] prodVals = DbQueries.insertProductRow();
    	String expectedProdName = "updatedname";
		String query = "update product set productname='"+ expectedProdName +"' where productid="+prodVals[1];	
		System.out.println("update query " + query);
	    IDataAccessTest test = new DataAccessSubsystemFacade();
	    String prodNameFound = null;
	    
	    try {
	    	test.runTestUpdate(query, PRODUCT_DBURL);
	    	query = "select * from product where productid="+prodVals[1];
	    	System.out.println("search query " + query);
	    	ResultSet resultSet = test.runTestQuery(query, PRODUCT_DBURL);
	    	int prodId = Integer.parseInt(prodVals[1]);
	    	System.out.println("my prodId " + prodId);
	        while(resultSet.next()){
	        	int idFound = Integer.parseInt(resultSet.getString("productid"));
	        	System.out.println("idFound" + idFound);
	        	if (idFound == prodId) {
	        		prodNameFound = resultSet.getString("productname");
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
   			//Now delete the rows that were added for testing
   			DbQueries.deleteProductRow(Integer.parseInt(prodVals[1]));
   		}
    }
}

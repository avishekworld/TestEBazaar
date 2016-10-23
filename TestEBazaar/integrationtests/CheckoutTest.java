package integrationtests;

import java.util.List;
import java.util.logging.Logger;

import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICustomerSubsystem;
import business.externalinterfaces.IProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import dbsetup.DbQueries;
import junit.framework.TestCase;
import middleware.DatabaseException;

public class CheckoutTest extends TestCase {
	
	static String name = "Checkout Test";
	static Logger log = Logger.getLogger(CheckoutTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}

	public void testSelectShipAddressStep(){
		// Add row in customer table for testing
		String[] customerVals = DbQueries.insertCustomerRow();
		// Add row in altshipaddress table for testing
		String[] addressVals = DbQueries.insertAddressRow(customerVals[1]);
		
		String expectedName = addressVals[3];
		
		// Perform test
        ICustomerSubsystem customerSubsystem = new CustomerSubsystemFacade();
		List<IAddress> adressList=null;
        
		try {
			customerSubsystem.initializeCustomer(Integer.parseInt(customerVals[1]));
			adressList = customerSubsystem.getAllAddresses();
                
		}
		catch(DatabaseException ex){
			fail("DatabaseException: " + ex.getMessage());
		}
		finally {
			assertTrue(adressList != null);
			boolean nameFound = false;
			if(adressList != null){
				for (IAddress adress : adressList) {
					if(adress.getStreet1().equals(expectedName)) {
						nameFound=true;
						System.out.println(adress.getStreet1());
						break;
					}
				}
			}
			assertTrue(nameFound);
			// Clean up table
			DbQueries.deleteAddressRow(Integer.parseInt(addressVals[1]));
			DbQueries.deleteCustomerRow(Integer.parseInt(customerVals[1]));
			
		}
	}
}

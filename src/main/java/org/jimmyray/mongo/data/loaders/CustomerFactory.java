package org.jimmyray.mongo.data.loaders;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jimmyray.mongo.data.model.Customer;
import org.jimmyray.mongo.data.model.CustomerAddress;
import org.jimmyray.mongo.framework.Filer;
import org.jimmyray.mongo.framework.Properties;
import org.jimmyray.mongo.framework.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory for creating Customer objects.
 */
public final class CustomerFactory {
	
	/** The log. */
	private static Logger log = LoggerFactory.getLogger(CustomerFactory.class);

	/** The Constant COMMON_DELIMITER. */
	private static final String COMMON_DELIMITER = Properties
			.getString("employeeFactory.delimiter.comma");

	/**
	 * Instantiates a new customer factory.
	 */
	private CustomerFactory() {
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			loadData();
		} catch (FileNotFoundException fnfe) {
			log.error(Strings.getStackTraceAsString(fnfe));
		}
	}

	/**
	 * Load data.
	 *
	 * @return the list
	 * @throws FileNotFoundException the file not found exception
	 */
	public static List<Customer> loadData() throws FileNotFoundException {
		List<Customer> customers = new ArrayList<Customer>();
		Filer filer = new Filer();

		List<String> lines = filer.readFile(Properties
				.getString("customerFactory.path.customerData")); //$NON-NLS-1$

		for (String line : lines) {
			Customer customer = new Customer();
			CustomerAddress customerAddress = new CustomerAddress();

			Scanner scanner = new Scanner(line);
			scanner.useDelimiter(CustomerFactory.COMMON_DELIMITER);

			customer.setCustomerId(scanner.next().trim());
			customer.setCustomerName(scanner.next().trim());

			customerAddress.setAddressLine1(scanner.next().trim());
			customerAddress.setCity(scanner.next().trim());
			customerAddress.setState(scanner.next().trim());
			customerAddress.setZipCode(scanner.next().trim());
			customerAddress.setCountry(scanner.next().trim());
			customerAddress.setPhone(scanner.next().trim());
			customerAddress.setFax(scanner.next().trim());

			customer.setAddress(customerAddress);
			customer.setShippingMethod(scanner.next().trim());
			customer.setTerms(scanner.next().trim());
			customer.setTaxRate(Float.parseFloat(scanner.next().trim()));

			customers.add(customer);
			log.info(customer.toString());
		}

		return customers;
	}
}

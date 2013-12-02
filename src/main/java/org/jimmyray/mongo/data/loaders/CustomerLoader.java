package org.jimmyray.mongo.data.loaders;

import java.util.List;

import org.jimmyray.mongo.data.model.Customer;
import org.jimmyray.mongo.framework.Properties;
import org.jimmyray.mongo.framework.Strings;
import org.jimmyray.mongo.services.CustomerAddressService;
import org.jimmyray.mongo.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Loads customer data into MongoDB.
 * 
 * @author jimmyray
 * @version 1.0
 */
public final class CustomerLoader {
	private static Logger log = LoggerFactory.getLogger(CustomerLoader.class);

	private CustomerLoader() {

	}

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		log.info(Properties
				.getString("springMongoConfig.msg.startingContainer")); //$NON-NLS-1$

		try {
			ApplicationContext ctx = new GenericXmlApplicationContext(
					Properties.getString("springMongoConfig.path.configFile")); //$NON-NLS-1$

			CustomerService customerService = (CustomerService) ctx
					.getBean(Properties
							.getString("springMongoConfig.bean.customerService")); //$NON-NLS-1$

			CustomerAddressService customerAddressService = (CustomerAddressService) ctx
					.getBean(Properties
							.getString("springMongoConfig.bean.customerAddressService")); //$NON-NLS-1$

			List<Customer> customers = CustomerFactory.loadData();

			log.info(Properties.getString("customerLoader.msg.saving")); //$NON-NLS-1$

			//Must save addresses first
			for (Customer customer : customers) {
				customer.setAddress(customerAddressService
						.saveCustomerAddress(customer.getAddress()));
			}

			customerService.saveCustomers(customers);

			log.info(Properties.getString("mongoEmployeeLoader.msg.complete")); //$NON-NLS-1$
		} catch (Exception e) {
			log.error(Strings.getStackTraceAsString(e));
		}
	}
}

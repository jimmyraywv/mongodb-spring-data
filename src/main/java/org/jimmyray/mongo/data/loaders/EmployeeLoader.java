package org.jimmyray.mongo.data.loaders;

import java.util.List;

import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.framework.Properties;
import org.jimmyray.mongo.framework.SpringBeanFactory;
import org.jimmyray.mongo.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Loads data into MongoDB.
 * 
 * @author jimmyray
 * @version 1.0
 */
public final class EmployeeLoader {
	private static Logger log = LoggerFactory.getLogger(EmployeeLoader.class);

	private EmployeeLoader() {

	}

	public static void main(String[] args) {
		EmployeeLoader.loadEmployees();
	}

	public static void loadEmployees() {
		log.debug(Properties
				.getString("springMongoConfig.msg.startingContainer")); //$NON-NLS-1$
		SpringBeanFactory.initContext();
		EmployeeLoader.loadEmployees(SpringBeanFactory.getContext(), false, 0);
	}

	public static void loadEmployees(ApplicationContext ctx,
			boolean batchInsert, int batchSize) {

		EmployeeService employeeService = (EmployeeService) SpringBeanFactory
				.getBean(Properties
						.getString("springMongoConfig.bean.employeeService")); //$NON-NLS-1$

		long startTime = System.currentTimeMillis();
		List<Employee> employees = EmployeeFactory.loadData();
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("Data load time: " + duration + "ms");
		log.info(Properties.getString("employeeShortLoader.msg.saving")); //$NON-NLS-1$

		startTime = System.currentTimeMillis();

		if (batchInsert) {
			employeeService.bulkInsert(employees, batchSize);
		} else {
			employeeService.saveEmployees(employees);
		}

		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		log.info("Data save time: " + duration + "ms");

		log.info(Properties.getString("mongoEmployeeLoader.msg.complete")); //$NON-NLS-1$
	}
}

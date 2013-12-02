package org.jimmyray.mongo.data.loaders;

import java.util.List;

import org.jimmyray.mongo.data.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class EmployeeShortLoadGenerator is used to generate records for import.
 */
public final class EmployeeShortLoadGenerator {

	/** The log. */
	private static Logger log = LoggerFactory
			.getLogger(EmployeeShortLoadGenerator.class);

	private EmployeeShortLoadGenerator() {

	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		List<Employee> employees = EmployeeFactory.loadData();

		for (Employee employee : employees) {
			log.info(employee.toString());
		}
	}
}

package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jimmyray.mongo.data.loaders.EmployeeShortLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.services.EmployeeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;

public class PagingQueryTest {
	private static Logger log = LoggerFactory.getLogger(PagingQueryTest.class);

	private ApplicationContext ctx;
	MongoOperations mongoOps;
	List<Employee> employees;
	EmployeeService employeeService;

	@Before
	public void setup() {
		ctx = new GenericXmlApplicationContext("context/main.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
		employeeService = (EmployeeService) ctx.getBean("employeeService");
		try {
			EmployeeShortLoader.main(null);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}

	@Test
	public void testPaging() {
		String[] lastNames = new String[] { "Stanfel", "Gustavson", "Lortz", "Marquardt", "Unno", "Savasere", "Spelt",
				"Wynblatt", "Danecki", "Weedman", "Hartvigsen", "Menhoudj", "Heyers", "Willoner", "Shumilov", "Zuberek",
				"Boguraev" };
		int pageCount = 10;
		int pageNumber = 0;
		String sortField = EmployeeProperties.EMPLOYEE_ID;
		Sort.Direction sortOrder = Sort.Direction.DESC;

		Page<Employee> employeesPage = employeeService.findAllWithPages(pageNumber, pageCount, sortOrder, sortField);

		while (employeesPage.hasNext()) {

			assertEquals("List size is incorrect.", pageCount, employeesPage.getSize());

			log.info("Page Number = " + pageNumber);

			if (employeesPage.hasContent()) {
				log.info(employeesPage.getContent().get(employeesPage.getSize() - 1).getLastName());

				assertEquals("Last name was incorrect.", lastNames[pageNumber],
						employeesPage.getContent().get(employeesPage.getSize() - 1).getLastName());
			}

			pageNumber++;

			employeesPage = employeeService.findAllWithPages(pageNumber, pageCount, sortOrder, sortField);
		}

		log.info("Page Number = " + pageNumber);

		employeesPage = employeeService.findAllWithPages(pageNumber, pageCount, sortOrder, sortField);

		log.info(employeesPage.getContent().get(employeesPage.getContent().size() - 1).getLastName());

		assertEquals("Last name was incorrect.", lastNames[pageNumber],
				employeesPage.getContent().get(employeesPage.getContent().size() - 1).getLastName());
	}

	@After
	public void tearDown() {
		// this.mongoOps.getCollection(EmployeeProperties.COLLECTION).drop();
	}
}

package org.jimmyray.mongo;

import java.util.List;

import org.jimmyray.mongo.data.loaders.EmployeeShortLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.services.EmployeeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

public class WriteConcernTest {
	private static Logger log = LoggerFactory.getLogger(WriteConcernTest.class);
	private Mongo mongo;
	ApplicationContext ctx;

	private static List<Employee> employees;
	private static EmployeeService employeeService;

	static {
		try {
			employees = EmployeeShortLoader.getEmployees();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}

	@Before
	public void setup() {
		ctx = new GenericXmlApplicationContext("context/main.xml");
		employeeService = (EmployeeService) ctx.getBean("employeeService");
	}

	@Test
	public void testWriteConcernNormal() {
		mongo = (Mongo) ctx.getBean("mongo");
		mongo.setWriteConcern(WriteConcern.NORMAL);
		long startTime = System.currentTimeMillis();
		employeeService.saveEmployees(employees);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("WriteConcernNormal time: " + duration + "ms");
	}

	//No indication of network errors
	@Test
	public void testWriteConcernSafeNone() {
		mongo = (Mongo) ctx.getBean("mongo");
		mongo.setWriteConcern(WriteConcern.SAFE);
		long startTime = System.currentTimeMillis();
		employeeService.saveEmployees(employees);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("WriteConcernSafe time: " + duration + "ms");
	}

	// This one runs forever with large data sets.  Waits till journal file is written.
	@Test
	public void testWriteConcernJournalSafe() {
		mongo = (Mongo) ctx.getBean("mongo");
		mongo.setWriteConcern(WriteConcern.JOURNAL_SAFE);
		long startTime = System.currentTimeMillis();
		employeeService.saveEmployees(employees);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("WriteConcernJournalSafe time: " + duration + "ms");
	}

	// The fsync command forces the mongod process to flush all pending writes
	// to the storage layer.
	@Test
	public void testWriteConcernFsync() {
		mongo = (Mongo) ctx.getBean("mongo");
		mongo.setWriteConcern(WriteConcern.FSYNC_SAFE);
		long startTime = System.currentTimeMillis();
		employeeService.saveEmployees(employees);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("WriteConcernFsync time: " + duration + "ms");
	}

	@After
	public void clearDatabase() {
		MongoOperations mongoOps = (MongoOperations) ctx
				.getBean("mongoTemplate");
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}

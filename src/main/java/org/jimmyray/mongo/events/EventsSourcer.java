package org.jimmyray.mongo.events;

import java.util.List;

import org.bson.Transformer;
import org.jimmyray.mongo.data.loaders.EmployeeFactory;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.transformers.EmployeeTransformer;
import org.jimmyray.mongo.framework.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class EventsSourcer {
	private static Logger log = LoggerFactory.getLogger(EventsSourcer.class);

	public static void main(String[] args) {
		EventsSourcer eventsSourcer = new EventsSourcer();
		eventsSourcer.sourceEvents();
	}

	public void sourceEvents() {
		ApplicationContext ctx = new GenericXmlApplicationContext(
				Properties.getString("springMongoConfig.path.configFile"));
		List<Employee> employees = EmployeeFactory.loadData();
		CappedCollection cappedCollection = new CappedCollection();
		DBCollection collection = cappedCollection.createCappedCollection(ctx, "HRDB", "events");
		Transformer transformer = new EmployeeTransformer();

		for (Employee employee : employees) {
			log.debug(employee.toString());
			collection.insert((DBObject) transformer.transform(employee));
		}
	}
}

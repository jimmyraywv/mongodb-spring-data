package org.jimmyray.mongo.data.loaders;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.jimmyray.mongo.data.model.JobDescription;
import org.jimmyray.mongo.framework.Filer;
import org.jimmyray.mongo.framework.Properties;
import org.jimmyray.mongo.framework.Strings;
import org.jimmyray.mongo.services.JobDescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Job description data loader.
 * 
 * @author jimmyray
 * @version 1.0
 */
public final class JobDescriptionLoader {

	/** The log. */
	private static Logger log = LoggerFactory
			.getLogger(JobDescriptionLoader.class);

	/**
	 * Instantiates a new job description loader.
	 */
	private JobDescriptionLoader() {

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		log.info(Properties
				.getString("springMongoConfig.msg.startingContainer")); //$NON-NLS-1$

		try {
			ApplicationContext ctx = new GenericXmlApplicationContext(
					Properties.getString("springMongoConfig.path.configFile")); //$NON-NLS-1$

			Set<JobDescription> jobs = loadJobs();

			JobDescriptionService jobDescriptionService = (JobDescriptionService) ctx
					.getBean(Properties
							.getString("springMongoConfig.bean.jobDescriptionService")); //$NON-NLS-1$

			jobDescriptionService.saveJobDescriptions(jobs);

		} catch (Exception e) {
			log.error(Strings.getStackTraceAsString(e));
		}
	}

	
	/**
	 * Load jobs.
	 *
	 * @return the sets the
	 * @throws FileNotFoundException the file not found exception
	 */
	private static Set<JobDescription> loadJobs() throws FileNotFoundException {
		Set<JobDescription> jobs = new HashSet<JobDescription>();

		log.info(Properties.getString("employeeFactory.msg.loadTitles")); //$NON-NLS-1$

		Filer filer = new Filer();
		List<String> lines = filer.readFile(Properties
				.getString("employeeFactory.path.titleData")); //$NON-NLS-1$

		for (String line : lines) {
			Scanner scanner = new Scanner(line);
			scanner.useDelimiter(Properties
					.getString("employeeFactory.delimiter.comma")); //$NON-NLS-1$
			scanner.next();
			String title = scanner.next();

			JobDescription job = new JobDescription();
			job.setTitle(title);
			job.setDescription("Description::" + title);

			jobs.add(job);
		}

		return jobs;
	}
}

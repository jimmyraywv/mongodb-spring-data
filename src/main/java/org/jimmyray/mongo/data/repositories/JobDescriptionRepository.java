package org.jimmyray.mongo.data.repositories;

import java.util.List;

import org.jimmyray.mongo.data.model.JobDescription;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB Repository for JobDescription objects. Methods are not
 * concretely implemented anywhere at design time. Methods are implemented at
 * Spring Container startup.
 * 
 * @author jimmyray
 * @version 1.0
 */
public interface JobDescriptionRepository extends
		MongoRepository<JobDescription, String> {

	List<JobDescription> findAll();

	List<JobDescription> findByTitle(String title);

	JobDescription save(JobDescription jobDescription);

	void delete(JobDescription jobDescription);

	void deleteAll();
}

package org.jimmyray.mongo.services;

import java.util.List;
import java.util.Set;

import org.jimmyray.mongo.data.model.JobDescription;

/**
 * JobDescriptionService interface defines Job Description service contract.
 * 
 * @author jimmyray
 * @version 1.0
 */
public interface JobDescriptionService {
	JobDescription saveJobDescription(JobDescription jobDescription);

	void saveJobDescriptions(Set<JobDescription> jobDescriptions);

	List<JobDescription> findAll();

	List<JobDescription> findByTitle(String title);

	void deleteJobDescription(JobDescription jobDescription);

	void deleteAll();
}

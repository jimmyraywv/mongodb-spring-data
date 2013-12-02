package org.jimmyray.mongo.services;

import java.util.List;
import java.util.Set;

import org.jimmyray.mongo.data.model.JobDescription;
import org.jimmyray.mongo.data.repository.JobDescriptionRepository;

/**
 * Implementation of JobDescriptionService contract.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class JobDescriptionServiceImpl implements JobDescriptionService {
	private JobDescriptionRepository jobDescriptionRepository;

	//@Override
	public JobDescription saveJobDescription(JobDescription jobDescription) {
		return jobDescriptionRepository.save(jobDescription);
	}

	//@Override
	public void saveJobDescriptions(Set<JobDescription> jobDescriptions) {
		jobDescriptionRepository.save(jobDescriptions);
	}

	//@Override
	public List<JobDescription> findAll() {
		return jobDescriptionRepository.findAll();
	}

	//@Override
	public List<JobDescription> findByTitle(String title) {
		return jobDescriptionRepository.findByTitle(title);
	}

	//@Override
	public void deleteJobDescription(JobDescription jobDescription) {
		jobDescriptionRepository.delete(jobDescription);
	}

	//@Override
	public void deleteAll() {
		jobDescriptionRepository.deleteAll();
	}

	public void setJobDescriptionRepository(
			JobDescriptionRepository jobDescriptionRepository) {
		this.jobDescriptionRepository = jobDescriptionRepository;
	}

}

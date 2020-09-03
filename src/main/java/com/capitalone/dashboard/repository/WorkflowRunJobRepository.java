
package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.GitWorkflowRunJob;

public interface WorkflowRunJobRepository<T extends GitWorkflowRunJob> extends CrudRepository<T, ObjectId> {

	@Query(value = "{'jobId': ?0}")
	public GitWorkflowRunJob findByJobId(String jobId);

	@Query(value = "{'runId': ?0}")
	public List<GitWorkflowRunJob> findByRunId(String runId);

}

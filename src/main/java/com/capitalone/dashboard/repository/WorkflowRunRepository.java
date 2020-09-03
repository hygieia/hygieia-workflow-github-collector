
package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.GitWorkflowRun;

public interface WorkflowRunRepository<T extends GitWorkflowRun> extends CrudRepository<T, ObjectId> {

	@Query(value = "{'runId': ?0}")
	public GitWorkflowRun findByRunId(String runId);

	@Query(value = "{'workflowId': ?0}")
	public List<GitWorkflowRun> findByWorkflowId(String workflowId);

}

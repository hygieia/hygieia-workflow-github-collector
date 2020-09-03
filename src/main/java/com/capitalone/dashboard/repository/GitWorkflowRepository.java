package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.GitWorkflow;

public interface GitWorkflowRepository<T extends GitWorkflow> extends CrudRepository<T, ObjectId> {
	
	@Query(value = "{'collectorId' : ?0}")
	public List<GitWorkflow> findByCollectorId(ObjectId collectorId);
	
	@Query(value = "{'workflowId' : ?0}")
	public boolean exists(String workflowId);
	
	@Query(value = "{'enabled' : ?0}")
	public List<GitWorkflow> findEnabledWorkflows(Boolean enabled);
	
	@Query(value = "{'workflowId' : ?0}")
	public GitWorkflow findByWorkflowId(String workflowId);
}

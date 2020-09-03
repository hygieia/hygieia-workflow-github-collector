package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.GitWorkflowRepo;

public interface GitWorkflowRepoRepository<T extends GitWorkflowRepo> extends CrudRepository<T, ObjectId> {
	
	@Query(value = "{'collectorId' : ?0}")
	public List<GitWorkflowRepo> findByCollectorId(ObjectId collectorId);
	
	@Query(value = "{'workflowId' : ?0}")
	public boolean exists(String workflowId);
	
	@Query(value = "{'enabled' : ?0}")
	public List<GitWorkflowRepo> findEnabledWorkflows(Boolean enabled);
	
	@Query(value = "{'workflowId' : ?0}")
	public GitWorkflowRepo findByWorkflowId(String workflowId);
}

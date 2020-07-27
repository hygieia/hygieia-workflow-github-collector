package com.capitalone.dashboard.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.Workflow;

public interface WorkflowRepository<T extends Workflow> extends CrudRepository<T, ObjectId> {

 
}

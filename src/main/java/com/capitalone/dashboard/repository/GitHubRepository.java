package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.GitHub;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;


import java.util.List;
 
public interface GitHubRepository extends BaseCollectorItemRepository<GitHub> {

    @Query(value="{ 'collectorId' : ?0, options.repoUrl : ?1, options.branch : ?2}")
    GitHub findGitHubRepo(ObjectId collectorId, String url, String branch);

    @Query(value="{ 'collectorId' : ?0, enabled: true}")
    List<GitHub> findEnabledGitHubRepos(ObjectId collectorId);
}

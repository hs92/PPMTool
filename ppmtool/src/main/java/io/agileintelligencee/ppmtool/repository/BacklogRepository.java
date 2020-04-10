package io.agileintelligencee.ppmtool.repository;

import io.agileintelligencee.ppmtool.model.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {
    Backlog findByProjectIdentifier(String identifier);
}

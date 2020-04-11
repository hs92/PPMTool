package io.agileintelligencee.ppmtool.repository;

import io.agileintelligencee.ppmtool.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository <Project, Long> {

    Project findByProjectIdentifier(String projectId);

    @Override
    Iterable<Project> findAll();

    @Override
    void delete(Project project);

    List<Project> findByProjectLeader(String username);
}

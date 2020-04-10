package io.agileintelligencee.ppmtool.service;

import io.agileintelligencee.ppmtool.exceptions.ProjectIdException;
import io.agileintelligencee.ppmtool.model.Backlog;
import io.agileintelligencee.ppmtool.model.Project;
import io.agileintelligencee.ppmtool.repository.BacklogRepository;
import io.agileintelligencee.ppmtool.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project){
        String projectIdentifier = project.getProjectIdentifier().toUpperCase();
        try {
            project.setProjectIdentifier(projectIdentifier);
            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                backlog.setProjectIdentifier(projectIdentifier);
                backlog.setProject(project);
                project.setBacklog(backlog);
            } else {
                Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
                if(backlog != null) {
                    project.setBacklog(backlog);
                }
            }
            return projectRepository.save(project);
        } catch (Exception e){
            throw new ProjectIdException("Project ID '" + projectIdentifier + "' already exists!");
        }
    }

    public Project findProjectByProjectIdentifier(String projectIdentifier){
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Project with id : " + projectIdentifier.toUpperCase() + " doesn't exist.");
        }
        return project;
    }

    public List<Project> findAllProjects(){
        Iterable<Project> allProjects = projectRepository.findAll();
        List<Project> projectList = new ArrayList<>();
        for(Project project : allProjects){
            projectList.add(project);
        }
        return projectList;
    }

    public void deleteProject(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId);
        if(project == null){
            throw new ProjectIdException("Project with id : " + projectId.toUpperCase() + " doesn't exist.");
        }
        projectRepository.delete(project);
    }

}

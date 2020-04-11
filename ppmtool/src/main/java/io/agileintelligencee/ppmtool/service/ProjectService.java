package io.agileintelligencee.ppmtool.service;

import io.agileintelligencee.ppmtool.exceptions.ProjectIdException;
import io.agileintelligencee.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligencee.ppmtool.model.Backlog;
import io.agileintelligencee.ppmtool.model.Project;
import io.agileintelligencee.ppmtool.model.User;
import io.agileintelligencee.ppmtool.repository.BacklogRepository;
import io.agileintelligencee.ppmtool.repository.ProjectRepository;
import io.agileintelligencee.ppmtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String name){
        String projectIdentifier = project.getProjectIdentifier().toUpperCase();

        if(project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifier(projectIdentifier);
            if(existingProject != null && !existingProject.getProjectLeader().equals(name)) {
                throw new ProjectNotFoundException("Project not found in your account.");
            } else if (existingProject == null) {
                throw new ProjectNotFoundException("Project with ID: " + projectIdentifier + " doesn't exist.");
            }
        }

        try {
            User user = userRepository.findByUsername(name);
            project.setUser(user);
            project.setProjectIdentifier(projectIdentifier);
            project.setProjectLeader(user.getUsername());
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

    public Project findProjectByProjectIdentifier(String projectIdentifier, String username){
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if (project == null){
            throw new ProjectIdException("Project with id : " + projectIdentifier.toUpperCase() + " doesn't exist.");
        }
        if (!username.equals(project.getProjectLeader())) {
            throw new ProjectNotFoundException("Project not found in your account");
        }
        return project;
    }

    public List<Project> findAllProjects(String username) {
        return projectRepository.findByProjectLeader(username);
    }

    public void deleteProject(String projectId, String username) {
        projectRepository.delete(findProjectByProjectIdentifier(projectId, username));
    }

}

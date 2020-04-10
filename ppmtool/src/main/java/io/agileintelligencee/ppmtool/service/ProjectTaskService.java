package io.agileintelligencee.ppmtool.service;

import io.agileintelligencee.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligencee.ppmtool.model.Backlog;
import io.agileintelligencee.ppmtool.model.Project;
import io.agileintelligencee.ppmtool.model.ProjectTask;
import io.agileintelligencee.ppmtool.repository.BacklogRepository;
import io.agileintelligencee.ppmtool.repository.ProjectRepository;
import io.agileintelligencee.ppmtool.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        if (backlog == null) {
            throw new ProjectNotFoundException("Project not found!");
        }
        projectTask.setBacklog(backlog);
        Integer backlogSequence = backlog.getPTSequence();
        backlogSequence++;
        backlog.setPTSequence(backlogSequence);
        projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);
        if(projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }
        if("".equals(projectTask.getStatus()) || projectTask.getStatus() == null) {
            projectTask.setStatus("TO_DO");
        }
        return projectTaskRepository.save(projectTask);
    }

    public List<ProjectTask> findBacklogById(String backlog_id) {
        Project project = projectRepository.findByProjectIdentifier(backlog_id);
        if(project == null) {
            throw new ProjectNotFoundException("Project with ID: " + backlog_id + " doesn't exist!");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if (backlog == null) {
           throw new ProjectNotFoundException("Project with ID: " + backlog_id + " doesn't exist!");
        }
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask == null) {
            throw new ProjectNotFoundException("ProjectTask with ID: " + pt_id + " doesn't exist!");
        }
        if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException("ProjectTask with ID: " + projectTask.getProjectIdentifier() + " doesn't belong to Project with ID: " + backlog_id);
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedProjectTask, String backlog_id, String pt_id) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        if (!updatedProjectTask.getProjectSequence().equals(pt_id)) {
            throw new ProjectNotFoundException("ProjectTask sequence id can't be updated!");
        }
        projectTask = updatedProjectTask;
        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
        projectTaskRepository.delete(projectTask);
    }
}

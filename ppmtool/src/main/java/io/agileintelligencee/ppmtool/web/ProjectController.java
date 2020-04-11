package io.agileintelligencee.ppmtool.web;

import io.agileintelligencee.ppmtool.model.Project;
import io.agileintelligencee.ppmtool.service.ProjectService;
import io.agileintelligencee.ppmtool.service.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ValidationErrorService validationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal) {

        ResponseEntity<?> errorMap = validationErrorService.checkForErrors(result);

        if(null != errorMap){
            return errorMap;
        }

        Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<>(project1, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> findProjectById(@PathVariable String projectId, Principal principal)  {
        Project project = projectService.findProjectByProjectIdentifier(projectId, principal.getName());
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllProjects(Principal principal) {
        List<Project> projects = projectService.findAllProjects(principal.getName());
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
        projectService.deleteProject(projectId, principal.getName());
        return new ResponseEntity<>("Project with ID : " + projectId + "was deleted!", HttpStatus.OK);
    }
}

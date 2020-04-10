package io.agileintelligencee.ppmtool.web;

import io.agileintelligencee.ppmtool.model.ProjectTask;
import io.agileintelligencee.ppmtool.service.ProjectTaskService;
import io.agileintelligencee.ppmtool.service.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ValidationErrorService validationErrorService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult bindingResult,
                                                     @PathVariable String backlog_id) {
        ResponseEntity<?> errorMap = validationErrorService.checkForErrors(bindingResult);
        if(errorMap != null)
            return errorMap;
        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask);
        return new ResponseEntity<>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public ResponseEntity<List<ProjectTask>> getProjectTasksByBacklogId(@PathVariable String backlog_id) {
        return new ResponseEntity<>(projectTaskService.findBacklogById(backlog_id), HttpStatus.OK);
    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id) {
        return new ResponseEntity<>(projectTaskService.findPTByProjectSequence(backlog_id, pt_id), HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, @PathVariable String backlog_id,
                                               @PathVariable String pt_id, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = validationErrorService.checkForErrors(bindingResult);
        if(errorMap != null)
            return errorMap;

        return new ResponseEntity<>(projectTaskService.updateByProjectSequence(projectTask, backlog_id, pt_id), HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id) {
        projectTaskService.deletePTByProjectSequence(backlog_id, pt_id);
        return new ResponseEntity<>("Deleted ProjectTask with Id: "+ pt_id, HttpStatus.OK);
    }
}

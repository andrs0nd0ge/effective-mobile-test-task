package controllers;

import dto.TaskDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.TaskService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("create")
    public String createTask() {
        return "done";
    }

    @GetMapping("main")
    public ResponseEntity<List<TaskDto>> fetchTasksOfOthers(
            @RequestParam(name = "author_id") long authorId
    ) {
        List<TaskDto> tasks = taskService.getTasksOfOtherUsers(authorId);

        if (tasks.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}

package controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    @PostMapping("create")
    public String createTask() {
        return "done";
    }

    @GetMapping
    public String fetchTasksOfOthers() {
        return "done";
    }
}

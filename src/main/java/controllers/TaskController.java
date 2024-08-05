package controllers;

import dto.*;

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

    @GetMapping("all")
    public ResponseEntity<List<TaskDto>> fetchAllTasks(
            @RequestParam(name = "p") int page
    ) {
        List<TaskDto> tasks = taskService.getAllTasks(page);

        if (tasks.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("of_others")
    public ResponseEntity<List<TaskDto>> fetchTasksOfOtherUsers(
            @RequestParam(name = "p") int page,
            @RequestParam(name = "user_id") long userId
    ) {
        List<TaskDto> tasks = taskService.getTasksOfOtherUsers(page, userId);

        if (tasks.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> fetchTasksOfUser(
            @RequestParam(required = false, name = "p") Integer page,
            @RequestParam(required = false, name = "status_id") Integer statusId,
            @RequestParam(required = false, name = "priority_id") Integer priorityId,
            @RequestParam(required = false) String header,
            @RequestParam(required = false) String description,
            @RequestParam(name = "user_id") long userId
    ) {
        List<TaskDto> tasks = taskService.getTasksOfUser(page, statusId, priorityId, header, description, userId);

        if (tasks.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<ResponseDto> createTask(@RequestBody CreateTaskDto taskDto,
                                                  @RequestParam(name = "author_id") long authorId) {
        taskService.createTask(taskDto, authorId);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PostMapping("edit")
    public ResponseEntity<ResponseDto> editTask(@RequestBody EditTaskDto taskDto,
                                                @RequestParam(name = "author_id") long authorId) {

        // TODO Return message "Task ID cannot be null" (using enums)
        Long taskId = taskDto.getTaskId();

        if (taskId == null) {
            HttpStatus badRequest = HttpStatus.BAD_REQUEST;

            ResponseDto responseDto = new ResponseDto();
            responseDto.setStatus(badRequest.value());
            responseDto.setMessage(badRequest.getReasonPhrase());

            return new ResponseEntity<>(responseDto, badRequest);
        }
        // TODO Return message "Task ID cannot be null" (using enums)

        taskService.editTask(taskDto, authorId);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto> deleteTask(@RequestBody DeleteTaskDto taskDto,
                                                  @RequestParam(name = "author_id") long authorId) {
        // TODO Return message "Task ID cannot be null" (using enums)
        Long taskId = taskDto.getTaskId();

        if (taskId == null) {
            HttpStatus badRequest = HttpStatus.BAD_REQUEST;

            ResponseDto responseDto = new ResponseDto();
            responseDto.setStatus(badRequest.value());
            responseDto.setMessage(badRequest.getReasonPhrase());

            return new ResponseEntity<>(responseDto, badRequest);
        }
        // TODO Return message "Task ID cannot be null" (using enums)

        taskService.deleteTask(taskDto, authorId);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PostMapping("change_status")
    public ResponseEntity<ResponseDto> changeStatusOfTask(@RequestBody ChangeStatusTaskDto taskDto,
                                                          @RequestParam(name = "user_id") long userId) {
        // TODO Return message "Task ID cannot be null" (using enums)
        Long taskId = taskDto.getTaskId();

        if (taskId == null) {
            HttpStatus badRequest = HttpStatus.BAD_REQUEST;

            ResponseDto responseDto = new ResponseDto();
            responseDto.setStatus(badRequest.value());
            responseDto.setMessage(badRequest.getReasonPhrase());

            return new ResponseEntity<>(responseDto, badRequest);
        }
        // TODO Return message "Task ID cannot be null" (using enums)

        taskService.changeStatusOfTask(taskDto, userId);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }


    @PostMapping("appoint")
    public ResponseEntity<ResponseDto> appointExecutorToTask(@RequestBody ExecutorTaskDto taskDto,
                                                             @RequestParam(name = "author_id") long authorId) {
        // TODO Return message "Task ID cannot be null" (using enums)
        Long taskId = taskDto.getTaskId();

        if (taskId == null) {
            HttpStatus badRequest = HttpStatus.BAD_REQUEST;

            ResponseDto responseDto = new ResponseDto();
            responseDto.setStatus(badRequest.value());
            responseDto.setMessage(badRequest.getReasonPhrase());

            return new ResponseEntity<>(responseDto, badRequest);
        }
        // TODO Return message "Task ID cannot be null" (using enums)

        taskService.appointExecutorToTask(taskDto, authorId);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PostMapping("comment")
    public ResponseEntity<ResponseDto> addCommentToTask(@RequestBody CommentTaskDto taskDto) {
        // TODO Return message "Task ID cannot be null" (using enums)
        Long taskId = taskDto.getTaskId();

        if (taskId == null) {
            HttpStatus badRequest = HttpStatus.BAD_REQUEST;

            ResponseDto responseDto = new ResponseDto();
            responseDto.setStatus(badRequest.value());
            responseDto.setMessage(badRequest.getReasonPhrase());

            return new ResponseEntity<>(responseDto, badRequest);
        }
        // TODO Return message "Task ID cannot be null" (using enums)

        taskService.addCommentToTask(taskDto);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }
}

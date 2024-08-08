package controllers;

import dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import services.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks from the database - only available for authors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All tasks are successfully fetched from the database",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No tasks were found",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorised or not enough privileges (e.g. being an executor instead of an author)",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error: something wrong on the server side",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            )
    })
    @GetMapping("all")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<List<TaskDto>> fetchAllTasks(
            @Parameter(description = "Page - used for pagination")
            @RequestParam(required = false, name = "p") Integer page
    ) {
        List<TaskDto> tasks = taskService.getAllTasks(page);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


    @Operation(summary = "Get tasks of other users from the database - only available for authors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The tasks are successfully fetched from the database",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No tasks were found",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorised or not enough privileges (e.g. being an executor instead of an author)",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error: something wrong on the server side",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            )
    })
    @GetMapping("of_others")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<List<TaskDto>> fetchTasksOfOtherUsers(
            @Parameter(description = "Page - used for pagination")
            @RequestParam(required = false, name = "p") Integer page,

            Authentication auth
    ) {
        List<TaskDto> tasks = taskService.getTasksOfOtherUsers(page, auth);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


    @Operation(summary = "Get tasks of specific user from the database - only available for authors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The tasks are successfully fetched from the database",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No tasks were found",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorised or not enough privileges (e.g. being an executor instead of an author)",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error: something wrong on the server side",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            )
    })
    @GetMapping
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<List<TaskDto>> fetchTasksOfUser(
            @Parameter(description = "Page - used for pagination")
            @RequestParam(required = false, name = "p") Integer page,

            @Parameter(description = "Task's status ID - used for filtration")
            @RequestParam(required = false, name = "status_id") Integer statusId,

            @Parameter(description = "Task's priority ID - used for filtration")
            @RequestParam(required = false, name = "priority_id") Integer priorityId,

            @Parameter(description = "Task's header - used to get task of a user by specific header's name (ignoring trailing spaces)")
            @RequestParam(required = false) String header,

            @Parameter(description = "Task's description - used to get task of a user by specific description (ignoring trailing spaces)")
            @RequestParam(required = false) String description,

            @Parameter(description = "ID of the user whose tasks need to be fetched from the database")
            @RequestParam(name = "user_id") Long userId
    ) {
        List<TaskDto> tasks = taskService.getTasksOfUser(page, statusId, priorityId, header, description, userId);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


    @Operation(summary = "Create a task - only available for authors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The task is successfully created",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "This status code cannot be returned for this endpoint",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorised or not enough privileges (e.g. being an executor instead of an author)",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error: something wrong on the server side",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            )
    })
    @PostMapping("create")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ResponseDto> createTask(
            @RequestBody CreateTaskDto taskDto,
            Authentication auth
    ) {
        taskService.createTask(taskDto, auth);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }


    @Operation(summary = "Edit a task - only available for authors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The task is successfully edited",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "This status code cannot be returned for this endpoint",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorised or not enough privileges (e.g. being an executor instead of an author)",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error: something wrong on the server side",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            )
    })
    @PostMapping("edit")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ResponseDto> editTask(
            @RequestBody EditTaskDto taskDto,
            Authentication auth
    ) {
        taskService.editTask(taskDto, auth);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }


    @Operation(summary = "Delete a task - only available for authors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The task is successfully deleted",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "This status code cannot be returned for this endpoint",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorised or not enough privileges (e.g. being an executor instead of an author)",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error: something wrong on the server side",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            )
    })
    @DeleteMapping("delete")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ResponseDto> deleteTask(
            @RequestBody DeleteTaskDto taskDto,
            Authentication auth
    ) {
        taskService.deleteTask(taskDto, auth);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }


    @Operation(summary = "Change the status of a task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The task's status is successfully changed",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "This status code cannot be returned for this endpoint",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorised",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error: something wrong on the server side",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            )
    })
    @PostMapping("change_status")
    @PreAuthorize("hasAnyRole('AUTHOR', 'EXECUTOR')")
    public ResponseEntity<ResponseDto> changeStatusOfTask(
            @RequestBody ChangeStatusTaskDto taskDto,
            Authentication auth
    ) {
        taskService.changeStatusOfTask(taskDto, auth);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }


    @Operation(summary = "Appoint executor to a task - only available for authors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Executor is successfully appointed to the task",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "This status code cannot be returned for this endpoint",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorised or not enough privileges (e.g. being an executor instead of an author)",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error: something wrong on the server side",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            )
    })
    @PostMapping("appoint")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ResponseDto> appointExecutorToTask(
            @RequestBody ExecutorTaskDto taskDto,
            Authentication auth
    ) {
        taskService.appointExecutorToTask(taskDto, auth);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }


    @Operation(summary = "Post comment on a task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment is posted successfully",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "This status code cannot be returned for this endpoint",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorised",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error: something wrong on the server side",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            )
    })
    @PostMapping("comment")
    @PreAuthorize("hasAnyRole('AUTHOR', 'EXECUTOR')")
    public ResponseEntity<ResponseDto> addCommentToTask(
            @RequestBody CommentTaskDto taskDto
    ) {
        taskService.addCommentToTask(taskDto);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(httpStatus.value());
        responseDto.setMessage(httpStatus.getReasonPhrase());

        return new ResponseEntity<>(responseDto, httpStatus);
    }
}

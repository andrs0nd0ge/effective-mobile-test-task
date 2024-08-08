package services;

import dao.TaskDao;
import dto.*;
import exceptions.ForbiddenException;
import exceptions.TaskIdNullException;
import exceptions.TasksNotFoundException;
import exceptions.UserIdNullException;
import models.Task;
import models.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskDao taskDao;

    public TaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public List<TaskDto> getAllTasks(Integer page) {
        List<Task> tasks = taskDao.getAllTasks(page);

        List<TaskDto> taskDtos = tasks.stream()
                .map(TaskDto::from)
                .toList();

        if (taskDtos.isEmpty()) {
            throw new TasksNotFoundException();
        }

        return taskDtos;
    }

    public List<TaskDto> getTasksOfOtherUsers(Integer page, Authentication auth) {
        User user = (User) auth.getPrincipal();

        long userId;

        if (user != null) {
            userId = user.getId();
        } else {
            throw new ForbiddenException();
        }

        List<Task> tasks = taskDao.getTasksOfOtherUsers(page, userId);

        List<TaskDto> taskDtos = tasks.stream()
                .map(TaskDto::from)
                .toList();

        if (taskDtos.isEmpty()) {
            throw new TasksNotFoundException();
        }

        return taskDtos;
    }

    public List<TaskDto> getTasksOfUser(Integer page,
                                        Integer statusId,
                                        Integer priorityId,
                                        String header,
                                        String description,
                                        Long userId) {
        if (userId == null) {
            throw new UserIdNullException();
        }

        header = header != null ? header.trim() : null;

        description = description != null ? description.trim() : null;

        List<Task> tasks = taskDao.getTasksOfUser(page, statusId, priorityId, header, description, userId);

        List<TaskDto> taskDtos = tasks.stream()
                .map(TaskDto::from)
                .toList();

        if (taskDtos.isEmpty()) {
            throw new TasksNotFoundException();
        }

        return taskDtos;
    }

    public void createTask(CreateTaskDto taskDto, Authentication auth) {
        User author = (User) auth.getPrincipal();

        long authorId;

        if (author != null) {
            authorId = author.getId();
        } else {
            throw new ForbiddenException();
        }

        String header = taskDto.getHeader();
        String description = taskDto.getDescription();

        Long statusId = taskDto.getStatusId();

        if (statusId == null || statusId == 0L) {
            statusId = 1L;
        }

        Long priorityId = taskDto.getPriorityId();

        if (priorityId == null || priorityId == 0L) {
            priorityId = 1L;
        }

        taskDao.createTask(header, description, statusId, priorityId, authorId);
    }

    public void editTask(EditTaskDto taskDto, Authentication auth) {
        User author = (User) auth.getPrincipal();

        long authorId;

        if (author != null) {
            authorId = author.getId();
        } else {
            throw new ForbiddenException();
        }

        Long taskId = taskDto.getTaskId();

        if (taskId == null) {
            throw new TaskIdNullException();
        }

        String header = taskDto.getHeader();
        String description = taskDto.getDescription();

        taskDao.editTask(taskId, header, description, authorId);
    }

    public void deleteTask(DeleteTaskDto taskDto, Authentication auth) {
        User author = (User) auth.getPrincipal();

        long authorId;

        if (author != null) {
            authorId = author.getId();
        } else {
            throw new ForbiddenException();
        }

        Long taskId = taskDto.getTaskId();

        if (taskId == null) {
            throw new TaskIdNullException();
        }

        taskDao.deleteTask(taskId, authorId);
    }

    public void changeStatusOfTask(ChangeStatusTaskDto taskDto, Authentication auth) {
        User user = (User) auth.getPrincipal();

        long userId;

        if (user != null) {
            userId = user.getId();
        } else {
            throw new ForbiddenException();
        }

        Long taskId = taskDto.getTaskId();

        if (taskId == null) {
            throw new TaskIdNullException();
        }

        long statusId = taskDto.getStatusId();

        taskDao.changeStatusOfTask(taskId, statusId, userId);
    }

    public void appointExecutorToTask(ExecutorTaskDto taskDto, Authentication auth) {
        User author = (User) auth.getPrincipal();

        long authorId;

        if (author != null) {
            authorId = author.getId();
        } else {
            throw new ForbiddenException();
        }

        Long taskId = taskDto.getTaskId();

        if (taskId == null) {
            throw new TaskIdNullException();
        }

        long executorId = taskDto.getExecutorId();

        taskDao.appointExecutorToTask(taskId, authorId, executorId);
    }

    public void addCommentToTask(CommentTaskDto taskDto) {
        Long taskId = taskDto.getTaskId();

        if (taskId == null) {
            throw new TaskIdNullException();
        }

        String comment = taskDto.getComment();

        taskDao.addCommentToTask(taskId, comment);
    }
}

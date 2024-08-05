package services;

import dao.TaskDao;
import dto.*;
import models.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskDao taskDao;

    public TaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskDao.getAllTasks();

        return tasks.stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksOfOtherUsers(long authorId) {
        List<Task> tasks = taskDao.getTasksOfOtherUsers(authorId);

        return tasks.stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksOfUser(long authorId) {
        List<Task> tasks = taskDao.getTasksOfUser(authorId);



        return tasks.stream()
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }

    public void createTask(CreateTaskDto taskDto, long authorId) {
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

    public void editTask(EditTaskDto taskDto, long authorId) {
        long taskId = taskDto.getTaskId();

        String header = taskDto.getHeader();
        String description = taskDto.getDescription();

        long statusId = taskDto.getStatusId();

        long priorityId = taskDto.getPriorityId();

        long executorId = taskDto.getExecutorId();

        taskDao.editTask(taskId, header, description, statusId, priorityId, authorId, executorId);
    }

    public void deleteTask(DeleteTaskDto taskDto, long authorId) {
        long taskId = taskDto.getTaskId();

        taskDao.deleteTask(taskId, authorId);
    }

    public void changeStatusOfTask(ChangeStatusTaskDto taskDto, long userId) {
        long taskId = taskDto.getTaskId();

        long statusId = taskDto.getStatusId();

        taskDao.changeStatusOfTask(taskId, statusId, userId);
    }

    public void appointExecutorToTask(ExecutorTaskDto taskDto, long authorId) {
        long taskId = taskDto.getTaskId();

        long executorId = taskDto.getExecutorId();

        taskDao.appointExecutorToTask(taskId, authorId, executorId);
    }

    public void addCommentToTask(CommentTaskDto taskDto) {
        long taskId = taskDto.getTaskId();

        String comment = taskDto.getComment();

        taskDao.addCommentToTask(taskId, comment);
    }
}

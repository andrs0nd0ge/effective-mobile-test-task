package services;

import dao.TaskDao;
import dto.TaskDto;
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

    public void createTask(TaskDto taskDto, long authorId) {
        String header = taskDto.getHeader();
        String description = taskDto.getDescription();

        Long statusId = taskDto.getStatusId();

        if (statusId == null || statusId == 0L) {
            statusId = 1L;
        }

        Long priorityId = taskDto.getPriorityId();

        if (priorityId == null || priorityId == 0L) {
            priorityId = 3L;
        }

        taskDao.createTask(header, description, statusId, priorityId, authorId);
    }

    public void editTask(TaskDto taskDto, long authorId) {
        long taskId = taskDto.getTaskId();

        String header = taskDto.getHeader();
        String description = taskDto.getDescription();

        long statusId = taskDto.getStatusId();

        long priorityId = taskDto.getPriorityId();

        long executorId = taskDto.getExecutorId();

        taskDao.editTask(taskId, header, description, statusId, priorityId, authorId, executorId);
    }

    public void deleteTask(TaskDto taskDto, long authorId) {
        long taskId = taskDto.getTaskId();

        taskDao.deleteTask(taskId, authorId);
    }

    public void changeStatusOfTask(TaskDto taskDto, long userId) {
        long taskId = taskDto.getTaskId();

        long statusId = taskDto.getStatusId();

        taskDao.changeStatusOfTask(taskId, statusId, userId);
    }

    public void appointExecutorToTask(TaskDto taskDto, long authorId) {
        long taskId = taskDto.getTaskId();

        long executorId = taskDto.getExecutorId();

        taskDao.appointExecutorToTask(taskId, authorId, executorId);
    }
}

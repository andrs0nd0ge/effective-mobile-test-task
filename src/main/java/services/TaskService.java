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
}

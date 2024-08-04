package dao;

import models.Task;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public TaskDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public List<Task> getTasksOfOtherUsers(long authorId) {
        String sql = "select * from w.tasks where author_id <> :authorId";

        return namedJdbcTemplate.query(sql,
                new MapSqlParameterSource().addValue("authorId", authorId),
                new BeanPropertyRowMapper<>(Task.class));
    }

    public List<Task> getTasksOfUser(long authorId) {
        String sql = "select * from w.tasks where author_id = :authorId";

        return namedJdbcTemplate.query(sql,
                new MapSqlParameterSource().addValue("authorId", authorId),
                new BeanPropertyRowMapper<>(Task.class));
    }

    public void createTask(String header, String description, long statusId, long priorityId, long authorId) {
        String sql = "insert into w.tasks (header, description, status_id, priority_id, author_id, executor_id) " +
                "values (:header, :description, :statusId, :priorityId, :authorId, :authorId)";

        namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("header", header)
                .addValue("description", description)
                .addValue("statusId", statusId)
                .addValue("priorityId", priorityId)
                .addValue("authorId", authorId));
    }

    public void editTask(long taskId, String header, String description, long statusId, long priorityId, long authorId, long executorId) {
        String sql = "update w.tasks " +
                "set header = :header, " +
                "    description = :description, " +
                "    status_id = :statusId, " +
                "    priority_id = :priorityId, " +
                "    executor_id = :executorId " +
                "where author_id = :authorId and id = :taskId";

        namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("header", header)
                .addValue("description", description)
                .addValue("statusId", statusId)
                .addValue("priorityId", priorityId)
                .addValue("executorId", executorId)
                .addValue("authorId", authorId)
                .addValue("taskId", taskId));
    }

    public void deleteTask(long taskId, long authorId) {
        String sql = "delete from w.tasks where id = :taskId and author_id = :authorId";

        namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("taskId", taskId)
                .addValue("authorId", authorId));
    }

    public void changeStatusOfTask(long taskId, long statusId, long userId) {
        String sql = "update w.tasks " +
                "set status_id = :statusId " +
                "where (author_id = :userId or executor_id = :userId) and id = :taskId";

        namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("statusId", statusId)
                .addValue("userId", userId)
                .addValue("taskId", taskId));
    }

    public void appointExecutorToTask(long taskId, long authorId, long executorId) {
        String sql = "update w.tasks " +
                "set executor_id = :executorId " +
                "where author_id = :authorId and id = :taskId";

        namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("authorId", authorId)
                .addValue("executorIr", executorId)
                .addValue("taskId", taskId));
    }

    public void addCommentToTask(long taskId, String comment) {
        String sql = "insert into w.comments (comment, task_id) " +
                "values (:comment, :taskId)";

        namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("comment", comment)
                .addValue("taskId", taskId));
    }
}

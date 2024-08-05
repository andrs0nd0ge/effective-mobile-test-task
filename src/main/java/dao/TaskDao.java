package dao;

import mappers.TaskRowMapper;
import models.Comment;
import models.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    @Value("${page.size}")
    private int pageSize;
    public TaskDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public List<Task> getAllTasks(int page) {
        String sql = "select t.id as task_id, " +
                "       t.header, " +
                "       t.description, " +
                "       s.name as status, " +
                "       p.name as priority, " +
                "       u.username as author_username, " +
                "       e.username as executor_username " +
                "from w.tasks as t " +
                "join w.statuses as s on t.status_id = s.id " +
                "join w.priorities as p on t.priority_id = p.id " +
                "join w.users as u on t.author_id = u.id " +
                "join w.users as e on t.executor_id = e.id " +
                "order by t.id " +
                "limit :pageSize offset (:pageNumber - 1) * :pageSize";

        List<Task> tasks = namedJdbcTemplate.query(sql, new MapSqlParameterSource()
                .addValue("pageSize", pageSize)
                .addValue("pageNumber", page),
                new TaskRowMapper());

        for (Task task : tasks) {
            List<Comment> comments = getCommentsForTask(task.getId());

            task.setComments(comments);
        }

        return tasks;
    }

    public List<Task> getTasksOfOtherUsers(int page, long userId) {
        String sql = "select t.id as task_id, " +
                "       t.header, " +
                "       t.description, " +
                "       s.name as status, " +
                "       p.name as priority, " +
                "       u.username as author_username, " +
                "       e.username as executor_username " +
                "from w.tasks as t " +
                "inner join w.statuses as s on t.status_id = s.id " +
                "inner join w.priorities as p on t.priority_id = p.id " +
                "inner join w.users as u on t.author_id = u.id " +
                "inner join w.users as e on t.executor_id = e.id " +
                "where (t.author_id <> :userId and t.executor_id <> :userId) " +
                "order by t.id " +
                "limit :pageSize offset (:pageNumber - 1) * :pageSize";

        List<Task> tasks = namedJdbcTemplate.query(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("pageSize", pageSize)
                .addValue("pageNumber", page),
                new TaskRowMapper());

        for (Task task : tasks) {
            List<Comment> comments = getCommentsForTask(task.getId());

            task.setComments(comments);
        }

        return tasks;
    }

    public List<Task> getTasksOfUser(int page, long userId) {
        String sql = "select t.id as task_id, " +
                "       t.header, " +
                "       t.description, " +
                "       s.name as status, " +
                "       p.name as priority, " +
                "       u.username as author_username, " +
                "       e.username as executor_username " +
                "from w.tasks as t " +
                "inner join w.statuses as s on t.status_id = s.id " +
                "inner join w.priorities as p on t.priority_id = p.id " +
                "inner join w.users as u on t.author_id = u.id " +
                "inner join w.users as e on t.executor_id = e.id " +
                "where (t.author_id = :userId or t.executor_id = :userId) " +
                "order by t.id " +
                "limit :pageSize offset (:pageNumber - 1) * :pageSize";

        List<Task> tasks = namedJdbcTemplate.query(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("pageSize", pageSize)
                .addValue("pageNumber", page),
                new TaskRowMapper());

        for (Task task : tasks) {
            List<Comment> comments = getCommentsForTask(task.getId());

            task.setComments(comments);
        }

        return tasks;
    }

    private List<Comment> getCommentsForTask(long taskId) {
        String sql = "select c.id, c.comment " +
                "from w.comments as c " +
                "inner join w.tasks as t on c.task_id = t.id " +
                "where c.task_id = :taskId";

        return namedJdbcTemplate.query(sql,
                new MapSqlParameterSource().addValue("taskId", taskId),
                new BeanPropertyRowMapper<>(Comment.class));
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

    public void editTask(long taskId, String header, String description, long authorId) {
        String sql = "update w.tasks " +
                "set header = :header, " +
                "    description = :description " +
                "where author_id = :authorId and id = :taskId";

        namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("header", header)
                .addValue("description", description)
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
                .addValue("executorId", executorId)
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

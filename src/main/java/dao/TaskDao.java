package dao;

import exceptions.UnhandledException;
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

    public List<Task> getAllTasks(Integer page) {
        try {
            StringBuilder sqlStringBuilder = new StringBuilder("select t.id as task_id, " +
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
                    "order by t.id ");

            MapSqlParameterSource parameterSource = new MapSqlParameterSource();

            if (page != null) {
                sqlStringBuilder.append("limit :pageSize offset (:pageNumber - 1) * :pageSize ");
                parameterSource
                        .addValue("pageSize", pageSize)
                        .addValue("pageNumber", page);
            }

            String sql = sqlStringBuilder.toString();

            List<Task> tasks = namedJdbcTemplate.query(sql, parameterSource, new TaskRowMapper());

            for (Task task : tasks) {
                List<Comment> comments = getCommentsForTask(task.getId());

                task.setComments(comments);
            }

            return tasks;
        } catch (RuntimeException ex) {
            ex.printStackTrace();

            throw new UnhandledException(ex);
        }
    }

    public List<Task> getTasksOfOtherUsers(Integer page, long userId) {
        try {
            StringBuilder sqlStringBuilder = new StringBuilder("select t.id as task_id, " +
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
                    "order by t.id ");

            MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("userId", userId);

            if (page != null) {
                sqlStringBuilder.append("limit :pageSize offset (:pageNumber - 1) * :pageSize ");
                parameterSource
                        .addValue("pageSize", pageSize)
                        .addValue("pageNumber", page);
            }

            String sql = sqlStringBuilder.toString();

            List<Task> tasks = namedJdbcTemplate.query(sql, parameterSource, new TaskRowMapper());

            for (Task task : tasks) {
                List<Comment> comments = getCommentsForTask(task.getId());

                task.setComments(comments);
            }

            return tasks;
        } catch (RuntimeException ex) {
            throw new UnhandledException(ex);
        }
    }

    public List<Task> getTasksOfUser(Integer page,
                                     Integer statusId,
                                     Integer priorityId,
                                     String header,
                                     String description,
                                     long userId) {
        try {
            StringBuilder sqlStringBuilder = new StringBuilder(
                    "select t.id as task_id, " +
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
                            "where (t.author_id = :userId or t.executor_id = :userId)"
            );


            MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("userId", userId);

            if (statusId != null) {
                sqlStringBuilder.append(" and (:statusId is null or t.status_id = :statusId) ");
                parameterSource.addValue("statusId", statusId);
            }

            if (priorityId != null) {
                sqlStringBuilder.append(" and (:priorityId is null or t.priority_id = :priorityId) ");
                parameterSource.addValue("priorityId", priorityId);
            }

            if (header != null) {
                sqlStringBuilder.append(" and (:header is null or lower(t.header) like concat('%', lower(:header), '%')) ");
                parameterSource.addValue("header", header);
            }

            if (description != null) {
                sqlStringBuilder.append(" and (:description is null or lower(t.description) like concat('%', lower(:description), '%')) ");
                parameterSource.addValue("description", description);
            }

            sqlStringBuilder.append(" order by t.id ");

            if (page != null) {
                sqlStringBuilder.append(" limit :pageSize offset (:pageNumber - 1) * :pageSize ");
                parameterSource
                        .addValue("pageSize", pageSize)
                        .addValue("pageNumber", page);
            }

            String sql = sqlStringBuilder.toString();

            List<Task> tasks = namedJdbcTemplate.query(sql, parameterSource, new TaskRowMapper());

            for (Task task : tasks) {
                List<Comment> comments = getCommentsForTask(task.getId());

                task.setComments(comments);
            }

            return tasks;
        } catch (RuntimeException ex) {
            throw new UnhandledException(ex);
        }
    }

    private List<Comment> getCommentsForTask(long taskId) {
        try {
            String sql = "select c.id, c.comment " +
                    "from w.comments as c " +
                    "inner join w.tasks as t on c.task_id = t.id " +
                    "where c.task_id = :taskId";

            return namedJdbcTemplate.query(sql,
                    new MapSqlParameterSource().addValue("taskId", taskId),
                    new BeanPropertyRowMapper<>(Comment.class));
        } catch (RuntimeException ex) {
            throw new UnhandledException(ex);
        }
    }

    public void createTask(String header, String description, long statusId, long priorityId, long authorId) {
        try {
            String sql = "insert into w.tasks (header, description, status_id, priority_id, author_id, executor_id) " +
                    "values (:header, :description, :statusId, :priorityId, :authorId, :authorId)";

            namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("header", header)
                    .addValue("description", description)
                    .addValue("statusId", statusId)
                    .addValue("priorityId", priorityId)
                    .addValue("authorId", authorId));
        } catch (RuntimeException ex) {
            throw new UnhandledException(ex);
        }
    }

    public void editTask(long taskId, String header, String description, long authorId) {
        try {
            String sql = "update w.tasks " +
                    "set header = :header, " +
                    "    description = :description " +
                    "where author_id = :authorId and id = :taskId";

            namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("header", header)
                    .addValue("description", description)
                    .addValue("authorId", authorId)
                    .addValue("taskId", taskId));
        } catch (RuntimeException ex) {
            throw new UnhandledException(ex);
        }
    }

    public void deleteTask(long taskId, long authorId) {
        try {
            String sql = "delete from w.tasks where id = :taskId and author_id = :authorId";

            namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("taskId", taskId)
                    .addValue("authorId", authorId));
        } catch (RuntimeException ex) {
            throw new UnhandledException(ex);
        }
    }

    public void changeStatusOfTask(long taskId, long statusId, long userId) {
        try {
            String sql = "update w.tasks " +
                    "set status_id = :statusId " +
                    "where (author_id = :userId or executor_id = :userId) and id = :taskId";

            namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("statusId", statusId)
                    .addValue("userId", userId)
                    .addValue("taskId", taskId));
        } catch (RuntimeException ex) {
            throw new UnhandledException(ex);
        }
    }

    public void appointExecutorToTask(long taskId, long authorId, long executorId) {
        try {
            String sql = "update w.tasks " +
                    "set executor_id = :executorId " +
                    "where author_id = :authorId and id = :taskId";

            namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("authorId", authorId)
                    .addValue("executorId", executorId)
                    .addValue("taskId", taskId));
        } catch (RuntimeException ex) {
            throw new UnhandledException(ex);
        }
    }

    public void addCommentToTask(long taskId, String comment) {
        try {
            String sql = "insert into w.comments (comment, task_id) " +
                    "values (:comment, :taskId)";

            namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("comment", comment)
                    .addValue("taskId", taskId));
        } catch (RuntimeException ex) {
            throw new UnhandledException(ex);
        }
    }
}

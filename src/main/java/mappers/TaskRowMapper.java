package mappers;

import models.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();
        task.setId(rs.getLong("task_id"));
        task.setHeader(rs.getString("header"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getString("priority"));

        User author = new User();
        author.setUsername(rs.getString("author_username"));

        task.setAuthor(author);

        User executor = new User();
        executor.setUsername(rs.getString("executor_username"));

        task.setExecutor(executor);

        return task;
    }
}

package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import models.*;

import java.util.List;

@Getter
@Setter
@Builder
public class TaskDto {
    @JsonProperty("task_id")
    private Long taskId;
    private String header;
    private String description;
    private String status;
    private String priority;
    private String author;
    private String executor;
    private List<CommentDto> comments;

    public static TaskDto from(Task task) {
        return builder()
                .taskId(task.getId())
                .header(task.getHeader())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(task.getAuthor() != null ? task.getAuthor().getUsername() : null)
                .executor(task.getExecutor() != null ? task.getExecutor().getUsername() : null)
                .comments(task.getComments().stream().map(CommentDto::from).toList())
                .build();
    }
}
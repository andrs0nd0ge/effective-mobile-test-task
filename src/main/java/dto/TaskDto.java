package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import models.Task;

@Getter
@Setter
@Builder
public class TaskDto {
    @JsonProperty("task_id")
    private Long taskId;
    private String header;
    private String description;
    @JsonProperty("status_id")
    private Long statusId;
    @JsonProperty("priority_id")
    private Long priorityId;
    @JsonProperty("author_id")
    private Long authorId;
    @JsonProperty("executor_id")
    private Long executorId;

    public static TaskDto from(Task task) {
        return builder()
                .header(task.getHeader())
                .description(task.getDescription())
                .statusId(task.getStatusId())
                .priorityId(task.getPriorityId())
                .authorId(task.getAuthorId())
                .executorId(task.getExecutorId())
                .build();
    }
}
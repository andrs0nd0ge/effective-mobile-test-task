package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import models.Task;

@Getter
@Setter
@Builder
public class TaskDto {
    private String header;
    private String description;
    private long statusId;
    private long priorityId;
    private long authorId;
    private long executorId;

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
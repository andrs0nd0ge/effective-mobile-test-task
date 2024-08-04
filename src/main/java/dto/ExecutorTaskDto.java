package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecutorTaskDto {
    @JsonProperty("task_id")
    private Long taskId;
    @JsonProperty("executor_id")
    private Long executorId;
}

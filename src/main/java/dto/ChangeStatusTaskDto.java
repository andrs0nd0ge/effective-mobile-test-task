package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeStatusTaskDto {
    @JsonProperty("task_id")
    private Long taskId;
    @JsonProperty("status_id")
    private Long statusId;
}

package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteTaskDto {
    @JsonProperty("task_id")
    private Long taskId;
}

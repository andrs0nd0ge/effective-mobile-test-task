package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditTaskDto {
    @JsonProperty("task_id")
    private Long taskId;
    private String header;
    private String description;
}

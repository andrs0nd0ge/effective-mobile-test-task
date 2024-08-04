package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskDto {
    private String header;
    private String description;
    @JsonProperty("status_id")
    private Long statusId;
    @JsonProperty("priority_id")
    private Long priorityId;
}
